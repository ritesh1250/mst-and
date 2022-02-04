package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Interfaces.CampaignInterestCallback;
import com.meest.model.CheckboxModel;
import com.meest.R;

import java.util.List;

public class CampaignInterestAdapter extends RecyclerView.Adapter<CampaignInterestAdapter.InterestViewHolder> {



    private Context mCtx;

    //we are storing all the products in a list
    private List<CheckboxModel> interestList;

    private CampaignInterestCallback campaignInterestCallback;
    private boolean isGender;

    public CampaignInterestAdapter(Context mCtx, List<CheckboxModel> interestList,
                                   CampaignInterestCallback campaignInterestCallback,boolean isGender) {
        this.mCtx = mCtx;
        this.isGender = isGender;
        this.interestList = interestList;
        this.campaignInterestCallback = campaignInterestCallback;

    }

    @NonNull
    @Override
    public InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.campaign_item_interest, null);
        return new InterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {
        holder.interestValue.setText(interestList.get(position).get_name());
        holder.checkInterest.setChecked(interestList.get(position).is_ischecked());
        holder.checkInterest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                campaignInterestCallback.interestClick(position,isChecked, isGender);
            }
        });
    }

    @Override
    public int getItemCount() {

        return interestList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class InterestViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkInterest;
        TextView interestValue;
        public InterestViewHolder(@NonNull View itemView) {
            super(itemView);

            checkInterest=itemView.findViewById(R.id.checkInterest);
            interestValue=itemView.findViewById(R.id.interestValue);

        }
    }
}
