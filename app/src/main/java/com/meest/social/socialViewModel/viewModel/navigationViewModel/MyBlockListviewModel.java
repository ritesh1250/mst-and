package com.meest.social.socialViewModel.viewModel.navigationViewModel;

import android.content.Context;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.responses.UserBlockListResponse;
import com.meest.videomvvmmodule.utils.Global;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class MyBlockListviewModel extends ViewModel {
    public int pagePerRecord = 10, start = 0,totalPage=0;
    public MutableLiveData<List<UserBlockListResponse.Row>> saveDataresponse = new MutableLiveData<>();
    public ObservableBoolean isEmpty = new ObservableBoolean(false);
    public ObservableBoolean noRecord = new ObservableBoolean(false);
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public ObservableBoolean isLastPage = new ObservableBoolean(false);
    private CompositeDisposable disposable = new CompositeDisposable();

    public void getDataFromServer(Context context){
        isEmpty.set(true);
        noRecord.set(false);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        disposable.add(Global.initSocialRetrofit().getBlockedUserList( map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> {
                    isLoading.set(false);
                })
                .subscribe(new BiConsumer<UserBlockListResponse, Throwable>() {
                    @Override
                    public void accept(UserBlockListResponse savedDataResponse, Throwable throwable) throws Exception {
                        if (savedDataResponse.getData()!=null && savedDataResponse.getSuccess()) {
                            isEmpty.set(false);
                            saveDataresponse.setValue(savedDataResponse.getData().getRows());
                        }else {
                            Log.e("TAG", "accept: "+"false");
                            noRecord.set(true);
                            isEmpty.set(true);
                            isLoading.set(false);
                        }
                    }
                }));
    }
}
