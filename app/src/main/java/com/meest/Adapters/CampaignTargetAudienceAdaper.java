package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Interfaces.CampaignInterestListCallback;
import com.meest.R;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicsResponse;

public class CampaignTargetAudienceAdaper extends RecyclerView.Adapter<CampaignTargetAudienceAdaper.InterestViewHolder> {
    private Context mCtx;

    //we are storing all the products in a list
   // private List<String> interestListItem;
    private TopicsResponse topicsResponse;

    private CampaignInterestListCallback campaignInterestListCallbacks;

    public CampaignTargetAudienceAdaper(Context mCtx,TopicsResponse topicsResponse, CampaignInterestListCallback campaignInterestListCallbacks) {
        this.mCtx = mCtx;
        this.topicsResponse = topicsResponse;
        this.campaignInterestListCallbacks = campaignInterestListCallbacks;
    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.campaign_target_audience_layout, null);
        return new CampaignTargetAudienceAdaper.InterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {

        final  CampaignTargetAudienceAdaper.InterestViewHolder userViewHolder = ( CampaignTargetAudienceAdaper.InterestViewHolder) holder;
        final TopicsResponse.Row model = topicsResponse.getData().getRows().get(position);

        holder.interestVal.setText(model.getTopic());

        holder.ivCloseInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campaignInterestListCallbacks.interestItemClick(position);
            }
        });
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
  return topicsResponse == null ? 0 : topicsResponse.getData().getRows().size();
    }



    public class InterestViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCloseInterest;
        TextView interestVal;

        public InterestViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCloseInterest=itemView.findViewById(R.id.ivCloseInterest);
            interestVal=itemView.findViewById(R.id.interestVal);
        }
    }
}
