package com.meest.social.socialViewModel.viewModel.trendingViewModel;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.viewpager.widget.ViewPager;

import com.meest.R;
import com.meest.databinding.TrendingFragmentModelBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.social.socialViewModel.adapter.HashtagNameAdapter;
import com.meest.social.socialViewModel.adapter.trendingAdapters.TopTrendingSearchPagerAdapter;
import com.meest.social.socialViewModel.model.HashtagSearch;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.recordvideo.Utilss;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TrendingFragmentViewModel extends ViewModel {
    private static final String TAG = "TrendingFragmentViewModel";
    public Context context;
    public Activity activity;
    public TrendingFragmentModelBinding binding;
    public int selectedPos = 0;
    public FragmentManager fragmentManager;
    public Fragment fragment;
    public HashtagNameAdapter hashtagNameAdapter;
    public CompositeDisposable disposable = new CompositeDisposable();


    TopTrendingSearchPagerAdapter trendingSearchPagerAdapter;

    public long hashTagResponseSize = 0;


    public TrendingFragmentViewModel(Context context, FragmentActivity activity, TrendingFragmentModelBinding binding, FragmentManager childFragmentManager) {
        this.activity = activity;
        this.context = context;
        this.binding = binding;
        this.fragmentManager = childFragmentManager;

        hashtagNameAdapter = new HashtagNameAdapter(context);
    }

    public void inits() {
        binding.editDataFind.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (selectedPos == 0) {
                    if (binding.output.getAdapter() != null) {
                        TrendingFragViewModel vvf = (TrendingFragViewModel) binding.output.getAdapter().instantiateItem(binding.output, binding.output.getCurrentItem());
                        vvf.updateSearch(binding.editDataFind.getText().toString());
                    }

                } else if (selectedPos == 1) {
                    if (binding.output.getAdapter() != null) {
                        TopPostFragViewModel vpf = (TopPostFragViewModel) binding.output.getAdapter().instantiateItem(binding.output, binding.output.getCurrentItem());
                        vpf.updateSearch(binding.editDataFind.getText().toString());
                    }

                }
                return false;
            }
            return false;
        });

        trendingSearchPagerAdapter = new TopTrendingSearchPagerAdapter(fragmentManager, context, false, binding.editDataFind.getText().toString());
        binding.output.setAdapter(trendingSearchPagerAdapter);
        //binding.output.setAdapter(new TopTrendingSearchPagerAdapter(fragmentManager, context, false, binding.editDataFind.getText().toString()));
        binding.output.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        selectPeople();
                        break;
                    case 1:
                        selectVideo();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        binding.editDataFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    binding.ivClear.setVisibility(View.GONE);
                } else {
                    binding.ivClear.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        binding.ivClear.setOnClickListener(v -> binding.editDataFind.getText().clear());
        binding.layoutTrending.setOnClickListener(v -> selectPeople());
        binding.layoutTops.setOnClickListener(v -> selectVideo());

    }

    public void selectPeople() {
        binding.txtTrending.setTextColor(ContextCompat.getColor(context, R.color.social_background_blue));
        binding.txtTop.setTextColor(ContextCompat.getColor(context, R.color.gray));
        binding.viewTrending.setVisibility(View.VISIBLE);
        binding.viewTop.setVisibility(View.INVISIBLE);
        selectedPos = 0;
        binding.output.setCurrentItem(selectedPos);
    }

    public void selectVideo() {
        binding.txtTrending.setTextColor(ContextCompat.getColor(context, R.color.gray));
        binding.txtTop.setTextColor(ContextCompat.getColor(context, R.color.social_background_blue));
        binding.viewTrending.setVisibility(View.INVISIBLE);
        binding.viewTop.setVisibility(View.VISIBLE);
        selectedPos = 1;
        binding.output.setCurrentItem(selectedPos);
    }

    public void fetchHashTag(boolean loadMore, int pageno) {

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        HashMap<String, String> body = new HashMap<>();
        body.put("pageSize", "20");
        body.put("key", "");
        body.put("pageNumber", String.valueOf(pageno));
        disposable.add(Global.initSocialRetrofit().searchHashTag(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> binding.loading.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> binding.loading.setVisibility(View.GONE))
                .subscribe((HashtagSearch hashtagSearchResponse, Throwable throwable) -> {
                    binding.loading.setVisibility(View.GONE);
                    if (hashtagSearchResponse != null) {
                        if (hashtagSearchResponse.getSuccess()) {
                            if (hashtagSearchResponse.getData() != null) {
                                if (hashtagSearchResponse.getData().getRows() != null) {

                                    hashTagResponseSize = hashTagResponseSize + hashtagSearchResponse.getData().getRows().size();
                                    if (loadMore) {
                                        hashtagNameAdapter.loadMore(hashtagSearchResponse.getData().getRows());
                                    } else {
                                        hashtagNameAdapter.setData(hashtagSearchResponse.getData().getRows());
                                    }
                                } else {
                                    Utilss.showToast(context, "there is no Hashtag", R.color.msg_fail);
                                }
                            } else {
                                Utilss.showToast(context, "there is no Hashtag", R.color.msg_fail);
                            }

                        } else {
                            Utilss.showToast(context, "there is no Hashtag", R.color.msg_fail);
                        }
                    }

                }));
    }

    public void changeFragment() {
        try {
            FragmentManager fm = fragmentManager;
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.output, fragment);
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void onBack() {
        activity.onBackPressed();
    }
}
