package com.meest.social.socialViewModel.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.databinding.ProfileFragmentImagePostBinding;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.social.socialViewModel.model.MediaPostResponse1;

import java.util.ArrayList;
import java.util.List;

public class MyPhotoGridAdapter1 extends RecyclerView.Adapter<MyPhotoGridAdapter1.hashtag_profileVIewHolder> {

    private static final String TAG = "MyPhotoGridAdapter1";
    private List<MediaPostResponse1.Data.RowsFeed> postList = new ArrayList<>();

    @NonNull
    @Override
    public hashtag_profileVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_fragment_image_post, parent, false);
        hashtag_profileVIewHolder HPVH = new hashtag_profileVIewHolder(v);
        return HPVH;
    }

    @Override
    public void onBindViewHolder(@NonNull hashtag_profileVIewHolder holder, int position) {
        MediaPostResponse1.Data.RowsFeed model = postList.get(position);
        if (model.getThumbnail().length() > 0) {
            Glide.with(holder.binding.imgPost.getContext()).load(model.getThumbnail())
                    .placeholder(R.drawable.image_placeholder).into(holder.binding.imgPost);
        } else {
            Glide.with(holder.binding.imgPost.getContext()).load(R.drawable.image_placeholder).into(holder.binding.imgPost);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NotificationSocialFeedActivity.class);
            intent.putExtra("userId", model.getUserId());
            intent.putExtra("postId", model.getId());
            ((AppCompatActivity) v.getContext()).startActivityForResult(intent, 4515);
        });
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void update(List<MediaPostResponse1.Data.RowsFeed> postList) {
        this.postList.clear();
        this.postList.addAll(postList);
        notifyDataSetChanged();
    }

    public class hashtag_profileVIewHolder extends RecyclerView.ViewHolder {

        ProfileFragmentImagePostBinding binding;

        public hashtag_profileVIewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
    public void loadMore(List<MediaPostResponse1.Data.RowsFeed> data) {
        for (int i = 0; i < data.size(); i++) {
            postList.add(data.get(i));
            notifyItemInserted(postList.size() - 1);
        }

    }

    public void loadMore1(List<MediaPostResponse1.Data.RowsFeed> data) {
//        postList.addAll(data);
        notifyItemInserted(postList.size() - 1);

    }

    public void setData(List<MediaPostResponse1.Data.RowsFeed> list) {
        postList = list;
        notifyDataSetChanged();
    }

    public void notifyFeedsDataSetChanged(List<MediaPostResponse1.Data.RowsFeed> postList) {
        this.postList.clear();
        this.postList.addAll(postList);
        notifyDataSetChanged();
    }
}
