package com.meest.videomvvmmodule.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.model.wallet.CoinRate;
import com.meest.videomvvmmodule.model.wallet.MyWallet;
import com.meest.videomvvmmodule.model.wallet.RewardingActions;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class WalletViewModel extends ViewModel {

    public MutableLiveData<MyWallet> myWallet = new MutableLiveData<>();
    public MutableLiveData<CoinRate> coinRate = new MutableLiveData<>();
    public MutableLiveData<RewardingActions> rewardingActions = new MutableLiveData<>();
    public List<RewardingActions.Data> rewardingActionsList = new ArrayList<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public void fetchMyWallet() {
        disposable.add(Global.initRetrofit().getMyWalletDetails(Global.apikey,Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doOnTerminate(() -> isLoading.setValue(false))
                .subscribe((wallet, throwable) -> {
                    if (wallet != null && wallet.getStatus() != null) {
                        myWallet.setValue(wallet);
                    }
                }));
    }

    public void fetchRewardingActions() {
        disposable.add(Global.initRetrofit().getRewardingAction(Global.apikey,Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new BiConsumer<RewardingActions, Throwable>() {
                    @Override
                    public void accept(RewardingActions rewardingActions, Throwable throwable) throws Exception {

                        if (rewardingActions != null && rewardingActions.getStatus() != null) {
                            rewardingActionsList = rewardingActions.getData();
                            WalletViewModel.this.rewardingActions.setValue(rewardingActions);
                        }

                    }

                }));
    }

    public void fetchCoinRate() {
        disposable.add(Global.initRetrofit().getCoinRate(Global.apikey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((coinRate, throwable) -> {
                    if (coinRate != null && coinRate.getStatus() != null) {
                        this.coinRate.setValue(coinRate);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

}
