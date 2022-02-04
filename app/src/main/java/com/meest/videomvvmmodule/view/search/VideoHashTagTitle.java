package com.meest.videomvvmmodule.view.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;

import java.util.ArrayList;

/**
 * Created by sanket kumar.
 */
public class VideoHashTagTitle extends RecyclerView.Adapter<VideoHashTagTitle.MyViewHolder>{
    private final ArrayList<HashtagList> data;
    Context context;


    public VideoHashTagTitle(Context context, ArrayList<HashtagList> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hashtagtitle, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.hashtag_title.setText("#"+data.get(position).getName());
        if (position == 0)
            holder.hashtag_title.setBackgroundResource(R.drawable.movies);
        else if (position == 1)
            holder.hashtag_title.setBackgroundResource(R.drawable.photography);
        else if (position == 2)
            holder.hashtag_title.setBackgroundResource(R.drawable.food);
        else if (position == 3)
            holder.hashtag_title.setBackgroundResource(R.drawable.photography);
        else if (position == 4)
            holder.hashtag_title.setBackgroundResource(R.drawable.follow);
        else if (position == 5)
            holder.hashtag_title.setBackgroundResource(R.drawable.food);
        else if (position == 6)
            holder.hashtag_title.setBackgroundResource(R.drawable.movies);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView hashtag_image;
        TextView hashtag_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            hashtag_image = itemView.findViewById(R.id.hashtag_image);
            hashtag_title = itemView.findViewById(R.id.hashtag_title);
        }
    }
}
