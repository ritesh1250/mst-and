package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.meest.Interfaces.CampaignCallback;
import com.meest.R;
import com.meest.responses.CampaignPreviewResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SeeMoreCampaignActivity extends AppCompatActivity implements CampaignCallback {

    TextView txt_listview;
    RelativeLayout layout_registeredPeople;
    CampaignPreviewResponse campaignPreviewResponse;
    String campaignId, campaignType;
    ImageView back, imv_three_dots;
    ImageView iv_registered_people_list;
    TextView tvSpent, tvPeopleReached, tvImpressions, tvPostEngagement, tvPostReaction, tvPhotoViews, tvPostShare, tvNewConnection, tvMessageConversation, tvPostComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more_campaign);

        if (getIntent().getExtras() == null) {
            Utilss.showToast(this, getString(R.string.Something_went_wrong),
                    R.color.msg_fail);
            finish();
        }

        campaignId = getIntent().getExtras().getString("id", "");
        campaignType = getIntent().getExtras().getString("campaignType", "");

        txt_listview = findViewById(R.id.txt_listview);
        layout_registeredPeople = findViewById(R.id.layout_registeredPeople);
        layout_registeredPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(SeeMoreCampaignActivity.this, CampaignRegisteredPeopleListActivity.class);
                p.putExtra("id", campaignId);
                startActivity(p);
            }
        });

        if (getIntent().getExtras() == null) {
            Utilss.showToast(getApplicationContext(), getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            return;
        }

        if (campaignType.equals("CPC")) {
            layout_registeredPeople.setVisibility(View.GONE);

        } else if (campaignType.equals("CPL")) {
            layout_registeredPeople.setVisibility(View.VISIBLE);

        } else {
            layout_registeredPeople.setVisibility(View.GONE);
        }

        tvSpent = findViewById(R.id.tvSpent);
        tvPeopleReached = findViewById(R.id.tvPeopleReached);
        tvImpressions = findViewById(R.id.tvImpressions);
        tvPostEngagement = findViewById(R.id.tvPostEngagement);
        tvPostReaction = findViewById(R.id.tvPostReaction);
        tvPhotoViews = findViewById(R.id.tvPhotoViews);
        tvPostShare = findViewById(R.id.tvPostShare);
        tvNewConnection = findViewById(R.id.tvNewConnection);
        tvMessageConversation = findViewById(R.id.tvMessageConversion);
        tvPostComments = findViewById(R.id.tvPostComments);

        iv_registered_people_list = findViewById(R.id.iv_registered_people_list);
//        iv_registered_people_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(SeeMoreCampaignActivity.this,CampaignRegisteredPeopleListActivity.class);
//                intent.putExtra("id",campaignId);
//                startActivity(intent);
//            }
//        });

        back = (ImageView) findViewById(R.id.img_back_SeeMoreCampaign);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fetchData();
    }

    private void fetchData() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        // body
        HashMap<String, String> body = new HashMap<>();
        body.put("id", campaignId);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<CampaignPreviewResponse> call = webApi.getCampaignDetail(header, body);
        call.enqueue(new Callback<CampaignPreviewResponse>() {
            @Override
            public void onResponse(Call<CampaignPreviewResponse> call, Response<CampaignPreviewResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {

                    campaignPreviewResponse = response.body();
                    bindData();

                } else {
                    Utilss.showToast(SeeMoreCampaignActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<CampaignPreviewResponse> call, Throwable t) {
                Utilss.showToast(SeeMoreCampaignActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void bindData() {

        txt_listview.setText(campaignPreviewResponse.getData().getCampaignTitle());
        tvSpent.setText(campaignPreviewResponse.getData().getTotalAmount().toString());
        tvPeopleReached.setText(campaignPreviewResponse.getData().getViewerCount().toString());
        tvImpressions.setText(campaignPreviewResponse.getData().getViewerCount().toString());
        tvPostEngagement.setText(campaignPreviewResponse.getData().getViewerCount().toString());
        tvPostReaction.setText(campaignPreviewResponse.getData().getPostReactions().toString());
        tvPhotoViews.setText(campaignPreviewResponse.getData().getPhotoViews().toString());
        tvPostShare.setText(campaignPreviewResponse.getData().getPostShares().toString());
        tvNewConnection.setText(campaignPreviewResponse.getData().getViewerCount().toString());
        tvPostComments.setText(campaignPreviewResponse.getData().getPostComments().toString());
        tvMessageConversation.setText(campaignPreviewResponse.getData().getMessageConversations().toString());
    }

    @Override
    public void lineChartClick(int position) {

    }
}