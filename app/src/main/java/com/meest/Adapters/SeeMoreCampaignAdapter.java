package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Interfaces.CampaignCallback;
import com.meest.R;
import com.meest.responses.CampaignPreviewResponse;
import com.meest.responses.SeeMoreCampaignItem;

import java.util.ArrayList;
import java.util.List;

public class SeeMoreCampaignAdapter extends RecyclerView.Adapter<SeeMoreCampaignAdapter.SeeMoreCampaignViewHolder> {

    private ArrayList<SeeMoreCampaignItem> SeeMoreCampaignList;
    private Context context;
    private CampaignCallback campaignCallback;
    private List<CampaignPreviewResponse.Data> campaignList;


    public SeeMoreCampaignAdapter(ArrayList<SeeMoreCampaignItem> seeMoreCampaignList, Context context, CampaignCallback campaignCallback,List<CampaignPreviewResponse.Data> campaignList) {
        SeeMoreCampaignList = seeMoreCampaignList;
        this.context = context;
        this.campaignCallback = campaignCallback;
        this.campaignList=campaignList;
    }

    @NonNull
    @Override
    public SeeMoreCampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.see_more_campaign_item_list,parent,false);
        SeeMoreCampaignViewHolder SMCVH = new SeeMoreCampaignViewHolder(v);
        return SMCVH;
    }

    @Override
    public void onBindViewHolder(@NonNull SeeMoreCampaignViewHolder holder, int position) {
        SeeMoreCampaignItem seemore = SeeMoreCampaignList.get(position);
        holder.seemoretxt1.setText(seemore.getTxt1());
        holder.seemoretxt2.setText(seemore.getTxt2());

        holder.card_view_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campaignCallback.lineChartClick(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return SeeMoreCampaignList.size();
    }

    public class SeeMoreCampaignViewHolder extends RecyclerView.ViewHolder{

        public TextView seemoretxt1;
        public TextView seemoretxt2;
        public CardView card_view_item;

        public SeeMoreCampaignViewHolder(@NonNull View itemView) {
            super(itemView);

            seemoretxt1 = (TextView)itemView.findViewById(R.id.txt1_seemore);
            seemoretxt2 = (TextView)itemView.findViewById(R.id.txt2_seemore);
            card_view_item = (CardView)itemView.findViewById(R.id.card_view_item);
        }
    }
}
