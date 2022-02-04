package com.meest.videomvvmmodule.utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class GlobalApi {

    CompositeDisposable disposable = new CompositeDisposable();


    public void rewardUser(String rewardActionId) {
        disposable.add(Global.initRetrofit().rewardUser(Global.apikey, Global.userId, rewardActionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((reward, throwable) -> {
                    if (reward != null) {
                        Timber.d(reward.getMessage());
                    }
                }));
    }

    public void increaseView(String postId) {
        disposable.add(Global.initRetrofit().increaseView(Global.apikey, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((reward, throwable) -> {
                    if (reward != null) {
                        Timber.d(reward.getMessage());
                    }
                }));
    }
}
