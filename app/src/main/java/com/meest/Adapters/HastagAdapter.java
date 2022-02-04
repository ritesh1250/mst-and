package com.meest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Activities.HashtagProfileActivity;
import com.meest.R;
import com.meest.responses.SearchHomeResponse;

import java.util.ArrayList;

public class HastagAdapter extends RecyclerView.Adapter<HastagAdapter.hashtagViewHolder> {

    //private ArrayList<HastagItem> Hashtag_List;

    ArrayList<SearchHomeResponse.Data.Hashtag> HashTagList;
    Context mContext;

    public HastagAdapter(ArrayList<SearchHomeResponse.Data.Hashtag> hashTagList, Context mContext) {
        HashTagList = hashTagList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public hashtagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hashtag_itm_list,parent,false);
        hashtagViewHolder HVH = new hashtagViewHolder(v);
        return HVH;
    }

    @Override
    public void onBindViewHolder(@NonNull hashtagViewHolder holder, int position) {
        SearchHomeResponse.Data.Hashtag hastags = HashTagList.get(position);
        Log.e("Atg","hashtags="+hastags.getHashtag());
        holder.hashtagname.setText("#"+hastags.getHashtag());
        holder.videos.setText(String.valueOf(hastags.getCount()));

        holder.rl_hashtag_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent intent =new Intent(mContext, HashtagProfileActivity.class);
                intent.putExtra("hashtag_profile",hastags.getHashtag());
                mContext.startActivity(intent);*/
                mContext.startActivity(new Intent(mContext,HashtagProfileActivity.class).putExtra("hashtag_profile",hastags.getHashtag()));

            }
        });


    }

    @Override
    public int getItemCount() {
        return  HashTagList == null ? 0 : HashTagList.size();

    }

    public class hashtagViewHolder extends RecyclerView.ViewHolder{

        public TextView hashtagname;
        public TextView videos;
        private LinearLayout rl_hashtag_profile;

        public hashtagViewHolder(@NonNull View itemView) {
            super(itemView);
            hashtagname = (TextView)itemView.findViewById(R.id.txt_hashtag_name);
            videos = (TextView)itemView.findViewById(R.id.txt_videos);
            rl_hashtag_profile =(LinearLayout)itemView.findViewById(R.id.rl_hashtag_profile);


        }
    }

}
