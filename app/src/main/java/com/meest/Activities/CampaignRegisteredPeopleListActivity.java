package com.meest.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Adapters.CampaignLeadsSignupAdapter;
import com.meest.R;
import com.meest.responses.CampaignSignUpLeadsResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CampaignRegisteredPeopleListActivity extends AppCompatActivity {

    RecyclerView recyclerViewCampaignSign_upLeads;
    CampaignLeadsSignupAdapter campaignLeadsSignupAdapter;
    CampaignSignUpLeadsResponse campaignSignUpLeadsResponse;
    String campaignId;
    ImageView img_back_Campaign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_registered_people_layout);

        recyclerViewCampaignSign_upLeads = findViewById(R.id.recyclerViewCampaignSign_upLeads);
        img_back_Campaign = findViewById(R.id.img_back_Campaign);
        img_back_Campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (getIntent().getExtras() == null) {
            Utilss.showToast(getApplicationContext(), getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            return;
        }
        campaignId = getIntent().getExtras().getString("id", "");
        fetchData();

    }

    private void fetchData() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(CampaignRegisteredPeopleListActivity.this, SharedPrefreances.AUTH_TOKEN));

        HashMap<String, String> body = new HashMap<>();
        body.put("campaignId", campaignId);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<CampaignSignUpLeadsResponse> call = webApi.getSignupLeads(header, body);
        call.enqueue(new Callback<CampaignSignUpLeadsResponse>() {
            @Override
            public void onResponse(Call<CampaignSignUpLeadsResponse> call, Response<CampaignSignUpLeadsResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {
                    campaignSignUpLeadsResponse = response.body();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(CampaignRegisteredPeopleListActivity.this, LinearLayoutManager.VERTICAL, false);
                    recyclerViewCampaignSign_upLeads.setLayoutManager(layoutManager);
                    recyclerViewCampaignSign_upLeads.setHasFixedSize(true);
                    campaignLeadsSignupAdapter = new CampaignLeadsSignupAdapter(CampaignRegisteredPeopleListActivity.this, campaignSignUpLeadsResponse);
                    recyclerViewCampaignSign_upLeads.setAdapter(campaignLeadsSignupAdapter);

                } else {
                    Utilss.showToast(CampaignRegisteredPeopleListActivity.this, getResources().getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<CampaignSignUpLeadsResponse> call, Throwable t) {
                Utilss.showToast(CampaignRegisteredPeopleListActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }


}