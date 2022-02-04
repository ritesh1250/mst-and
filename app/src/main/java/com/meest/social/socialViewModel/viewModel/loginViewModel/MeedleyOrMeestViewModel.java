package com.meest.social.socialViewModel.viewModel.loginViewModel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.databinding.ActivityMedleyOrMeestBinding;
import com.meest.meestbhart.register.DefaultAppResponse;
import com.meest.meestbhart.register.DefaultParam;
import com.meest.meestbhart.register.fragment.choosetopic.model.UpdateSelectedTopicsResponse;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.videomvvmmodule.utils.Global;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class MeedleyOrMeestViewModel extends ViewModel {
    ActivityMedleyOrMeestBinding binding;
    private CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public MutableLiveData<DefaultAppResponse> isdefaultApp = new MutableLiveData<>();

    public MeedleyOrMeestViewModel(ActivityMedleyOrMeestBinding binding) {
        this.binding = binding;
    }
    
    public void setDefaultApp(int value, Context context) {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(binding.getRoot().getContext(), SharedPrefreances.AUTH_TOKEN));
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
                    public void accept(DefaultAppResponse response, Throwable throwable) throws Exception {
                        if (response.getCode() == 1) {
                            SharedPrefreances.setSharedPreferenceString(context, SharedPrefreances.ID, response.getData().getId());
                            isdefaultApp.setValue(response);
                        }
                    }
                }));
    }
}
