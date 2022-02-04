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
import com.meest.svs.activities.MusicVideosActivity;
import com.meest.svs.models.FavMusicAudioResponse;

import java.util.List;

public class FavHeartAccountVideoAdapter  extends RecyclerView.Adapter<FavHeartAccountVideoAdapter.VideoHolder> {

    private Context context;
    private List<FavMusicAudioResponse.Row> videoList;

    public FavHeartAccountVideoAdapter(Context context, List<FavMusicAudioResponse.Row> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public FavHeartAccountVideoAdapter.VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_heart_item_account_video, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        final FavMusicAudioResponse.Row model = videoList.get(position);

        holder.tvFavText.setText(model.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(context,MusicVideosActivity.class);
               intent.putExtra(" audioId",model.getId());
               intent.putExtra("audioName",model.getDescription());
               context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {

        TextView tvFavText;
        ImageView ivFavMusic;
        public VideoHolder(@NonNull View itemView) {
            super(itemView);

            tvFavText=itemView.findViewById(R.id.tvFavText);
            ivFavMusic=itemView.findViewById(R.id.ivFavMusic);
        }
    }
}
