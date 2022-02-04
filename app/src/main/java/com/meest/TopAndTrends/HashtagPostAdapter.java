package com.meest.TopAndTrends;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.meest.R;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.responses.PostHashTagResponse;
import com.meest.social.socialViewModel.view.comment.VideoCommentActivityNew;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.utils.goLiveUtils.CommonUtils;

import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class HashtagPostAdapter extends RecyclerView.Adapter<HashtagPostAdapter.HashtagHolder> {

    private Context context;
    private List<PostHashTagResponse.Row> postList;
    private boolean isHashTag;
    private String trioLink1;

    public HashtagPostAdapter(Context context, List<PostHashTagResponse.Row> postList, boolean isHashTag, String trioLink1) {
        this.context = context;
        this.isHashTag = isHashTag;
        this.trioLink1 = trioLink1;
        this.postList = postList;
    }

    @NonNull
    @Override
    public HashtagPostAdapter.HashtagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hashtag_feed_child, parent, false);
        return new HashtagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagPostAdapter.HashtagHolder holder, int position) {
        PostHashTagResponse.Row model=postList.get(position);

        if (model.getPosts()!=null&& model.getPosts().size()>0&&model.getPosts().get(0)!=null)
        {
            Glide.with(context).load(model.getPosts().get(0).getPost()).into(holder.imageView);
        }
        else
        {
            Glide.with(context).load("").into(holder.imageView);
        }
        Glide.with(context).load(model.getUser().getDisplayPicture()).into(holder.img_profile);
        holder.name.setText(model.getUser().getFirstName());
        holder.name2.setText(model.getUser().getUsername());
        holder.tvAbout.setText(CommonUtils.decodeEmoji(model.getUser().getAbout()));


        holder.txt_liked_count.setText(String.valueOf(model.getLikeCounts()));



        holder.layout_commnet.setOnClickListener(v->{
            Intent intent = new Intent(context, VideoCommentActivityNew.class);
            intent.putExtra("videoId", model.getId());
            intent.putExtra("msg",  model.getCaption());
            intent.putExtra("isCommentAllowed", model.getAllowComment());
            intent.putExtra("commentCount", model.getCommentCounts());
            intent.putExtra("svs", false);
            intent.putExtra("isAd", false);
            context.startActivity(intent);
        });


        holder.layout_share.setOnClickListener(v->{
            final String appPackageName = context.getPackageName();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Checkout this awesome app");
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
            context.startActivity(Intent.createChooser(intent, "Share link"));
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotificationSocialFeedActivity.class);// open followers in profile activity
                intent.putExtra("userId", model.getUserId());
                intent.putExtra("postId", model.getId());
                context.startActivity(intent);
            }
        });

        holder.img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherUserAccount.class);// open followers in profile activity
                intent.putExtra("userId", model.getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class HashtagHolder extends RecyclerView.ViewHolder {

        ImageView imageView,img_profile;
        TextView name,name2,follow;
        EmojiconTextView  tvAbout;
        TextView txt_liked_count;
        LinearLayout layout_share,layout_commnet;

        public HashtagHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.vhc_image);
            img_profile = itemView.findViewById(R.id.img_profile);
            name = itemView.findViewById(R.id.name);
            name2 = itemView.findViewById(R.id.name2);
            tvAbout = itemView.findViewById(R.id.tvAbout);
            follow = itemView.findViewById(R.id.follow);
            txt_liked_count = itemView.findViewById(R.id.txt_liked_count);
            layout_share = itemView.findViewById(R.id.layout_share);
            layout_commnet = itemView.findViewById(R.id.layout_commnet);
        }
    }
}
