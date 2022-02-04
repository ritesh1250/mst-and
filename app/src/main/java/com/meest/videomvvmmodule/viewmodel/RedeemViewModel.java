package com.meest.videomvvmmodule.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RedeemViewModel extends ViewModel {

    public String coindCount;
    public String coinRate;
    public String requestType;
    public String redeemCoin;
    public String accountId;
    public String redeemCoinPrice;
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<RestResponse> redeem = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();

    public void afterPaymentAccountChanged(CharSequence s) {
        accountId = s.toString();
    }

    public void callApiToRedeem() {
        int amount = Integer.parseInt(coindCount) * Integer.parseInt(coinRate);
        Log.e("redeemCoin", "===================" + redeemCoin);
        Log.e("redeemCoinPrice", "===================" + redeemCoinPrice);
        Log.e("requestType", "===================" + requestType);
        Log.e("accountId", "===================" + accountId);
        disposable.add(Global.initRetrofit().sendRedeemRequest(Global.apikey, redeemCoinPrice, redeemCoin, requestType, Global.userId, accountId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doOnTerminate(() -> isLoading.setValue(false))
                .subscribe((redeem, throwable) -> {
                    if (redeem != null && redeem.getStatus() != null) {
                        this.redeem.setValue(redeem);
                    }
                }));

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }


}
