package com.meest.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.HashtagProfileResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class hashtag_profile_adapter extends RecyclerView.Adapter<hashtag_profile_adapter.hashtag_profileVIewHolder> {

    // private ArrayList<hashtag_profile_item> Hashtag_profile_List;
    ArrayList<HashtagProfileResponse.Data.Videos.Row> HashtagProfileList;
    ArrayList<HashtagProfileResponse.Data> PostedByUser;
    Context mContext;

    public hashtag_profile_adapter(ArrayList<HashtagProfileResponse.Data.Videos.Row> hashtagProfileList, ArrayList<HashtagProfileResponse.Data> postedByUser, Context mContext) {
        HashtagProfileList = hashtagProfileList;
        PostedByUser = postedByUser;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public hashtag_profileVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hashtag_profile_item_list,parent,false);
        hashtag_profileVIewHolder HPVH = new hashtag_profileVIewHolder(v);
        return HPVH;

    }

    @Override
    public void onBindViewHolder(@NonNull hashtag_profileVIewHolder holder, int position) {
        // hashtag_profile_item current = Hashtag_profile_List.get(position);
        // holder.post.setImageResource(current.getPosts());
        HashtagProfileResponse.Data.Videos.Row  hashtagprofile = HashtagProfileList.get(position);
        Glide.with(mContext).
                load(hashtagprofile.getThumbnail()).placeholder(R.drawable.placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.e("TAh","unsuccess="+hashtagprofile.getThumbnail());
                        return false;
                    }
                }) .centerCrop()
                .into(holder.post);




//       HashtagProfileResponse.Data user =PostedByUser.get(position);
//        Log.e("TAg","users="+user.getPostedBy().getUser().getId());
    }

    @Override
    public int getItemCount() {
        return HashtagProfileList == null ? 0 :  HashtagProfileList.size();
    }

    public class hashtag_profileVIewHolder extends RecyclerView.ViewHolder{
        public ImageView post;
        public hashtag_profileVIewHolder(@NonNull View itemView) {
            super(itemView);
            post = (ImageView)itemView.findViewById(R.id.post_img);

        }
    }

}
