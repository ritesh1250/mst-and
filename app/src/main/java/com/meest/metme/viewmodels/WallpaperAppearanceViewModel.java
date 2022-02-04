package com.meest.metme.viewmodels;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.WallpaperAppearance;
import com.meest.metme.model.SaveTextAppearance;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WallpaperAppearanceViewModel {
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    public String fontName;
    public void saveWallpaper(SaveTextAppearance model, String x_token, WallpaperAppearance context1) {
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
                if (response.code() == 200 ) {
                    isloading.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });
    }
}
