package com.meest.svs.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.VideosResponse;
import com.meest.svs.activities.SVSVideoActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

public class AccountVideoAdapter extends RecyclerView.Adapter<AccountVideoAdapter.VideoHolder> {

    private Context context;
    List<VideosResponse.Row> postsList;

    public AccountVideoAdapter(Context context, List<VideosResponse.Row> postsList) {
        this.context = context;
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public AccountVideoAdapter.VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.account_video_child, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountVideoAdapter.VideoHolder holder, int position) {
        final VideosResponse.Row model = postsList.get(position);
        Glide.with(context).load(model.getThumbnail()).into(holder.videoThumbnail);
        holder.viewCount.setText(String.valueOf(model.getViews()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SVSVideoActivity.class);
                intent.putExtra("videosList", new Gson().toJson(postsList));
                intent.putExtra("selectedPosition", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        TextView viewCount;
        ImageView videoThumbnail;

        public VideoHolder(@NonNull View itemView) {

            super(itemView);

            viewCount = itemView.findViewById(R.id.avc_viewCount);
            videoThumbnail = itemView.findViewById(R.id.avc_videoThumbnail);
        }
    }
}
