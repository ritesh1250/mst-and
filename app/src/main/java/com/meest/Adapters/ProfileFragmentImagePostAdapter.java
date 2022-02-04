package com.meest.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.UserProfileFetchResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProfileFragmentImagePostAdapter  extends RecyclerView.Adapter<ProfileFragmentImagePostAdapter.hashtag_profileVIewHolder> {

    private ArrayList<UserProfileFetchResponse.Post> profile_List_post;
    private Context context;


    public ProfileFragmentImagePostAdapter( Context context,ArrayList<UserProfileFetchResponse.Post> hashtag_profile_List) {
        profile_List_post = hashtag_profile_List;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileFragmentImagePostAdapter.hashtag_profileVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_fragment_image_post,parent,false);
        ProfileFragmentImagePostAdapter.hashtag_profileVIewHolder HPVH = new ProfileFragmentImagePostAdapter.hashtag_profileVIewHolder(v);
        return HPVH;

    }


    @Override
    public void onBindViewHolder(@NonNull ProfileFragmentImagePostAdapter.hashtag_profileVIewHolder holder, int position) {
        Glide.with(context).load(profile_List_post.get(position).getPosts().get(0).getImage()).into(holder.img_post);
    }

    @Override
    public int getItemCount() {
        Log.w("demodemo",String.valueOf(profile_List_post.size()));
        return profile_List_post.size();
    }

    public class hashtag_profileVIewHolder extends RecyclerView.ViewHolder{

        private ImageView img_post;
        public hashtag_profileVIewHolder(@NonNull View itemView) {
            super(itemView);
            img_post = itemView.findViewById(R.id.img_post);
        }
    }

}
