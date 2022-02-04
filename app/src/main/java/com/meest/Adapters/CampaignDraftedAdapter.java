package com.meest.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.meest.Activities.CampaignPreviewActivity;
import com.meest.Activities.CreateNewCampaignActivity;
import com.meest.Interfaces.CampaignDeleteCallback;
import com.meest.model.AdMediaData;
import com.meest.R;
import com.meest.responses.CampaignDeleteResponse;
import com.meest.responses.CampaignDraftedResponse;
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


public class CampaignDraftedAdapter extends RecyclerView.Adapter<CampaignDraftedAdapter.ViewHolder> {
    private Context context;
    CampaignDraftedResponse campaignDraftedResponse;
    private CampaignDeleteCallback campaignDeleteCallback;

    public CampaignDraftedAdapter(Context context, CampaignDraftedResponse campaignDraftedResponse, CampaignDeleteCallback campaignDeleteCallback) {
        this.context = context;
        this.campaignDraftedResponse = campaignDraftedResponse;
        this.campaignDeleteCallback = campaignDeleteCallback;
    }

    @NonNull
    @Override
    public CampaignDraftedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_campaign_drafted, parent, false);
        CampaignDraftedAdapter.ViewHolder viewHolder = new CampaignDraftedAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CampaignDraftedAdapter.ViewHolder holder, int position) {
        final CampaignDraftedAdapter.ViewHolder userViewHolder = (CampaignDraftedAdapter.ViewHolder) holder;
        final CampaignDraftedResponse.Row model = campaignDraftedResponse.getData().getRows().get(position);
        List<AdMediaData> adList = new ArrayList<>();

        AdMediaData data = new AdMediaData();


        //on success

//        campaignDeleteCallback.campaignDeleted();

        data.setButtonType(model.getCallToAction());
        data.setFileUrl(model.getFileURL());
        data.setHeading(model.getCampaignTitle());
        data.setSubHeading(model.getCampaignText());
        data.setWebsiteUrl(model.getWebsiteUrl());
        adList.addAll(model.getOtherImageVideos());

//        adList.addAll(model.getOtherImageVideos());

        ViewPagerAdapterForAd viewPagerAdapter = new ViewPagerAdapterForAd(context, adList, model.getStartDate() + " " + model.getEndDate(), model.getCampaignType());
        userViewHolder.CampViewPagerDraft.setAdapter(viewPagerAdapter);
        userViewHolder.CampViewPagerDraft.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        userViewHolder.CampViewPagerDraft.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        userViewHolder.txtAdTitleName.setText(model.getName());
        userViewHolder.tvStartDateData.setText(model.getStartDate().substring(0, 10));
        userViewHolder.tvEndDateData.setText(model.getEndDate().substring(0, 10));

        if (model.getAdminBlocked()) {
            userViewHolder.tvDraftAmt.setText("Blocked by Admin");
            userViewHolder.btEdit.setVisibility(View.GONE);
            userViewHolder.btActivate.setVisibility(View.GONE);
            userViewHolder.btDelete.setVisibility(View.GONE);
        } else if (model.getPaymentStatus().equalsIgnoreCase("Drafted")) {
            userViewHolder.tvDraftAmt.setText("Payment pending");
        } else if (model.getPaymentStatus().equalsIgnoreCase("Pending")) {
            userViewHolder.tvDraftAmt.setText("Payment pending");
        } else if (model.getPaymentStatus().equalsIgnoreCase("Failed")) {
            userViewHolder.tvDraftAmt.setText("Payment pending");
        } else if (!model.getApproved()) {
            userViewHolder.tvDraftAmt.setText("Pending for Approval");
            userViewHolder.btEdit.setVisibility(View.GONE);
            userViewHolder.btActivate.setVisibility(View.GONE);
            userViewHolder.btDelete.setVisibility(View.GONE);
        }

        Log.e("alhaj", "a" + model.getPaymentStatus());

        userViewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
                final View dialogView = LayoutInflater.from(context).inflate(R.layout.campaign_delete_notification_popup, null, false);
                Button btYes, btNo;
                btYes = dialogView.findViewById(R.id.btYes);
                btNo = dialogView.findViewById(R.id.btNo);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                btYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> header = new HashMap<>();
                        header.put("Accept", "application/json");
                        header.put("Content-Type", "application/json");
                        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
                        HashMap<String, String> body = new HashMap<>();
                        body.put("id", campaignDraftedResponse.getData().getRows().get(position).getId());
                        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

                        Call<CampaignDeleteResponse> call = webApi.deleteCampaign(header, body);
                        call.enqueue(new Callback<CampaignDeleteResponse>() {
                            @Override
                            public void onResponse(Call<CampaignDeleteResponse> call, Response<CampaignDeleteResponse> response) {

                                if (response.code() == 200 && response.body().getSuccess()) {

                                    campaignDeleteCallback.campaignDeleted();
                                    alertDialog.dismiss();

                                } else {

                                    Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                }
                            }

                            @Override
                            public void onFailure(Call<CampaignDeleteResponse> call, Throwable t) {

                                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                            }
                        });
                    }
                });
                btNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });


        userViewHolder.tvStat.setText(model.getCampaignType());

        userViewHolder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateNewCampaignActivity.class);
                intent.putExtra("SaveType", "edit");
                intent.putExtra("id", model.getId());
                intent.putExtra("type", model.getCampaignType());
                context.startActivity(intent);
            }
        });

        userViewHolder.btActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CampaignPreviewActivity.class);
                intent.putExtra("id", campaignDraftedResponse.getData().getRows().get(position).getId());
                intent.putExtra("campaignStatus", campaignDraftedResponse.getData().getRows().get(position).getCampaignStatus());
                intent.putExtra("amount", Integer.parseInt(campaignDraftedResponse.getData().getRows().get(position).getTotalAmmount()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return campaignDraftedResponse == null ? 0 : campaignDraftedResponse.getData().getRows().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        HeightWrappingViewPager CampViewPagerDraft;
        TextView txtAdTitleName, tvStartDateData, tvEndDateData, tvDraftAmt, tvStat;
        Button btActivate, btEdit, btDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            CampViewPagerDraft = itemView.findViewById(R.id.CampViewPagerDraft);
            txtAdTitleName = itemView.findViewById(R.id.txtAdTitleName);
            tvStartDateData = itemView.findViewById(R.id.tvStartDateData);
            tvEndDateData = itemView.findViewById(R.id.tvEndDateData);
            btActivate = itemView.findViewById(R.id.btActivate);
            btEdit = itemView.findViewById(R.id.btEdit);
            btDelete = itemView.findViewById(R.id.btDelete);
            tvDraftAmt = itemView.findViewById(R.id.tvDraftAmt);
            tvStat = itemView.findViewById(R.id.tvStat);

        }
    }
}
