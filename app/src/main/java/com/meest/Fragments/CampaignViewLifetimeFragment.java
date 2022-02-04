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


public class CampaignViewLifetimeFragment extends Fragment {

    TextView txt_startDateNum, txt_endDateNum, txt_num, tv_rs, tv_rs_one;
    ProgressBar pBar;
    CampaignViewResponse campaignViewResponse;

    String campaignId;
    Context context;

    public CampaignViewLifetimeFragment(String campaignId, Context context) {
        this.campaignId = campaignId;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_campaign_view_lifetime, container, false);

        txt_startDateNum = view.findViewById(R.id.txt_startDateNum);
        txt_endDateNum = view.findViewById(R.id.txt_endDateNum);
        txt_num = view.findViewById(R.id.txt_num);
        tv_rs = view.findViewById(R.id.tv_rs);
        tv_rs_one = view.findViewById(R.id.tv_rs_one);
        pBar = view.findViewById(R.id.pBar);
        fetchData();
        return view;
    }

    private void fetchData() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
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
                    Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<CampaignViewResponse> call, Throwable t) {

                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void bindData() {
        tv_rs.setText(campaignViewResponse.getData().getTotalAmmount().toString());
        tv_rs_one.setText(campaignViewResponse.getData().getTotalAmmount().toString());
        txt_num.setText(campaignViewResponse.getData().getTotalPostEngagement().toString());
        txt_endDateNum.setText(campaignViewResponse.getData().getEndDate().substring(0, 10));
        txt_startDateNum.setText(campaignViewResponse.getData().getStartDate().substring(0, 10));
        pBar.setProgress(campaignViewResponse.getData().getTotalPostEngagement());
    }
}