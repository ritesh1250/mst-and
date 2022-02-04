package com.meest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.R;
import com.meest.responses.UserProfileFetchResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OtherUserPhotoGridAdapter extends RecyclerView.Adapter<OtherUserPhotoGridAdapter.hashtag_profileVIewHolder> {

    private ArrayList<UserProfileFetchResponse.Row> profile_List_post;
    private Context context;

    public OtherUserPhotoGridAdapter(Context context, ArrayList<UserProfileFetchResponse.Row> hashtag_profile_List) {
        profile_List_post = hashtag_profile_List;
        this.context = context;
    }

    @NonNull
    @Override
    public OtherUserPhotoGridAdapter.hashtag_profileVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_fragment_image_post, parent, false);
        OtherUserPhotoGridAdapter.hashtag_profileVIewHolder HPVH = new OtherUserPhotoGridAdapter.hashtag_profileVIewHolder(v);
        return HPVH;
    }


    @Override
    public void onBindViewHolder(@NonNull OtherUserPhotoGridAdapter.hashtag_profileVIewHolder holder, int position) {
        final UserProfileFetchResponse.Row model = profile_List_post.get(position);


        if (model.getThumbnail().length() > 0) {
            Glide.with(context).load(model.getThumbnail()).placeholder(R.drawable.image_placeholder).into(holder.img_post);
        } else {
            Glide.with(context).load(R.drawable.image_placeholder).into(holder.img_post);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(context, FeedActivity.class);
                intent.putExtra("posts", new Gson().toJson(profile_List_post));
                intent.putExtra("position", position);
                context.startActivity(intent);*/
                Intent intent = new Intent(context, NotificationSocialFeedActivity.class);
                intent.putExtra("userId", model.getUserId());
                intent.putExtra("postId", model.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.w("demodemo", String.valueOf(profile_List_post.size()));
        return profile_List_post.size();
    }

    public class hashtag_profileVIewHolder extends RecyclerView.ViewHolder {

        private ImageView img_post;

        public hashtag_profileVIewHolder(@NonNull View itemView) {
            super(itemView);
            img_post = itemView.findViewById(R.id.img_post);
        }
    }

}
