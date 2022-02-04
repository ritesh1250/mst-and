package com.meest.social.socialViewModel.viewModel.navigationViewModel;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.Paramaters.UserSettingParams;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.responses.UserSettingUpdateResponse;
import com.meest.responses.UserSettingsResponse;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.videomvvmmodule.utils.Global;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class MySettingviewModel extends ViewModel {
    public MutableLiveData<UserSettingsResponse.User> bindUserDataUser = new MutableLiveData<>();
    public MutableLiveData<UserSettingUpdateResponse.Data> updateUser = new MutableLiveData<>();
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    private CompositeDisposable disposable = new CompositeDisposable();





    public void uploadDataToServer(UserSettingParams mObject,Context context) {
        try {
            final WebApi webApi = ApiUtils.getClientHeader(context);
            Map<String, String> map = new HashMap<>();
            map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

            disposable.add(Global.initSocialRetrofit().updateUserSettings( map,mObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable1 -> isLoading.set(true))
                    .doOnTerminate(() -> {
                        isLoading.set(false);
                    })
                    .subscribe(new BiConsumer<UserSettingUpdateResponse, Throwable>() {
                        @Override
                        public void accept(UserSettingUpdateResponse savedDataResponse, Throwable throwable) throws Exception {
                            if (savedDataResponse.getData()!=null && savedDataResponse.getSuccess()) {
                               updateUser.setValue(savedDataResponse.getData());
                            }else {
                                isLoading.set(false);
                            }
                        }
                    }));
        } catch (Exception e) {
            isLoading.set(false);
            e.printStackTrace();
        }
    }

    public void getDataFromServer(Context context) {
        try {
            final WebApi webApi = ApiUtils.getClientHeader(context);
            Map<String, String> map = new HashMap<>();
            map.put("x-token",SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
            HashMap<String, Object> body = new HashMap<>();
            body.put("userId", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID));
            disposable.add(Global.initSocialRetrofit().getUserSettings( map,body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable1 -> isLoading.set(true))
                    .doOnTerminate(() -> {
                        isLoading.set(false);
                    })
                    .subscribe(new BiConsumer<UserSettingsResponse, Throwable>() {
                        @Override
                        public void accept(UserSettingsResponse savedDataResponse, Throwable throwable) throws Exception {
                            if (savedDataResponse.getData()!=null && savedDataResponse.getSuccess()) {
                                bindUserDataUser.setValue(savedDataResponse.getData().getUser());
                            }else {
                                isLoading.set(false);
                            }
                        }
                    }));
        } catch (Exception e) {
            isLoading.set(false);
            e.printStackTrace();
        }
    }


    public void setTheme(String name, Context context) {
        // Create preference to store theme name
        SharedPreferences preferences = context.getSharedPreferences("Theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ThemeName", name);
        editor.apply();

    }


}
