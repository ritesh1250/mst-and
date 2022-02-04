package com.meest.social.socialViewModel.viewModel.navigationViewModel;

import android.content.Context;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.meest.responses.UserActivityResponse;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.social.socialViewModel.adapter.UserActivityAdapterviewModel;
import com.meest.videomvvmmodule.utils.Global;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;


public class MyOptionViewModel extends ViewModel {

    public MutableLiveData<String> toast = new MutableLiveData<>();
    public String userId;
    public int count = 40, start = 0,totalPage=0;
//    List<UserActivityResponse.Row> userResponseList ;
    public MutableLiveData<Boolean> onLoadMoreComplete = new MutableLiveData<>();
    public MutableLiveData<List<UserActivityResponse.Row>> userResponseList = new MutableLiveData<List<UserActivityResponse.Row>>();
    public UserActivityAdapterviewModel adapter=new UserActivityAdapterviewModel();
    public ObservableBoolean isEmpty = new ObservableBoolean(false);
    public ObservableBoolean noRecord = new ObservableBoolean(false);
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public ObservableBoolean isLastPage = new ObservableBoolean(false);
    private CompositeDisposable disposable = new CompositeDisposable();
    public boolean Loading = true;


    public void getDataFromServer(boolean loadMore,String pageNumber,Context context){
        isEmpty.set(true);
        noRecord.set(false);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        HashMap<String, Object> body = new HashMap<>();
        body.put("pageNumber", pageNumber);
        body.put("pageSize", String.valueOf(count));
        disposable.add(Global.initSocialRetrofit().getActivityHistory( map,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    isLoading.set(false);
                })
                .subscribe(new BiConsumer<UserActivityResponse, Throwable>() {
                    @Override
                    public void accept(UserActivityResponse userActivityResponse, Throwable throwable) throws Exception {
                        if (userActivityResponse.getData()!=null && userActivityResponse.getSuccess()) {
                            isEmpty.set(false);
                            userResponseList.setValue(userActivityResponse.getData().getRows());
                            totalPage=userActivityResponse.getData().getRows().size();
                            if(!loadMore){
                                adapter.update(userActivityResponse.getData().getRows());
                            }else {
                                adapter.loadMore(userActivityResponse.getData().getRows());
                            }
//                            adapter.addLoading(userActivityResponse.getData().getRows());
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
