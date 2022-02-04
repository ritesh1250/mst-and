package com.meest.Activities;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.ViewPagerAdapterForAd;
import com.meest.Fragments.CampaignViewGenderTabFragment;
import com.meest.Fragments.CampaignViewLifetimeFragment;
import com.meest.model.AdMediaData;
import com.meest.R;
import com.meest.responses.CampaignViewResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CampaignViewActivity extends AppCompatActivity {


    private boolean isAdvertise;
    ImageView img_back_Campaign;
    LottieAnimationView loadingProgress;
    FrameLayout frameTabGender, frameTabLifetime;
    //    TabLayout tabLayout,tab_lay;
//    ViewPager viewPager,viewP;
    CampaignViewResponse campaignViewResponse;
    TextView tv_campaign, tv_none, tv_startDate, tv_endDate, tv_rate, tv_ad_delivery, tv_men_women, tvAge,
            tv_place, txt_feeds, txt_id_num, txt_completed;
    ViewPager viewPager;
    ImageView img_cancer_aware;
    String campaignId;
    TextView tvTabLifetime, tvTabWeekDay, tvTabOneDay, tvTabGender, tvTabAge, tvTabLocation;
    ImageView imvCampProfile;
    TextView tvTextDetails, tvCampaignTitle, tvCampaignDate;

    Switch chip4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_view_layout);

        loadingProgress = findViewById(R.id.loading);
        loadingProgress.setAnimation("loading.json");
        loadingProgress.playAnimation();
        loadingProgress.loop(true);
        loadingProgress.setVisibility(View.VISIBLE);

        if (getIntent().getExtras() == null) {
            Utilss.showToast(getApplicationContext(), getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            return;
        }

        campaignId = getIntent().getExtras().getString("id", "");

        tvTabLifetime = findViewById(R.id.tvTabLifetime);
        tvTabWeekDay = findViewById(R.id.tvTabWeekDay);
        tvTabOneDay = findViewById(R.id.tvTabOneDay);
        tvTabGender = findViewById(R.id.tvTabGender);
        tvTabAge = findViewById(R.id.tvTabAge);
        tvTabLocation = findViewById(R.id.tvTabLocation);
        imvCampProfile = findViewById(R.id.imvCampProfile);
        tvCampaignTitle = findViewById(R.id.tvCampaignTitle);
        tvCampaignDate = findViewById(R.id.tvCampaignDate);


        viewPager = findViewById(R.id.view_pager);

        tvTabLifetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabLifetime.setTextColor(getResources().getColor(R.color.blue));
                tvTabWeekDay.setTextColor(getResources().getColor(R.color.gray));
                tvTabOneDay.setTextColor(getResources().getColor(R.color.gray));

                pushFragment(new CampaignViewLifetimeFragment(campaignId, CampaignViewActivity.this), "");
            }
        });

        tvTabWeekDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabLifetime.setTextColor(getResources().getColor(R.color.gray));
                tvTabWeekDay.setTextColor(getResources().getColor(R.color.blue));
                tvTabOneDay.setTextColor(getResources().getColor(R.color.gray));
                pushFragment(new CampaignViewLifetimeFragment(campaignId, CampaignViewActivity.this), "");
            }
        });
        tvTabOneDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabLifetime.setTextColor(getResources().getColor(R.color.gray));
                tvTabWeekDay.setTextColor(getResources().getColor(R.color.gray));
                tvTabOneDay.setTextColor(getResources().getColor(R.color.blue));
                pushFragment(new CampaignViewLifetimeFragment(campaignId, CampaignViewActivity.this), "");
            }
        });

        tvTabGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabGender.setTextColor(getResources().getColor(R.color.blue));
                tvTabAge.setTextColor(getResources().getColor(R.color.gray));
                tvTabLocation.setTextColor(getResources().getColor(R.color.gray));
                pushFragmentGender(new CampaignViewGenderTabFragment(campaignId, getApplicationContext(), "Gender"), "");
            }
        });

        tvTabAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabGender.setTextColor(getResources().getColor(R.color.gray));
                tvTabAge.setTextColor(getResources().getColor(R.color.blue));
                tvTabLocation.setTextColor(getResources().getColor(R.color.gray));
                pushFragmentGender(new CampaignViewGenderTabFragment(campaignId, CampaignViewActivity.this, "Age"), "");
            }
        });

        tvTabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTabGender.setTextColor(getResources().getColor(R.color.gray));
                tvTabAge.setTextColor(getResources().getColor(R.color.gray));
                tvTabLocation.setTextColor(getResources().getColor(R.color.blue));
                pushFragmentGender(new CampaignViewGenderTabFragment(campaignId, CampaignViewActivity.this, "Location"), "");
            }
        });

        pushFragmentGender(new CampaignViewGenderTabFragment(campaignId, CampaignViewActivity.this, "Gender"), "");
        pushFragment(new CampaignViewLifetimeFragment(campaignId, CampaignViewActivity.this), "");

        frameTabGender = findViewById(R.id.frameTabGender);
        frameTabLifetime = findViewById(R.id.frameTabLifetime);
        img_back_Campaign = findViewById(R.id.img_back_Campaign);
        chip4 = findViewById(R.id.chip4);
        tv_campaign = findViewById(R.id.tv_campaign);

        tv_none = findViewById(R.id.tv_none);
        tv_startDate = findViewById(R.id.tv_startDate);
        tv_endDate = findViewById(R.id.tv_EndDate);
        tv_rate = findViewById(R.id.tv_rate);
        tv_ad_delivery = findViewById(R.id.tv_ad_delivery);
        tv_men_women = findViewById(R.id.tv_men_women);
        tvAge = findViewById(R.id.tvAge);
        tv_place = findViewById(R.id.tv_place);
        txt_id_num = findViewById(R.id.txt_id_num);
        txt_feeds = findViewById(R.id.txt_feeds);
        txt_completed = findViewById(R.id.txt_completed);

        img_back_Campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fetchData();

//        tab_lay=findViewById(R.id.tabLay);
//        viewP=findViewById(R.id.viewP);
//
//        tab_lay.addTab( tab_lay.newTab().setText("Gender"));
//        tab_lay.addTab( tab_lay.newTab().setText("Age"));
//        tab_lay.addTab( tab_lay.newTab().setText("Location"));
//
//        tab_lay.setTabGravity(TabLayout.GRAVITY_FILL);
//        final TabAdapter adapter_tab = new TabAdapter(this, getSupportFragmentManager(),
//                tab_lay.getTabCount());
//        viewP.setAdapter(adapter_tab);
//        viewP.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener( tab_lay));
//
//        tab_lay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                viewP.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });

        /////////////////////////////////

//        tabLayout = findViewById(R.id.tabLayout);
//        viewPager = findViewById(R.id.viewPager);
//        tabLayout.addTab(tabLayout.newTab().setText("Lifetime"));
//        tabLayout.addTab(tabLayout.newTab().setText("7 days"));
//        tabLayout.addTab(tabLayout.newTab().setText("1 day"));
//
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        final MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(),
//                tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
    }

    private void fetchData() {

        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        // body
        HashMap<String, Object> body = new HashMap<>();
        body.put("campaignId", campaignId);
        body.put("isAdvertise", true);

        Log.v("harsh", "body == " + new Gson().toJson(body));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<CampaignViewResponse> call = webApi.getCampView(header, body);

        call.enqueue(new Callback<CampaignViewResponse>() {
            @Override
            public void onResponse(Call<CampaignViewResponse> call, Response<CampaignViewResponse> response) {

                if (response.code() == 200 && response.body().getSuccess()) {
                    loadingProgress.setVisibility(View.GONE);
                    campaignViewResponse = response.body();
                    bindData();
                } else {
                    loadingProgress.setVisibility(View.GONE);
                    Utilss.showToast(CampaignViewActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<CampaignViewResponse> call, Throwable t) {
                loadingProgress.setVisibility(View.GONE);
                Utilss.showToast(CampaignViewActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });

    }

    private void bindData() {
//
//        List<AdMediaData> mediaList = new ArrayList<>();
//        AdMediaData data = new AdMediaData();
////        data.setButtonType(campaignViewResponse.getData()());
//        data.setFileUrl(campaignViewResponse.getData().getFileURL());
//        data.setHeading(campaignViewResponse.getData().getCampaignTitle());
//        data.setSubHeading(campaignViewResponse.getData().getCampaignText());
//        data.setWebsiteUrl(campaignViewResponse.getData().getWebsiteUrl());
//        mediaList.add(data);


        List<AdMediaData> mediaList = new ArrayList<>();

        for (int i = 0; i < campaignViewResponse.getData().getOtherImageVideos().size(); i++) {
            AdMediaData adMediaData = new AdMediaData();
            adMediaData.setFileUrl(campaignViewResponse.getData().getOtherImageVideos().get(i).getFileUrl());
//        adMediaData.setButtonType(campaignViewResponse.getData().getCallToAction());
            adMediaData.setSubHeading(campaignViewResponse.getData().getOtherImageVideos().get(i).getSubHeading());
            adMediaData.setHeading(campaignViewResponse.getData().getOtherImageVideos().get(i).getHeading());
            adMediaData.setFileType(campaignViewResponse.getData().getOtherImageVideos().get(i).getFileType());
            adMediaData.setWebsiteUrl(campaignViewResponse.getData().getOtherImageVideos().get(i).getWebsiteUrl());
            mediaList.add(adMediaData);
        }
//        mediaList.addAll(campaignViewResponse.getData().getOtherImageVideos());

        ViewPagerAdapterForAd viewPagerAdapter = new ViewPagerAdapterForAd(CampaignViewActivity.this, mediaList, campaignViewResponse.getData().getStartDate() + " " + campaignViewResponse.getData().getEndDate(), campaignViewResponse.getData().getCampaignType());
        viewPager.setAdapter(viewPagerAdapter);

        tvCampaignDate.setText("Created at " + campaignViewResponse.getData().getStartDate().substring(0, 10));
        tvCampaignTitle.setText(SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.USERNAME));
        if (!SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.PROFILE).isEmpty()) {
            Glide.with(this).load(SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.PROFILE)).into(imvCampProfile);
        }

        tv_campaign.setText(campaignViewResponse.getData().getCampaignTitle());
        tv_startDate.setText(campaignViewResponse.getData().getStartDate().substring(0, 10));
        tv_endDate.setText(campaignViewResponse.getData().getEndDate().substring(0, 10));
        tv_rate.setText("Rs. " + campaignViewResponse.getData().getTotalAmmount() + "/- INR");
        tv_ad_delivery.setText(campaignViewResponse.getData().getCampaignText());
        txt_completed.setText(campaignViewResponse.getData().getCampaignStatus());
        txt_id_num.setText(campaignViewResponse.getData().getId());

        tv_none.setText(campaignViewResponse.getData().getCampaignType());
        StringBuilder gendar = new StringBuilder();
        for (String i : campaignViewResponse.getData().getGender()) {
            gendar.append(i);
        }
        tv_men_women.setText(gendar.toString());
        tvAge.setText(campaignViewResponse.getData().getStartAge() + "");

        txt_feeds.setText(campaignViewResponse.getData().getGender().toString());
        chip4.setChecked(campaignViewResponse.getData().getCampaignStatus().equalsIgnoreCase("Completed"));

        // setting location
        StringBuilder builder = new StringBuilder();
        if (campaignViewResponse.getData().getLocation().size() > 0) {
            for (String loc : campaignViewResponse.getData().getLocation()) {
                builder.append(loc).append("\n");
            }
            tv_place.setText(builder.toString());
        }
    }

    public boolean pushFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameTabLifetime, fragment, tag)
                    .addToBackStack("fragment")
                    .commit();
            return true;
        }
        return false;
    }

    public boolean pushFragmentGender(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameTabGender, fragment, tag)
                    .addToBackStack("fragment")
                    .commit();
            return true;
        }
        return false;
    }
}