package com.meest.social.socialViewModel.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.databinding.ProfileTagGridAdapterBinding;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.social.socialViewModel.model.ProfilesVideoResponse1;

import java.util.ArrayList;
import java.util.List;

public class OtherUserProfileVideoAdapter1 extends RecyclerView.Adapter<OtherUserProfileVideoAdapter1.hashtag_profileVIewHolder> {

    private static final String TAG = "OtherUserProfileVideoAd";
    public Context context;
    OtherUserProfileVideoAdapter1.OnItemClickListener mItemClickListener;
    private List<ProfilesVideoResponse1.Row> Hashtag_profile_List = new ArrayList<>();

    public OtherUserProfileVideoAdapter1(/*ArrayList<ProfilesVideoResponse1.Row> hashtag_profile_List, Context context*/) {
      /*  Hashtag_profile_List = hashtag_profile_List;
        this.context = context;*/
    }

    @NonNull
    @Override
    public hashtag_profileVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_tag_grid_adapter, parent, false);
        hashtag_profileVIewHolder HPVH = new hashtag_profileVIewHolder(v);
        return HPVH;

    }


    @Override
    public void onBindViewHolder(@NonNull OtherUserProfileVideoAdapter1.hashtag_profileVIewHolder holder, int position) {
        ProfilesVideoResponse1.Row model = Hashtag_profile_List.get(position);
        holder.setList(Hashtag_profile_List.get(position));

        /*
        holder.setList(Hashtag_profile_List.get(position));
        * */

        if (model.getThumbnail().length() > 0) {
            Glide.with(holder.binding.imgThum.getContext()).load(model.getThumbnail())
                    .placeholder(R.drawable.image_placeholder).into(holder.binding.imgThum);
        } else {
            Glide.with(holder.binding.imgThum.getContext()).load(R.drawable.image_placeholder).into(holder.binding.imgThum);
        }


//        holder.layout_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mItemClickListener != null)
//                    mItemClickListener.onItemClick(position);
//            }
//        });
        holder.binding.layoutMain.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), NotificationSocialFeedActivity.class);
            intent.putExtra("userId", Hashtag_profile_List.get(position).getUserId());
            intent.putExtra("postId", Hashtag_profile_List.get(position).getId());
            ((AppCompatActivity) v.getContext()).startActivityForResult(intent, 4515);
        });

//        Glide.with(context)
//                .load(Hashtag_profile_List.get(position).getPosts().get(0).getPost())
//                .apply(new RequestOptions().override(400, 600)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .error(R.drawable.video_story_icon))
//                .into(holder.img_thum);


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

    public void loadMore(List<ProfilesVideoResponse1.Row> data) {
        Log.e(TAG, "Running loadMore");
        for (int i = 0; i < data.size(); i++) {
            Hashtag_profile_List.add(data.get(i));
            notifyItemInserted(Hashtag_profile_List.size() - 1);
        }

    }

    public void setData(List<ProfilesVideoResponse1.Row> list) {
        Log.e(TAG, "Running setData");
        Hashtag_profile_List = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(final OtherUserProfileVideoAdapter1.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class hashtag_profileVIewHolder extends RecyclerView.ViewHolder {
        ProfileTagGridAdapterBinding binding;

        public hashtag_profileVIewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setList(ProfilesVideoResponse1.Row row) {
            binding.setProfileTagAdapterModel(row);
        }
    }


}
