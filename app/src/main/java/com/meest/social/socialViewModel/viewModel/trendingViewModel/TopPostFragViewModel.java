package com.meest.social.socialViewModel.viewModel.trendingViewModel;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.meest.Fragments.FollowListFragment;
import com.meest.R;
import com.meest.databinding.TopPostFragModelBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.social.socialViewModel.adapter.HashtagNameAdapter;
import com.meest.social.socialViewModel.adapter.trendingAdapters.TempTopTrendingAdapter;
import com.meest.social.socialViewModel.adapter.trendingAdapters.TopTrendingAdapter;
import com.meest.social.socialViewModel.model.HashtagSearchResponse;
import com.meest.social.socialViewModel.model.TopResponse;
import com.meest.utils.PaginationListener;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TopPostFragViewModel extends ViewModel {
    private static final String TAG = "TopPostFragViewModel";
    public Context context;
    public Activity activity;
    public TopPostFragModelBinding binding;
    public String data;
    private CompositeDisposable disposable = new CompositeDisposable();
    public TopResponse searchResponse;
    public List<TopResponse.Datum> list = new ArrayList();
    public List<TopResponse.Datum> tempList = new ArrayList();
    public boolean isSvs;
    public TopTrendingAdapter videoPeopleAdapter;
    public TempTopTrendingAdapter tempTopTrendingAdapter;
    public TopTrendingAdapter peopleSearchAdapter;
    public static List<HashtagSearchResponse.Datum> datahashTagList;
    public HashtagNameAdapter hashtagNameAdapter;
    public int pagePerRecord = 10;
    public int pageno = 1;
    public boolean Loading = true;

    public MutableLiveData<Boolean> loader = new MutableLiveData<>();

    public TopPostFragViewModel(Context context, FragmentActivity activity, TopPostFragModelBinding binding, boolean isSvs) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;
        this.isSvs = isSvs;

        tempTopTrendingAdapter = new TempTopTrendingAdapter(context);
    }

    public void fetchData(boolean isLoadMore, int pageno) {

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        HashMap<String, Object> body = new HashMap<>();
        body.put("pageNumber", pageno);
        body.put("pageSize", 20);
        disposable.add(Global.initSocialRetrofit().hashTagTop(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((topResponse, throwable) -> {
                    if (topResponse != null && topResponse.getSuccess()) {
                        searchResponse = topResponse;
                        if (isLoadMore)
                            tempTopTrendingAdapter.loadMore(topResponse.getData());
                        else
                            tempTopTrendingAdapter.setData(topResponse.getData());
                        bindData();
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void bindData() {

        if (searchResponse == null || searchResponse.getData() == null || searchResponse.getData().size() == 0) {
            binding.notFound.setVisibility(View.VISIBLE);
            binding.peopleRecycleView.setVisibility(View.GONE);
            return;
        }
        binding.notFound.setVisibility(View.GONE);
        binding.peopleRecycleView.setVisibility(View.VISIBLE);

    }

    public void inits() {
        datahashTagList = new ArrayList<>();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        binding.peopleRecycleView.setLayoutManager(layoutManager);
        binding.peopleRecycleView.setHasFixedSize(true);
        fetchData(false, pageno);
    }

    public void paginationOnFeedFollow() {
        try {
            FollowListFragment.recyclerView.addOnScrollListener(new com.meest.utils.PaginationListener((LinearLayoutManager) FollowListFragment.recyclerView.getLayoutManager(), PaginationListener.PAGE_SIZE_10) {
                @Override
                public void loadMoreItems() {
                    System.out.println("==PAGINATION STARTED " + FollowListFragment.pageno);
                    if (Loading) {
                        Loading = false;
                        pageno = pageno + 1;
                        fetchData(false, pageno);
                    }
                }

                @Override
                public boolean isLastPage() {
                    return false;
                }

                @Override
                public boolean isLoading() {
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void updateSearch(String search) {
        this.data = search;
        if (data != null) {
            fetchData(false, pageno);
        }
    }
}
