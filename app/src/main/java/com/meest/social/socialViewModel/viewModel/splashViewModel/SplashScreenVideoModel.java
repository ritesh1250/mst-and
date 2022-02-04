package com.meest.social.socialViewModel.viewModel.splashViewModel;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.meest.databinding.SplashVideoScreenActivityBinding;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SplashScreenVideoModel extends ViewModel {
    Context context;
    Activity activity;
    SplashVideoScreenActivityBinding binding;
    private CompositeDisposable disposable = new CompositeDisposable();
    Uri video;
    SessionManager sessionManager;

    public SplashScreenVideoModel(Context context, Activity activity, SplashVideoScreenActivityBinding binding) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;
        sessionManager = new SessionManager(activity);
    }

    public void getLanguage() {
        disposable.add(Global.initRetrofit().getLanguage(Global.apikey, Global.userId, "hindi")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((languageResponseMedley, throwable) -> {
                    if (languageResponseMedley != null) {
                        Global.initLanguage(languageResponseMedley.getData());
                    }
                }));
    }


}
