package com.meest.svs.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.Services.DownloadVideo;
import com.meest.svs.activities.SVSVideoActivity;
import com.meest.svs.models.VideoDetailModel;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

public class HashtagVideosAdapter extends RecyclerView.Adapter<HashtagVideosAdapter.HashtagHolder> {

    private Activity activity;
    private List<VideoDetailModel> videosList;
    private boolean isHashTag;
    private String trioLink1;

    public HashtagVideosAdapter(Activity activity, List<VideoDetailModel> videosList,
                                boolean isHashTag, String trioLink1) {
        this.activity = activity;
        this.isHashTag = isHashTag;
        this.trioLink1 = trioLink1;
        this.videosList = videosList;
    }

    @NonNull
    @Override
    public HashtagVideosAdapter.HashtagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.video_hashtag_child, parent, false);
        return new HashtagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagVideosAdapter.HashtagHolder holder, int position) {
        final VideoDetailModel model = videosList.get(position);
        Glide.with(activity).load(model.getThumbnail()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHashTag) {
                    Intent intent = new Intent(activity, SVSVideoActivity.class);
                    intent.putExtra("videosList", new Gson().toJson(videosList));
                    intent.putExtra("selectedPosition", position);
                    activity.startActivity(intent);
                    activity.finish();
                } else {
                    new DownloadVideo(activity, "trioEnd", trioLink1, null).execute(model.getVideoURL());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    public class HashtagHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public HashtagHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.vhc_image);
        }
    }
}
