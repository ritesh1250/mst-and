package com.meest.social.socialViewModel.viewModel.otherUserAccountViewModel;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.databinding.OtherFriendPhotoGridFragModelBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.social.socialViewModel.adapter.MyPhotoGridAdapter1;
import com.meest.social.socialViewModel.model.MediaPostResponse1;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OtherFriendPhotoGridFragViewModel extends ViewModel {
    private static final String TAG = "OtherFriendPhotoGridFra";
    public List<MediaPostResponse1.Data.RowsFeed> rowsFeedList = new ArrayList<>();
    Context context;
    FragmentActivity activity;
    public MyPhotoGridAdapter1 adapter;
    public CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<Boolean> loaderVisibility = new MutableLiveData<>();
    public MutableLiveData<Boolean> adapterNullCheck = new MutableLiveData<>();
    public static int totalCount = -1;
    public String userId;
    public int currentPage = 1;
    public List<MediaPostResponse1.Data.RowsFeed> rowsFeed;
    public ArrayList<MediaPostResponse1.Data.RowsFeed> feedArrayList = new ArrayList<>();
    public MutableLiveData<Boolean> tvNoDataVis = new MutableLiveData<>();

    public OtherFriendPhotoGridFragViewModel(Context context, FragmentActivity activity, String userId) {
        this.context = context;
        this.activity = activity;
        this.userId = userId;
        adapter = new MyPhotoGridAdapter1();
    }

    public void homeFragmentFeedListingService(boolean loadMore, int pageNumber) {
        loaderVisibility.setValue(true);
        Map<String, String> map = new HashMap<>();
        map.put("x-token",SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
        body.put("pageNumber", String.valueOf(pageNumber));
        body.put("pageSize", "27");
        body.put("userId", userId);
        disposable.add(Global.initSocialRetrofit().getUserMedia(map, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> loaderVisibility.setValue(true))
                .doFinally(() -> loaderVisibility.setValue(false))
                .subscribe((MediaPostResponse1 response, Throwable throwable) ->
                        {
                            loaderVisibility.setValue(false);
                            if (response.getData().getRowsFeed().size() > 0) {
                                feedArrayList.addAll(response.getData().getRowsFeed());
                                if (!loadMore) {
                                    adapter.update(response.getData().getRowsFeed());
                                } else {
                                    adapter.loadMore(response.getData().getRowsFeed());
                                }
                            } else {
                                if (feedArrayList.size() == 0) {
                                    tvNoDataVis.setValue(true);
                                }
                            }
                        }
                ));
    }

    public void updateAdapter(List<MediaPostResponse1.Data.RowsFeed> rowsFeed) {
        this.rowsFeed = rowsFeed;
        if (adapter == null) {
            adapterNullCheck.setValue(true);
        } else {
            adapterNullCheck.setValue(false);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
