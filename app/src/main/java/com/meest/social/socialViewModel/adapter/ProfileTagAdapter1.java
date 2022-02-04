package com.meest.social.socialViewModel.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.databinding.ProfileTagGridAdapterBinding;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.social.socialViewModel.model.ProfilesVideoResponse1;

import java.util.ArrayList;
import java.util.List;

public class ProfileTagAdapter1 extends RecyclerView.Adapter<ProfileTagAdapter1.hashtag_profileViewHolder> {

    private List<ProfilesVideoResponse1.Row> hashtag_profile_List = new ArrayList<>();

    @NonNull
    @Override
    public ProfileTagAdapter1.hashtag_profileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_tag_grid_adapter, parent, false);
        ProfileTagAdapter1.hashtag_profileViewHolder HPVH = new ProfileTagAdapter1.hashtag_profileViewHolder(v);
        return HPVH;
    }

    @Override
    public void onBindViewHolder(@NonNull hashtag_profileViewHolder holder, int position) {
        ProfilesVideoResponse1.Row model = hashtag_profile_List.get(position);

        if (model.getThumbnail().length() > 0) {
            Glide.with(holder.binding.imgThum.getContext()).load(model.getThumbnail())
                    .placeholder(R.drawable.image_placeholder).into(holder.binding.imgThum);
        } else {
            Glide.with(holder.binding.imgThum.getContext()).load(R.drawable.image_placeholder).into(holder.binding.imgThum);
        }
        holder.binding.layoutMain.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NotificationSocialFeedActivity.class);
            intent.putExtra("userId", model.getUserId());
            intent.putExtra("postId", model.getId());
            v.getContext().startActivity(intent);
        });

    }

    public void update(List<ProfilesVideoResponse1.Row> list){

        this.hashtag_profile_List.clear();
        this.hashtag_profile_List.addAll(list);
        notifyDataSetChanged();
    }
    public void loadMore(List<ProfilesVideoResponse1.Row> data) {
        Log.e("ProfilesVideoResponse1",data.size()+"data");
           for (int i = 0; i < data.size(); i++) {
            hashtag_profile_List.add(data.get(i));
            notifyItemInserted(hashtag_profile_List.size() - 1);
        }
    }

    @Override
    public int getItemCount() {
        return hashtag_profile_List.size();
    }

    public class hashtag_profileViewHolder extends RecyclerView.ViewHolder {

        ProfileTagGridAdapterBinding binding;

        public hashtag_profileViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
