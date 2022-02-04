package com.meest.Insights;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Paramaters.InsightsParameter;
import com.meest.R;
import com.meest.responses.UserInsightsResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsightsActivity extends AppCompatActivity {

    BottomSheetBehavior bottomSheetBehavior;
    LottieAnimationView image;
    private TextView likesCountTV, commentCountTV, shareCountTV, saveCountTV, totalFollowerCountTV, followingPercentTV, nameTV;

    private void initViews() {
        image = findViewById(R.id.loading);
        likesCountTV = findViewById(R.id.likesCountTV);
        commentCountTV = findViewById(R.id.commentCountTV);
        shareCountTV = findViewById(R.id.shareCountTV);
        saveCountTV = findViewById(R.id.saveCountTV);
        totalFollowerCountTV = findViewById(R.id.totalFollowerCountTV);
        nameTV = findViewById(R.id.nameTV);
        followingPercentTV = findViewById(R.id.followingPercentTV);
        image.setAnimation("loading.json");
        image.playAnimation();
        image.loop(true);
        nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(res.getData().getRows()==null ||res.getData().getRows().size()==0){

                }else{
                    startActivity(new Intent(InsightsActivity.this, InsightsTabActivity.class)
                            .putExtra("data", (Serializable) res));
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);
        LinearLayout linearLayout = findViewById(R.id.linearLay);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        initViews();
        getUserPosts();
    }

    private void showHideLoader(boolean showLoader) {
        if (showLoader) {
            if (image.getVisibility() != View.VISIBLE) {
                image.setVisibility(View.VISIBLE);
            }
        } else {
            image.setVisibility(View.GONE);
        }
    }

    private void setData(List<UserInsightsResponse.Row> data) {
        try {
            if(data==null || data.size()==0)
            {
                return;
            }
            likesCountTV.setText(data.get(0).getLikeCounts() + "");
            commentCountTV.setText(data.get(0).getCommentCounts() + "");
            shareCountTV.setText(data.get(0).getShareCount() + "");
            saveCountTV.setText(data.get(0).getPostSaveCounts() + "");
            totalFollowerCountTV.setText(data.get(0).getFollowersCount() + "");
            nameTV.setText(data.get(0).getUser().getUsername());
            followingPercentTV.setText(data.get(0).getFollowrPercent() + "% " + getResources().getString(R.string.following));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    UserInsightsResponse res;
    private void getUserPosts() {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        InsightsParameter parameter = new InsightsParameter(SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
        Call<UserInsightsResponse> call = webApi.getInsights(header, parameter);
        showHideLoader(true);
        call.enqueue(new Callback<UserInsightsResponse>() {
            @Override
            public void onResponse(Call<UserInsightsResponse> call, Response<UserInsightsResponse> response) {
                showHideLoader(false);
                if (response.code() == 200) {
                      res = response.body();
                    setData(res.getData().getRows());
                } else {
                }
            }

            @Override
            public void onFailure(Call<UserInsightsResponse> call, Throwable t) {
                showHideLoader(false);
            }
        });
    }

}