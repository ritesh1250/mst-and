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
import com.meest.Adapters.CampaignDraftedAdapter;
import com.meest.Interfaces.CampaignDeleteCallback;
import com.meest.R;
import com.meest.responses.CampaignDraftedResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CampaignDraftedFragment extends Fragment implements CampaignDeleteCallback {
    LottieAnimationView loadingIndicator;
    RecyclerView recyclerViewDrafted;
    LinearLayout noAdsCreateDrafted;
    CampaignDraftedAdapter campaignDraftedAdapter;
    CampaignDraftedResponse CampDraftedResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_campaign_drafted, container, false);
        loadingIndicator = view.findViewById(R.id.loading);
        loadingIndicator.setAnimation("loading.json");
        loadingIndicator.playAnimation();
        loadingIndicator.loop(true);
        recyclerViewDrafted = view.findViewById(R.id.recyclerViewDrafted);
        noAdsCreateDrafted = view.findViewById(R.id.noAdsCreateDrafted);
        recyclerViewDrafted.setNestedScrollingEnabled(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchData();
    }

    private void fetchData() {
        loadingIndicator.setVisibility(View.VISIBLE);
        noAdsCreateDrafted.setVisibility(View.GONE);
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
        //  header.put("x-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJJZCI6IjNmZmI0MmQ5LWUxYjQtNGU0ZC04NzdjLTYwODRjMGZhMzYwNSIsImVtYWlsIjoiIiwiY3JlYXRlZEF0IjoiMjAyMC0xMC0xMlQxNDowMzoyMC41MTRaIn0sImlhdCI6MTYwMjUxMTQwMH0.zVS3c5B24b-r2PU6hYPANWwavs409IuLXPNPbcMLWdM");
        HashMap<String, String> body = new HashMap<>();
        body.put("campaignStatus", "Drafted");

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

        Call<CampaignDraftedResponse> call = webApi.saveCampaignDraft(header, body);

        call.enqueue(new Callback<CampaignDraftedResponse>() {
            @Override
            public void onResponse(Call<CampaignDraftedResponse> call, Response<CampaignDraftedResponse> response) {

                if (response.code() == 200 && response.body().getSuccess()) {
                    loadingIndicator.setVisibility(View.GONE);
                    noAdsCreateDrafted.setVisibility(View.GONE);
                    CampDraftedResponse = response.body();
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewDrafted.setLayoutManager(layoutManager);
                    recyclerViewDrafted.setHasFixedSize(true);
                    campaignDraftedAdapter = new CampaignDraftedAdapter(getContext(), CampDraftedResponse, CampaignDraftedFragment.this);
                    recyclerViewDrafted.setAdapter(campaignDraftedAdapter);
                    campaignDraftedAdapter.notifyDataSetChanged();
                    if (response.body().getData().getRows().size() == 0) {
                        noAdsCreateDrafted.setVisibility(View.VISIBLE);
                        //  loadingIndicator.setVisibility(View.VISIBLE);
                    }

                } else {
                    loadingIndicator.setVisibility(View.GONE);
                    noAdsCreateDrafted.setVisibility(View.VISIBLE);
                    Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<CampaignDraftedResponse> call, Throwable t) {
                noAdsCreateDrafted.setVisibility(View.VISIBLE);
                loadingIndicator.setVisibility(View.GONE);
                Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    @Override
    public void campaignDeleted() {
        fetchData();
    }
}