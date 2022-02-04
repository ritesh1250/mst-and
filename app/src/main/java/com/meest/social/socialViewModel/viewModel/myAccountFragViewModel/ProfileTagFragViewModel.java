package com.meest.social.socialViewModel.viewModel.myAccountFragViewModel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.social.socialViewModel.adapter.ProfileTagAdapter1;
import com.meest.social.socialViewModel.model.ProfilesVideoResponse1;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileTagFragViewModel extends ViewModel {
    public ProfileTagAdapter1 adapter;
    public boolean Loading = true;
    public int pagePerRecord = 30;
    public int pageno = 1;
    public int totalCount = -1;
    public ArrayList<ProfilesVideoResponse1.Row> arrayList = new ArrayList<>();
    public CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<Boolean> loaderVisible = new MutableLiveData<>();
    public MutableLiveData<Boolean> tvNoDataVisiblity = new MutableLiveData<>();

    public ProfileTagFragViewModel() {
        arrayList.clear();
        adapter = new ProfileTagAdapter1();
    }

    public void homeFragmentFeedListingService(boolean loadMore, Context context, int pageNumber){
        HashMap<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
        body.put("pageNumber", String.valueOf(pageNumber));
        body.put("pageSize", String.valueOf(pagePerRecord));
        disposable.add(Global.initSocialRetrofit().tagUserMedia(map, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> loaderVisible.setValue(true))
                .doOnTerminate(() -> loaderVisible.setValue(false))
                .subscribe((ProfilesVideoResponse1 response, Throwable throwable) -> {
                    if (response.getCode() == 1) {
                        totalCount = response.getData().getCount();
                        if (response.getData().getRows().size() > 0) {
                            if(totalCount!=arrayList.size()){
                                Log.e("ProfilesVideoResponse1","list");
                                Loading = true;
                            }
                            arrayList.addAll(response.getData().getRows());
                            if(!loadMore){
                                adapter.update(response.getData().getRows());
                            }else {
                                adapter.loadMore(response.getData().getRows());
                            }
                        }else{
                            if(arrayList.size()==0){
                                tvNoDataVisiblity.setValue(true);
                            }
                        }
                    } else {
                        Utilss.showToast(context, context.getString(R.string.SOME_ERROR), R.color.grey);
                    }
                }));

    }

    public void checkNetAndFetchData(Context context) {
        if (ConnectionUtils.isConnected(context)){
            if(Loading){
                homeFragmentFeedListingService(false, context, pageno);
            }

        } else
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    }
}
