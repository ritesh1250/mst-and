package com.meest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.meest.Activities.CreateNewCampaignActivity;
import com.meest.Activities.SeeMoreCampaignActivity;
import com.meest.model.AdMediaData;
import com.meest.R;
import com.meest.responses.CampCompletedResponse;
import com.meest.responses.CampaignPaymentResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.utils.HeightWrappingViewPager;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class CampaignCompletedAdapter extends RecyclerView.Adapter<CampaignCompletedAdapter.ViewHolder> {
    private Context context;
    //    private List<CampaignCompletedModel> completedList;
    CampCompletedResponse campCompletedResponse;
    String type;

    public CampaignCompletedAdapter(Context context, CampCompletedResponse campCompletedResponse, String type) {
        this.context = context;
        this.campCompletedResponse = campCompletedResponse;
        this.type = type;
    }

    @NonNull
    @Override
    public CampaignCompletedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_campaign_completed, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CampaignCompletedAdapter.ViewHolder userViewHolder = (CampaignCompletedAdapter.ViewHolder) holder;
        final CampCompletedResponse.Row model = campCompletedResponse.getData().getRows().get(position);
        List<AdMediaData> adList = new ArrayList<>();

        userViewHolder.btInsight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeeMoreCampaignActivity.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("campaignType", model.getCampaignType());
                context.startActivity(intent);
            }
        });

        AdMediaData data = new AdMediaData();

        data.setButtonType(model.getCallToAction());
        data.setFileUrl(model.getFileURL());
        data.setHeading(model.getCampaignTitle());
        data.setSubHeading(model.getCampaignText());
        data.setWebsiteUrl(model.getWebsiteUrl());
        adList.addAll(campCompletedResponse.getData().getRows().get(position).getOtherImageVideos());

//        adList.addAll(model.getOtherImageVideos());

        ViewPagerAdapterForAd viewPagerAdapter = new ViewPagerAdapterForAd(context, adList, model.getStartDate() + " " + model.getEndDate(), model.getCampaignType());
        userViewHolder.CampViewPager.setAdapter(viewPagerAdapter);
        userViewHolder.CampViewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        userViewHolder.CampViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        userViewHolder.tvStatus.setText(model.getCampaignType());
        userViewHolder.txtAdName.setText(model.getCampaignTitle());
        userViewHolder.tvDateData.setText(model.getStartDate().substring(0, 10));
        userViewHolder.tvStatusCompleted.setText(model.getCampaignStatus());
        userViewHolder.tvAmt.setText("Rs." + model.getTotalAmmount() + "/-");

        if (type.equalsIgnoreCase("Completed")) {
            userViewHolder.btStopAd.setText("Restart");
            userViewHolder.btStopAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, CreateNewCampaignActivity.class);
                    intent.putExtra("SaveType", "edit");
                    intent.putExtra("id", model.getId());
                    intent.putExtra("type", model.getCampaignType());
                    intent.putExtra("SubType", "Completed");
                    context.startActivity(intent);
                }
            });
        } else if (type.equalsIgnoreCase("Running")) {
            final Boolean[] paused = {model.getPaused()};

            if (paused[0]) {
                userViewHolder.btStopAd.setText("Start");
                userViewHolder.tvStatusCompleted.setText("Paused");
                userViewHolder.tvStatusCompleted.setTextColor(userViewHolder.itemView.getContext().getColor(R.color.edit_line_red));
            } else {
                userViewHolder.btStopAd.setText("Pause");
                userViewHolder.tvStatusCompleted.setText("Running");
                userViewHolder.tvStatusCompleted.setTextColor(userViewHolder.itemView.getContext().getColor(R.color.green));
            }

            userViewHolder.btStopAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (paused[0]) {

                        Map<String, String> header = new HashMap<>();
                        header.put("Accept", "application/json");
                        header.put("Content-Type", "application/json");
                        header.put("x-token", SharedPrefreances.getSharedPreferenceString(userViewHolder.itemView.getContext(), SharedPrefreances.AUTH_TOKEN));
                        // body
                        HashMap<String, Object> body = new HashMap<>();
                        body.put("campaign", model.getId());
                        body.put("isPaused", false);

                        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                        Call<CampaignPaymentResponse> call = webApi.getPaymentStatus(header, body);
                        call.enqueue(new Callback<CampaignPaymentResponse>() {
                            @Override
                            public void onResponse(Call<CampaignPaymentResponse> call, Response<CampaignPaymentResponse> response) {

                                if (response.code() == 200 && response.body().getSuccess()) {
                                    userViewHolder.btStopAd.setText("Pause");
                                    userViewHolder.tvStatusCompleted.setText("Running");
                                    userViewHolder.tvStatusCompleted.setTextColor(userViewHolder.itemView.getContext().getColor(R.color.green));
                                    paused[0] = false;
                                } else {
                                    Utilss.showToast(userViewHolder.itemView.getContext(), context.getString(R.string.error_msg), R.color.grey);
                                }
                            }

                            @Override
                            public void onFailure(Call<CampaignPaymentResponse> call, Throwable t) {
                                Utilss.showToast(userViewHolder.itemView.getContext(), context.getString(R.string.error_msg), R.color.grey);
                            }
                        });
                    } else {
                        Map<String, String> header = new HashMap<>();
                        header.put("Accept", "application/json");
                        header.put("Content-Type", "application/json");
                        header.put("x-token", SharedPrefreances.getSharedPreferenceString(userViewHolder.itemView.getContext(), SharedPrefreances.AUTH_TOKEN));
                        // body
                        HashMap<String, Object> body = new HashMap<>();
                        body.put("campaign", model.getId());
                        body.put("isPaused", true);

                        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                        Call<CampaignPaymentResponse> call = webApi.getPaymentStatus(header, body);
                        call.enqueue(new Callback<CampaignPaymentResponse>() {
                            @Override
                            public void onResponse(Call<CampaignPaymentResponse> call, Response<CampaignPaymentResponse> response) {

                                if (response.code() == 200 && response.body().getSuccess()) {
                                    userViewHolder.btStopAd.setText("Start");
                                    userViewHolder.tvStatusCompleted.setText("Paused");
                                    userViewHolder.tvStatusCompleted.setTextColor(userViewHolder.itemView.getContext().getColor(R.color.edit_line_red));
                                    paused[0] = true;
                                } else {
                                    Utilss.showToast(userViewHolder.itemView.getContext(), context.getString(R.string.error_msg), R.color.grey);
                                }
                            }

                            @Override
                            public void onFailure(Call<CampaignPaymentResponse> call, Throwable t) {
                                Utilss.showToast(userViewHolder.itemView.getContext(), context.getString(R.string.error_msg), R.color.grey);
                            }
                        });
                    }
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return campCompletedResponse == null ? 0 : campCompletedResponse.getData().getRows().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        HeightWrappingViewPager CampViewPager;
        TextView txtAdName, tvDateData, tvStatusCompleted, tvAmt, tvStatus;
        Button btLearnMore, btStopAd, btInsight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CampViewPager = itemView.findViewById(R.id.CampViewPager);
            txtAdName = itemView.findViewById(R.id.txtAdName);
            tvDateData = itemView.findViewById(R.id.tvDateData);
            tvStatusCompleted = itemView.findViewById(R.id.tvStatusCompleted);
            tvAmt = itemView.findViewById(R.id.tvAmt);
            btStopAd = itemView.findViewById(R.id.btStopAd);
            btInsight = itemView.findViewById(R.id.btInsight);
            tvStatus = itemView.findViewById(R.id.tvStatus);

        }
    }
}