package com.meest.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.model.video_item;
import com.meest.R;

import java.util.ArrayList;

public class video_adepter extends RecyclerView.Adapter<video_adepter.videoViewHolder> {

    private ArrayList<video_item> Video_List;

    @NonNull
    @Override
    public videoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item_list,parent,false);
        videoViewHolder VVH = new videoViewHolder(v);
        return VVH;

    }

    @Override
    public void onBindViewHolder(@NonNull videoViewHolder holder, int position) {

        video_item current = Video_List.get(position);
        holder.profile_img.setImageResource(current.getProfile_img());
        holder.like_counts.setText(current.getLike_count());
        holder.bg_img.setImageResource(current.getBackgroung_img());




    }

    @Override
    public int getItemCount() {
        return Video_List.size();
    }

    public class videoViewHolder extends RecyclerView.ViewHolder{

        public ImageView profile_img;
        public ImageView bg_img;
        public TextView like_counts;

        public videoViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_img = (ImageView)itemView.findViewById(R.id.video_profile_image);
            bg_img = (ImageView) itemView.findViewById(R.id.background_image);
            like_counts=(TextView)itemView.findViewById(R.id.likes_count);
        }
    }

    public  video_adepter(ArrayList<video_item> videolist){
        Video_List = videolist;
    }
}
