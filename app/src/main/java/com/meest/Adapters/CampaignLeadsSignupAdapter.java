package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.CampaignSignUpLeadsResponse;

public class CampaignLeadsSignupAdapter extends RecyclerView.Adapter<CampaignLeadsSignupAdapter.CampaignSignUpLeadsViewHolder> {

    private Context context;
    CampaignSignUpLeadsResponse campaignSignUpLeadsResponse;

    public CampaignLeadsSignupAdapter(Context context, CampaignSignUpLeadsResponse campaignSignUpLeadsResponse) {
        this.context = context;
        this.campaignSignUpLeadsResponse = campaignSignUpLeadsResponse;
    }

    @NonNull
    @Override
    public CampaignSignUpLeadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_campaign_signup_leads, parent, false);
        CampaignLeadsSignupAdapter.CampaignSignUpLeadsViewHolder viewHolder = new CampaignLeadsSignupAdapter.CampaignSignUpLeadsViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CampaignSignUpLeadsViewHolder holder, int position) {
        final  CampaignLeadsSignupAdapter.CampaignSignUpLeadsViewHolder userViewHolder = ( CampaignLeadsSignupAdapter.CampaignSignUpLeadsViewHolder) holder;
        final CampaignSignUpLeadsResponse.Row model = campaignSignUpLeadsResponse.getData().getRows().get(position);

        holder.tv_name.setText(model.getName());
        holder.tv_phone.setText(model.getMobile());
        holder.tv_mail.setText(model.getEmail());
        holder.tv_add1.setText(model.getAddress());
    }

    @Override
    public int getItemCount() {
        return campaignSignUpLeadsResponse == null ? 0 : campaignSignUpLeadsResponse.getData().getRows().size();
    }

    public class CampaignSignUpLeadsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_1,tv_add1,tv_add2,tv_phone,tv_mail,tv_name;
        public CampaignSignUpLeadsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_1=itemView.findViewById(R.id.tv_1);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_add1=itemView.findViewById(R.id.tv_add1);
            tv_add2=itemView.findViewById(R.id.tv_add2);
            tv_mail=itemView.findViewById(R.id.tv_mail);
            tv_phone=itemView.findViewById(R.id.tv_phone);

        }
    }
}
