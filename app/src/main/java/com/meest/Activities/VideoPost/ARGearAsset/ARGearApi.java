package com.meest.Activities.VideoPost.ARGearAsset;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ARGearApi {

    @GET("/api/v3/{api_key}")
    Call<ARGearDataModel> getContents(@Path("api_key") String apiKey);

}
