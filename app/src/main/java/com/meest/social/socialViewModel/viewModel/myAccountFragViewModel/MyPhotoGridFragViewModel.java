package com.meest.social.socialViewModel.viewModel.myAccountFragViewModel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.social.socialViewModel.adapter.MyPhotoGridAdapter1;
import com.meest.social.socialViewModel.model.MediaPostResponse1;
import com.meest.videoEditing.videocollage.BitmapUtils.Utils;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MyPhotoGridFragViewModel extends ViewModel {

    private static int totalCount = -1;
    public int pageno = 1;
    public boolean Loading = true;
    public int pagePerRecord = 30;
    public ArrayList<MediaPostResponse1.Data.RowsFeed> feedArrayList = new ArrayList<>();
    public MyPhotoGridAdapter1 adapter;
    public MutableLiveData<Boolean> loaderVisible = new MutableLiveData<>();
    public MutableLiveData<Boolean> tvNoDataVisible = new MutableLiveData<>();
    public CompositeDisposable disposable = new CompositeDisposable();
    public  Context context;


    public MyPhotoGridFragViewModel(Context context) {
        this.context=context;
        adapter = new MyPhotoGridAdapter1();
    }

    public void homeFragmentFeedListingService(boolean loadMore, Context context, int pageNumber) {
        loaderVisible.setValue(true);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
        body.put("pageNumber", String.valueOf(pageNumber));
        body.put("pageSize", String.valueOf(pagePerRecord));
        disposable.add(Global.initSocialRetrofit().getUserMedia(map, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> loaderVisible.setValue(true))
                .doFinally(() -> loaderVisible.setValue(false))
                .subscribe((MediaPostResponse1 response, Throwable throwable) -> {
                    if(response.getCode()==1) {
                        if (response.getData().getRowsFeed() != null) {
                            if (response.getData().getRowsFeed().size() > 0) {
                                feedArrayList.addAll(response.getData().getRowsFeed());
                                if (!loadMore) {
                                    adapter.update(response.getData().getRowsFeed());
                                } else {
                                    adapter.loadMore(response.getData().getRowsFeed());
                                }
                            } else {
                                if (feedArrayList.size() == 0) {
                                    tvNoDataVisible.setValue(true);
                                }
                            }
                        }
                    }else{
                        Utilss.showToast(context, context.getString(R.string.no_data_available), R.color.grey);
                    }
                }));
    }

    /*public void loadMoreTrue(Context context){
        System.out.println("==PAGINATION STARTED " + FollowListFragment.pageno);
        if (Loading) {
            Loading = false;
            pageno++;
            homeFragmentFeedListingService(true, context, String.valueOf(pageno));
        }
    }*/

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
