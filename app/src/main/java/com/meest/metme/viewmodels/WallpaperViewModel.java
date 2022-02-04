package com.meest.metme.viewmodels;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.adapter.GradientWallpaperAdapter;
import com.meest.metme.adapter.WallpaperAdapter;
import com.meest.metme.model.GradientColor;
import com.meest.metme.model.WallpaperNewModel;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WallpaperViewModel extends ViewModel {

    public GradientWallpaperAdapter gradientWallpaperAdapter = new GradientWallpaperAdapter();
    public WallpaperAdapter wallpaperAdapter = new WallpaperAdapter();
    public String fontName;

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
                    gradientWallpaperAdapter.loadMore(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<GradientColor> call, Throwable t) {

            }
        });
    }

    public void getWallpaper(String x_token) {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", x_token);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<WallpaperNewModel> call = webApi.getWallpaper(header);
        call.enqueue(new Callback<WallpaperNewModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<WallpaperNewModel> call, Response<WallpaperNewModel> response) {
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    wallpaperAdapter.loadMore(response.body().getData().getRows());
                }
                else {
                    Log.e("===============","fail");
                }
            }

            @Override
            public void onFailure(Call<WallpaperNewModel> call, Throwable t) {

            }
        });
    }
}
