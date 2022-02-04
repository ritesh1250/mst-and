package com.meest.Activities.VideoPost.ARGearAsset;

import com.meest.meestbhart.utilss.Constant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ARGearWebService {

    public static ARGearApi createContentsService() {
      //  HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
      //  loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .build();

        return new Retrofit.Builder()
                .baseUrl(Constant.ARGEAR_API_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ARGearApi.class);
    }

}
