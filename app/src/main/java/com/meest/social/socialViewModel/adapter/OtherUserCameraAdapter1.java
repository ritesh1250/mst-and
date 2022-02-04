package com.meest.social.socialViewModel.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ProfileTagGridAdapterBinding;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.social.socialViewModel.model.MediaPostResponse1;
import com.meest.social.socialViewModel.model.ProfilesVideoResponse1;

import java.util.ArrayList;
import java.util.List;

public class OtherUserCameraAdapter1 extends RecyclerView.Adapter<OtherUserCameraAdapter1.hashtag_profileVIewHolder> {

    private static final String TAG = "OtherUserCameraAdapter1";
    private List<ProfilesVideoResponse1.Row> Hashtag_profile_List = new ArrayList<>();
    OtherUserCameraAdapter1.OnItemClickListener mItemClickListener;


    @NonNull
    @Override
    public OtherUserCameraAdapter1.hashtag_profileVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_tag_grid_adapter, parent, false);
        OtherUserCameraAdapter1.hashtag_profileVIewHolder HPVH = new OtherUserCameraAdapter1.hashtag_profileVIewHolder(v);
        return HPVH;
    }


    @Override
    public void onBindViewHolder(@NonNull OtherUserCameraAdapter1.hashtag_profileVIewHolder holder, int position) {
        holder.setList(Hashtag_profile_List.get(position));
        holder.binding.imgThum.setImageURI(Uri.parse(Hashtag_profile_List.get(position).getThumbnail()));
        holder.binding.layoutMain.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NotificationSocialFeedActivity.class);
            intent.putExtra("userId", Hashtag_profile_List.get(position).getUserId());
            intent.putExtra("postId", Hashtag_profile_List.get(position).getId());
            ((AppCompatActivity) v.getContext()).startActivityForResult(intent, 4515);
        });


    }

    @Override
    public int getItemCount() {
        return Hashtag_profile_List.size();
    }

    public void update(List<ProfilesVideoResponse1.Row> postList) {
        this.Hashtag_profile_List.clear();
        this.Hashtag_profile_List.addAll(postList);
        notifyDataSetChanged();
    }

    public class hashtag_profileVIewHolder extends RecyclerView.ViewHolder {
        ProfileTagGridAdapterBinding binding;

        public void setList(ProfilesVideoResponse1.Row row) {
            binding.setProfileTagAdapterModel(row);
        }

        public hashtag_profileVIewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void loadMore(List<ProfilesVideoResponse1.Row> data) {
        for (int i = 0; i < data.size(); i++) {
            Hashtag_profile_List.add(data.get(i));
            notifyItemInserted(Hashtag_profile_List.size() - 1);
        }
    }

    public void setData(List<ProfilesVideoResponse1.Row> list) {
        Hashtag_profile_List.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(final OtherUserCameraAdapter1.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }


}
