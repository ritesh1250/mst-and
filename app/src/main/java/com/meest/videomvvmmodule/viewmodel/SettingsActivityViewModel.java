package com.meest.videomvvmmodule.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SettingsActivityViewModel extends ViewModel {

    public CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<RestResponse> updateToken = new MutableLiveData<>();
    public MutableLiveData<RestResponse> logOut = new MutableLiveData<>();

    public void logOutUser() {
//        disposable.add(Global.initRetrofit().logOutUser(Global.apikey, Global.accessToken)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//
//                .subscribe((logoutUser, throwable) -> {
//                    if (logoutUser != null && logoutUser.getStatus() != null) {
//                        logOut.setValue(logoutUser);
//                    }
//                }));


    }

    public void updateToken(String token) {

        disposable.add(Global.initRetrofit().updateToken(Global.accessToken, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((updateToken, throwable) -> {
                    this.updateToken.setValue(updateToken);
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
