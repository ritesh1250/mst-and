package com.meest.videomvvmmodule.viewmodel;

import android.view.View;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.databinding.NewContainMainModelBinding;

public class HomeViewModel extends ViewModel {
    public MutableLiveData<Integer> onPageSelect = new MutableLiveData<>();
    public MutableLiveData<Boolean> onStop = new MutableLiveData<>();
    public ObservableInt loadingVisibility = new ObservableInt(View.GONE);
    public MutableLiveData<Boolean> isShowLoaderInHome = new MutableLiveData<>();
    public MutableLiveData<Integer> scroll = new MutableLiveData<>();
    public MutableLiveData<Boolean> onRefresh = new MutableLiveData<>();

}
