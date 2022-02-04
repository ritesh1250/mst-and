package com.meest.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;

import java.util.ArrayList;

public class ProfilePostListViewAdapter extends RecyclerView.Adapter<ProfilePostListViewAdapter.hashtag_profileVIewHolder> {

    private ArrayList<String> Hashtag_profile_List;
    ProfilePostListViewAdapter.OnItemClickListener mItemClickListener;

    public ProfilePostListViewAdapter(ArrayList<String> hashtag_profile_List) {
        Hashtag_profile_List = hashtag_profile_List;
    }

    @NonNull
    @Override
    public ProfilePostListViewAdapter.hashtag_profileVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_post_listview_adapter,parent,false);
        ProfilePostListViewAdapter.hashtag_profileVIewHolder HPVH = new ProfilePostListViewAdapter.hashtag_profileVIewHolder(v);
        return HPVH;

    }


    @Override
    public void onBindViewHolder(@NonNull ProfilePostListViewAdapter.hashtag_profileVIewHolder holder, int position) {


        holder.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Hashtag_profile_List.size();
    }

    public class hashtag_profileVIewHolder extends RecyclerView.ViewHolder{

            ImageView img_menu;
        public hashtag_profileVIewHolder(@NonNull View itemView) {
            super(itemView);
            img_menu = itemView.findViewById(R.id.img_menu);

        }
    }


    public void setOnItemClickListener(final ProfilePostListViewAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }





}
