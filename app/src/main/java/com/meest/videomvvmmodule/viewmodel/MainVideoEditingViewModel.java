package com.meest.videomvvmmodule.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class MainVideoEditingViewModel extends ViewModel {
    public MutableLiveData<Integer> onItemClick = new MutableLiveData<>();

    public void setOnItemClick(int type) {
        onItemClick.setValue(type);
    }

}
