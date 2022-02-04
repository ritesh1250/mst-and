package com.meest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.model.video_item;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.R;
import com.meest.responses.SearchHomeResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.videoViewHolder> {

    private ArrayList<video_item> Video_List;
    ArrayList<SearchHomeResponse.Data.Video>VideoSerachList = null;
    Context mContext;
    VideoAdapter.OnItemClickListener mItemClickListener;
    public VideoAdapter(ArrayList<SearchHomeResponse.Data.Video> videoSerachList, Context mContext) {
        VideoSerachList = videoSerachList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public videoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item_list,parent,false);
        videoViewHolder VVH = new videoViewHolder(v);
        return VVH;

    }

    @Override
    public void onBindViewHolder(@NonNull videoViewHolder holder, int position) {


        SearchHomeResponse.Data.Video  videoserach = VideoSerachList.get(position);

        // video_item current = Video_List.get(position);
//        holder.profile_img.setImageResource(current.getProfile_img());
        // holder.like_counts.setText(current.getLike_count());
//        holder.bg_img.setImageResource(current.getBackgroung_img());

        holder.like_counts.setText(String.valueOf(videoserach.getLikes()));
        holder.tv_profile_name.setText(videoserach.getUser().getUsername());
        Glide.with(mContext).load(videoserach.getUser().getDisplayPicture()).into(holder.profile_img);

//        Glide.with(mContext).load(videoserach.getVideoURL()).asBitmap()
//                .placeholder(R.drawable.img1)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                .into(holder.background_image);

        // Picasso.get().load(videoserach.getThumbnail()).into(holder.background_image);
        Glide.with(mContext)
                .load(videoserach.getThumbnail())
                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
                .into(holder.background_image);

        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), NotificationSocialFeedActivity.class);
                intent.putExtra("postId",videoserach.getId());
                intent.putExtra("userId",videoserach.getUserId());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return VideoSerachList == null ? 0 : VideoSerachList.size();
    }

    public class videoViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView profile_img;
        public ImageView bg_img,background_image;
        public TextView like_counts,tv_profile_name;
        RelativeLayout layout_main;
        public videoViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_img = (CircleImageView)itemView.findViewById(R.id.video_profile_image);
            bg_img = (ImageView) itemView.findViewById(R.id.background_image);
            like_counts=(TextView)itemView.findViewById(R.id.likes_count);
            tv_profile_name =(TextView)itemView.findViewById(R.id.tv_profile_name);
            background_image=(ImageView)itemView.findViewById(R.id.background_image);
            layout_main = itemView.findViewById(R.id.layout_main);

        }
    }

    public void setOnItemClickListener(final VideoAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

}
