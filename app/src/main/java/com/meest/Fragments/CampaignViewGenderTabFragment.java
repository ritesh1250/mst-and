package com.meest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.meest.R;
import com.meest.responses.CampaignViewResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CampaignViewGenderTabFragment extends Fragment {

    TextView txt_women, txt_men;
    ProgressBar proBar, progressBar;
    TextView txt_1, txt_rsAmt, txt_tvNum, txt1, txt_rsAm, txt_tvN;
    CampaignViewResponse campaignViewResponse;
    String campaignId;
    Context context;
    String tabType;

    public CampaignViewGenderTabFragment(String campaignId, Context context, String tabType) {
        this.campaignId = campaignId;
        this.context = context;
        this.tabType = tabType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gender_tab_campaign_view, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        proBar = view.findViewById(R.id.proBar);
        txt_1 = view.findViewById(R.id.txt_1);
        txt_rsAmt = view.findViewById(R.id.txt_rsAmt);
        txt_tvNum = view.findViewById(R.id.txt_tvNum);
        txt1 = view.findViewById(R.id.txt1);
        txt_rsAm = view.findViewById(R.id.txt_rsAmt);
        txt_tvN = view.findViewById(R.id.txt_tvN);
        txt_women = view.findViewById(R.id.txt_women);
        txt_men = view.findViewById(R.id.txt_men);

        if (tabType.equals("Age")) {

            txt_women.setText("0-35 yr");
            txt_men.setText("35-60 yr");

        } else if (tabType.equals("Location")) {
            txt_women.setText("location 1");
            txt_men.setText("location 2");
        } else {

        }


        fetchData();
        return view;
    }

    private void fetchData() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
        // body
        HashMap<String, Object> body = new HashMap<>();
        body.put("campaignId", campaignId);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<CampaignViewResponse> call = webApi.getCampView(header, body);
        call.enqueue(new Callback<CampaignViewResponse>() {
            @Override
            public void onResponse(Call<CampaignViewResponse> call, Response<CampaignViewResponse> response) {

                if (response.code() == 200 && response.body().getSuccess()) {
                    campaignViewResponse = response.body();
                    bindData();

                } else {
                    Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<CampaignViewResponse> call, Throwable t) {

                Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void bindData() {

        if (tabType.equals("Age")) {
            proBar.setProgress(campaignViewResponse.getData().getFemaleCount());
            progressBar.setProgress(campaignViewResponse.getData().getMaleCount());
            txt_1.setText(campaignViewResponse.getData().getZeroToThirtyPostEngagement().toString());
            txt1.setText(campaignViewResponse.getData().getThirtyToSixtyePostEngagement().toString());
            txt_rsAm.setText(campaignViewResponse.getData().getThirtyToSixtyCostPerEngagement().toString());
            txt_tvN.setText(campaignViewResponse.getData().getTotalMaleReached().toString());
            txt_tvNum.setText(campaignViewResponse.getData().getTotalFemaleReached().toString());
            txt_rsAmt.setText(campaignViewResponse.getData().getFemaleCostPerEngagement().toString());

        } else if (tabType.equals("Location")) {
            txt_men.setText(campaignViewResponse.getData().getLocation().toString().substring(0, 20));
            txt_women.setText(campaignViewResponse.getData().getLocation().toString().substring(0, 20));
            proBar.setProgress(campaignViewResponse.getData().getFemaleCount());
            txt_1.setText(campaignViewResponse.getData().getTotalPostEngagement().toString());
            txt_rsAmt.setText(campaignViewResponse.getData().getFemaleCostPerEngagement().toString());
            txt_tvNum.setText(campaignViewResponse.getData().getPostReactions().toString());
            progressBar.setProgress(campaignViewResponse.getData().getMaleCount());
            txt1.setText(campaignViewResponse.getData().getOneDayPostEngagement().toString());
            txt_rsAm.setText(campaignViewResponse.getData().getMaleCostPerEngagement().toString());
            txt_tvN.setText(campaignViewResponse.getData().getPostReactions().toString());


        } else {
            proBar.setProgress(campaignViewResponse.getData().getFemaleCount());
            txt_1.setText(campaignViewResponse.getData().getTotalPostEngagement().toString());
            txt_rsAmt.setText(campaignViewResponse.getData().getFemaleCostPerEngagement().toString());
            txt_tvNum.setText(campaignViewResponse.getData().getPostReactions().toString());
            progressBar.setProgress(campaignViewResponse.getData().getMaleCount());
            txt1.setText(campaignViewResponse.getData().getOneDayPostEngagement().toString());
            txt_rsAm.setText(campaignViewResponse.getData().getMaleCostPerEngagement().toString());
            txt_tvN.setText(campaignViewResponse.getData().getPostReactions().toString());


        }


    }
}