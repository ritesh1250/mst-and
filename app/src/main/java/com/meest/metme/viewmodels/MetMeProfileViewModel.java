package com.meest.metme.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.social.socialViewModel.model.UserProfileRespone1;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MetMeProfileViewModel extends ViewModel {
    public Context context;
    public MutableLiveData<UserProfileRespone1> userProfileRespone = new MutableLiveData<>();
//    public MetMeProfileViewModel(MetMeProfile context, String otherUserId) {
//        this.context=context;
//        this.otherUserId=otherUserId;
//    }

    public void fetchMetMeProfile(String otherUserId, String x_token) {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", x_token);

        HashMap<String, String> body = new HashMap<>();
        body.put("userId", otherUserId);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<UserProfileRespone1> call = webApi.userProfile(header, body);
        call.enqueue(new Callback<UserProfileRespone1>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<UserProfileRespone1> call, Response<UserProfileRespone1> response) {
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    userProfileRespone.setValue(response.body());
                    Log.e("TAG", "metme_profile: " + response.body().getDataUser().getFirstName());
                }
            }

            @Override
            public void onFailure(Call<UserProfileRespone1> call, Throwable t) {

            }
        });


    }
}
