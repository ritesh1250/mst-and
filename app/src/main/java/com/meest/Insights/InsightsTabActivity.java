package com.meest.Insights;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Paramaters.InsightsParameter;
import com.meest.R;
import com.meest.responses.UserInsightsResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InsightsTabActivity extends AppCompatActivity {

    TabLayout tabLayoutInsights;
    ViewPager viewPagerInsights;
    ImageView img_back_tab_insights;
    UserInsightsResponse insightResponse;
    ImageView profileImage;
    LottieAnimationView image;

    TextView nameTV;

    void setHeader() {
        try {
            nameTV.setText(insightResponse.getData().getRows().get(0).getUser().getUsername());
            Glide.with(this).load(insightResponse.getData().getRows().get(0).getUser().getDisplayPicture())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .error(R.drawable.placeholder)).into(profileImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        image = findViewById(R.id.loading);
        profileImage = findViewById(R.id.placeholder);
        nameTV = findViewById(R.id.nameTV);
        img_back_tab_insights = findViewById(R.id.img_back_tab_insights);
        img_back_tab_insights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        image.setAnimation(getString(R.string.loading_json));
        image.playAnimation();
        image.loop(true);
        tabLayoutInsights = findViewById(R.id.tabLayoutInsights);
        viewPagerInsights = findViewById(R.id.viewPagerInsights);
    }

    private void setTabs() {
        try {
            tabLayoutInsights.addTab(tabLayoutInsights.newTab().setText(getString(R.string.content)));
            tabLayoutInsights.addTab(tabLayoutInsights.newTab().setText(getString(R.string.Activity)));
            tabLayoutInsights.setTabGravity(TabLayout.GRAVITY_FILL);
            final InsightsAdapter adapter = new InsightsAdapter(this, getSupportFragmentManager(),
                    tabLayoutInsights.getTabCount(), insightResponse);
            viewPagerInsights.setAdapter(adapter);
            viewPagerInsights.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutInsights));
            tabLayoutInsights.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPagerInsights.setCurrentItem(tab.getPosition());

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights_tab);
        initViews();
        try {
            if (getIntent().hasExtra("id")) {
                userID = getIntent().getStringExtra("id");
            } else {
                userID = SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            userID = SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.ID);
        }
        getUserPosts();
    }

    private void getUserPosts() {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        InsightsParameter parameter = new InsightsParameter(userID);
        Call<UserInsightsResponse> call = webApi.getInsights(header, parameter);
        showHideLoader(true);
        call.enqueue(new Callback<UserInsightsResponse>() {
            @Override
            public void onResponse(Call<UserInsightsResponse> call, Response<UserInsightsResponse> response) {
                showHideLoader(false);
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    insightResponse = response.body();
                    setHeader();
                    setTabs();
                } else {
                    Utilss.showToast(InsightsTabActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<UserInsightsResponse> call, Throwable t) {
                showHideLoader(false);
                Utilss.showToast(InsightsTabActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }
}