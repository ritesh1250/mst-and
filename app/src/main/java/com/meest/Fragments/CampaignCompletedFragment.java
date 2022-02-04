package com.meest.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.CampaignCompletedAdapter;
import com.meest.R;
import com.meest.responses.CampCompletedResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CampaignCompletedFragment extends Fragment {
    LottieAnimationView loadingIndicator;
    LinearLayout noAdsCreateComplete;
    RecyclerView recyclerViewCompleted;
    CampaignCompletedAdapter campaignCompletedAdapter;
    CampCompletedResponse CampCompletedResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_campaign_completed, container, false);
        loadingIndicator = view.findViewById(R.id.loading);
        loadingIndicator.setAnimation("loading.json");
        loadingIndicator.playAnimation();
        loadingIndicator.loop(true);

        noAdsCreateComplete = view.findViewById(R.id.noAdsCreateComplete);
        recyclerViewCompleted = view.findViewById(R.id.recyclerViewCompleted);
        recyclerViewCompleted.setNestedScrollingEnabled(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchData();
    }

    private void fetchData() {
        loadingIndicator.setVisibility(View.VISIBLE);
        noAdsCreateComplete.setVisibility(View.GONE);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
        // header.put("x-token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJJZCI6IjNmZmI0MmQ5LWUxYjQtNGU0ZC04NzdjLTYwODRjMGZhMzYwNSIsImVtYWlsIjoiIiwiY3JlYXRlZEF0IjoiMjAyMC0xMC0xMlQxNDowMzoyMC41MTRaIn0sImlhdCI6MTYwMjUxMTQwMH0.zVS3c5B24b-r2PU6hYPANWwavs409IuLXPNPbcMLWdM");
        // body
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<CampCompletedResponse> call = webApi.getCampCompleted(header);
        call.enqueue(new Callback<CampCompletedResponse>() {
            @Override
            public void onResponse(Call<CampCompletedResponse> call, Response<CampCompletedResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {
                    loadingIndicator.setVisibility(View.GONE);
                    noAdsCreateComplete.setVisibility(View.GONE);
                    CampCompletedResponse = response.body();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewCompleted.setLayoutManager(layoutManager);
                    recyclerViewCompleted.setHasFixedSize(true);
                    campaignCompletedAdapter = new CampaignCompletedAdapter(getContext(), CampCompletedResponse, "Completed");
                    recyclerViewCompleted.setAdapter(campaignCompletedAdapter);

                    if (response.body().getData().getRows().size() == 0) {
                        noAdsCreateComplete.setVisibility(View.VISIBLE);
                    }

                } else {
                    noAdsCreateComplete.setVisibility(View.VISIBLE);
                    loadingIndicator.setVisibility(View.GONE);
                    Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<CampCompletedResponse> call, Throwable t) {
                noAdsCreateComplete.setVisibility(View.VISIBLE);
                loadingIndicator.setVisibility(View.GONE);
                Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }
}