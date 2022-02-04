package com.meest.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.model.AdsListingItem;
import com.meest.R;

import java.util.ArrayList;

public class AdsListingAdapter extends RecyclerView.Adapter<AdsListingAdapter.AdsListingViewHolder> {

    private ArrayList<AdsListingItem> AdsListingList;

    @NonNull
    @Override
    public AdsListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_listing_item_list,parent,false);
        AdsListingViewHolder ALVH = new AdsListingViewHolder(v);
        return ALVH;
    }

    @Override
    public void onBindViewHolder(@NonNull AdsListingViewHolder holder, int position) {

        AdsListingItem AdsListing = AdsListingList.get(position);
        holder.Name.setText(AdsListing.getName());
        holder.Startdate.setText(AdsListing.getStartdate());
        holder.Enddate.setText(AdsListing.getEnddate());
        holder.Amount_paid.setText(AdsListing.getAmountPaid());
        holder.profile_img.setImageResource(AdsListing.getProfile_img());



    }

    @Override
    public int getItemCount() {
        return AdsListingList.size();
    }


    public class AdsListingViewHolder extends RecyclerView.ViewHolder{
        public ImageView profile_img;
        public TextView Name;
        public TextView Startdate;
        public TextView Enddate;
        public TextView Amount_paid;



        public AdsListingViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_img = (ImageView)itemView.findViewById(R.id.pro_img_adslisting);

            Name = (TextView)itemView.findViewById(R.id.txt_ads_listing_name);
            Startdate = (TextView)itemView.findViewById(R.id.txt_startdate);
            Enddate = (TextView)itemView.findViewById(R.id.txt_enddate);
            Amount_paid = (TextView)itemView.findViewById(R.id.txt_amount_paid);

        }
    }
    public AdsListingAdapter(ArrayList<AdsListingItem> adslistinglist){
        AdsListingList = adslistinglist;
    }
}
