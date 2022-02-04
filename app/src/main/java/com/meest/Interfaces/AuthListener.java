package com.meest.Interfaces;

import androidx.lifecycle.LiveData;

import okhttp3.Response;

public interface AuthListener<T> {

    void onStarted();

    void onSuccess(Response data);

    void onFailure(String message);

}