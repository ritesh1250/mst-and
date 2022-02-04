package com.meest.metme.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.databinding.ActivityTextAppearanceBinding;
import com.meest.metme.TextAppearanceActivity;
import com.meest.metme.adapter.FontAdapter;
import com.meest.metme.adapter.GradientColorAdapter;
import com.meest.metme.model.FontResponse;
import com.meest.metme.model.GradientColor;
import com.meest.metme.model.SaveTextAppearance;
import com.meest.metme.model.SolidColor;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.adapter.SolidColorAdapter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TextAppearanceViewModel extends ViewModel {
    public SolidColorAdapter solidColorAdapter = new SolidColorAdapter();
    public GradientColorAdapter gradientColorAdapter = new GradientColorAdapter();
    public Context context;
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public String fontName;
    public String secondColor;

    public FontAdapter fontAdapter = new FontAdapter();
    public void getSolidColor(String x_token) {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", x_token);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<SolidColor> call = webApi.getSolidColor(header);
        call.enqueue(new Callback<SolidColor>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<SolidColor> call, Response<SolidColor> response) {
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                    solidColor.setValue(response.body());
                    solidColorAdapter.loadMore(response.body().getData().getList());
                }
            }

            @Override
            public void onFailure(Call<SolidColor> call, Throwable t) {

            }
        });
    }

    public void getGradientColor(String x_token) {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", x_token);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<GradientColor> call = webApi.getGradientColor(header);
        call.enqueue(new Callback<GradientColor>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<GradientColor> call, Response<GradientColor> response) {
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    gradientColorAdapter.loadMore(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<GradientColor> call, Throwable t) {

            }
        });
    }

    public void getFont(String x_token) {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", x_token);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FontResponse> call = webApi.getFont(header);
        call.enqueue(new Callback<FontResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<FontResponse> call, Response<FontResponse> response) {
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    fontAdapter.loadMore(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<FontResponse> call, Throwable t) {

            }
        });
    }

    public void saveTextAppearance(SaveTextAppearance model, String x_token, TextAppearanceActivity context1) {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", x_token);
        HashMap<String, String> body = new HashMap<>();
        body.put("firstColor", model.getFirstColor());
        body.put("secondColor", model.getSecondColor());
        body.put("bgColor", model.getBgColor());
        body.put("gradient", model.getGradient());
        body.put("fontName", model.getFontUrl());
        body.put("fontId", model.getFontId());
        body.put("toUserId", model.getToUserId());
        body.put("wallPaper", model.getWallpaper());
        body.put("bgFirstColor", model.getBgFirstColor());
        body.put("bgSecondColor", model.getBgSecondColor());
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<JSONObject> call = webApi.saveTextAppearance(header, body);
        call.enqueue(new Callback<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.code() == 200) {
                    isloading.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });
    }

    public void Temp(ActivityTextAppearanceBinding binding) {

    }
}
