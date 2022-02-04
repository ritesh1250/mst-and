package com.meest.social.socialViewModel.viewModel.navigationViewModel.mainViewModel;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.messaging.FirebaseMessaging;
import com.meest.R;
import com.meest.databinding.NewContainMainModelBinding;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.register.DefaultAppResponse;
import com.meest.meestbhart.register.DefaultParam;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.social.socialViewModel.view.home.main.HomeActivity;
import com.meest.videomvvmmodule.utils.Global;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    private CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public NewContainMainModelBinding newContainMainModelBinding;

    public MainActivityViewModel(NewContainMainModelBinding newContainMainModelBinding) {
        this.newContainMainModelBinding = newContainMainModelBinding;
    }

    public void setDefault(int value, HomeActivity homeActivity) {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.AUTH_TOKEN));

        DefaultParam defaultParam = new DefaultParam(value);
        disposable.add(Global.initSocialRetrofit().defaultApp(map, defaultParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    isloading.setValue(false);
                })
                .subscribe(new BiConsumer<DefaultAppResponse, Throwable>() {
                    @Override
                    public void accept(DefaultAppResponse defaultAppResponse, Throwable throwable) throws Exception {
                        if (defaultAppResponse.getCode() == 1) {
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.WHEREFROM, "Video");
                            toast.setValue(newContainMainModelBinding.getRoot().getResources().getString(R.string.Default_App_changed_to_Medley));
                            SharedPrefreances.setSharedPreferenceString(homeActivity, SharedPrefreances.ID, "1");
                        } else {
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.WHEREFROM, "Social");
                            toast.setValue(newContainMainModelBinding.getRoot().getResources().getString(R.string.Default_App_changed_to_Meest));
                            SharedPrefreances.setSharedPreferenceString(homeActivity, SharedPrefreances.ID, "0");
                        }
                    }
                }));
    }

    public void updatelanguage(String lang_eng) {
        if (lang_eng.equals("en")) {
            setAppLocale(lang_eng);
        } else if (lang_eng.equals("hi")) {
            setAppLocale(lang_eng);
        } else if (lang_eng.equals("pa")) {
            setAppLocale(lang_eng);
        } else if (lang_eng.equals("bn")) {
            setAppLocale(lang_eng);
        } else if (lang_eng.equals("ur")) {
            setAppLocale(lang_eng);
        } else if (lang_eng.equals("ma")) {
            setAppLocale(lang_eng);
        } else if (lang_eng.equals("or")) {
            setAppLocale(lang_eng);
        } else if (lang_eng.equals("as")) {
            setAppLocale(lang_eng);
        } else if (lang_eng.equals("te")) {
            setAppLocale(lang_eng);
        } else if (lang_eng.equals("ta")) {
            setAppLocale(lang_eng);
        } else {
            Log.e("==========", "Not Language");
        }

    }


    public void setAppLocale(String localeCode) {
        Resources resources = newContainMainModelBinding.getRoot().getContext().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(localeCode.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public void logoutNow() {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
        body.put("isOnline", "false");
        body.put("fcmToken", "");
        disposable.add(Global.initSocialRetrofit().update_profile(map, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    isloading.setValue(false);
                })
                .subscribe(new BiConsumer<ApiResponse, Throwable>() {
                    @Override
                    public void accept(ApiResponse response, Throwable throwable) throws Exception {
                        if (response.getCode() == 1) {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(SharedPrefreances.getSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.ID));
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.ID, "");
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.PROFILE, "");
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), "login", "0");
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), "Profile", "");
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), "token", "");
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.PROFILE_IMAGE, "");

                        }

                    }
                }));
    }

    public void deleteAccount() {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.AUTH_TOKEN));
        HashMap<String, Object> body = new HashMap<>();
        body.put("id", SharedPrefreances.getSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.ID));
        body.put("isDeleted", true);
        disposable.add(Global.initSocialRetrofit().delUserAccount(map, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    isloading.setValue(false);
                })
                .subscribe(new BiConsumer<ApiResponse, Throwable>() {
                    @Override
                    public void accept(ApiResponse response, Throwable throwable) throws Exception {
                        if (response.getCode() == 1) {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(SharedPrefreances.getSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.ID));
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.ID, "");
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.PROFILE, "");
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), "login", "0");
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), "Profile", "");
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), "token", "");
                            SharedPrefreances.setSharedPreferenceString(newContainMainModelBinding.getRoot().getContext(), SharedPrefreances.PROFILE_IMAGE, "");
                        }
                    }
                }));
    }


}
