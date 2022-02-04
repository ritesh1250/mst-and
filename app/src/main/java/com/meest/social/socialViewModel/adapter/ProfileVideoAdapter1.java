package com.meest.social.socialViewModel.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.social.socialViewModel.model.ProfilesVideoResponse1;

import java.util.ArrayList;
import java.util.List;

public class ProfileVideoAdapter1 extends RecyclerView.Adapter<ProfileVideoAdapter1.hashtag_profileVIewHolder> {

    private final ArrayList<ProfilesVideoResponse1.Row> video_profile_List = new ArrayList<>();

    @NonNull
    @Override
    public ProfileVideoAdapter1.hashtag_profileVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_video_grid_adapter, parent, false);
        ProfileVideoAdapter1.hashtag_profileVIewHolder HPVH = new ProfileVideoAdapter1.hashtag_profileVIewHolder(v);
        return HPVH;
    }


    @Override
    public void onBindViewHolder(@NonNull ProfileVideoAdapter1.hashtag_profileVIewHolder holder, int position) {

        ProfilesVideoResponse1.Row model = video_profile_List.get(position);

        if (model.getThumbnail().length() > 0) {
            Glide.with(holder.img_thum.getContext()).load(model.getThumbnail())
                    .placeholder(R.drawable.image_placeholder).into(holder.img_thum);
        } else {
            Glide.with(holder.img_thum.getContext()).load(R.drawable.image_placeholder).into(holder.img_thum);
        }

        holder.layout_main.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NotificationSocialFeedActivity.class);
            intent.putExtra("userId", model.getUserId());
            intent.putExtra("postId", model.getId());
            v.getContext().startActivity(intent);
        });
    }

    public void update(List<ProfilesVideoResponse1.Row> list) {
        this.video_profile_List.clear();
        this.video_profile_List.addAll(list);
            notifyDataSetChanged();
    }
    public void loadMore(List<ProfilesVideoResponse1.Row> data) {
        for (int i = 0; i < data.size(); i++) {
            video_profile_List.add(data.get(i));
            notifyItemInserted(video_profile_List.size() - 1);
        }

    }
    @Override
    public int getItemCount() {
        return video_profile_List.size();
    }

    public class hashtag_profileVIewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout_main;
        ImageView img_thum;

        public hashtag_profileVIewHolder(@NonNull View itemView) {
            super(itemView);
            layout_main = itemView.findViewById(R.id.layout_main);
            img_thum = itemView.findViewById(R.id.img_thum);

        }
    }


    public void notifyFeedsDataSetChanged(ArrayList<ProfilesVideoResponse1.Row> postList) {
        this.video_profile_List.clear();
        this.video_profile_List.addAll(postList);
        notifyDataSetChanged();
    }

}
