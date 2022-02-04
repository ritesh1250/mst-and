package com.meest.svs.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.responses.VideoSearchResponse;
import com.meest.utils.goLiveUtils.CommonUtils;

import java.util.List;

public class HashtagImageMeeshoAdapter extends RecyclerView.Adapter<HashtagImageMeeshoAdapter.HashtagHolder> {

    private Context context;
    private List<VideoSearchResponse.Row> videosList;
    private boolean isHashTag;
    private boolean isSvs;
    private String trioLink1;

    public HashtagImageMeeshoAdapter(Context context, List<VideoSearchResponse.Row> videosList,
                                     boolean isHashTag, String trioLink1) {
        this.context = context;
        this.isHashTag = isHashTag;
        this.trioLink1 = trioLink1;
        this.videosList = videosList;
    }

    @NonNull
    @Override
    public HashtagImageMeeshoAdapter.HashtagHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_hashtag_child, parent, false);
        return new HashtagImageMeeshoAdapter.HashtagHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HashtagImageMeeshoAdapter.HashtagHolder holder, int position) {
        final VideoSearchResponse.Row model = videosList.get(position);

        CommonUtils.loadImage(holder.imageView,model.getThumbnail(),context,R.drawable.image_placeholder);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSvs){
                }else{
                    Intent intent=new Intent(holder.itemView.getContext(), NotificationSocialFeedActivity.class);
                    intent.putExtra("userId",model.getUserId());
                    intent.putExtra("postId", model.getId());
                    holder.itemView.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    public void notifyFeedsDataSetChanged(List<VideoSearchResponse.Row> videosList) {
//        this.videosList.addAll(videosList);
        notifyItemInserted(videosList.size()-1);
    }

    public void update(List<VideoSearchResponse.Row> rows) {
        this.videosList.clear();
        this.videosList.addAll(rows);
        notifyDataSetChanged();
    }

    public class HashtagHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public HashtagHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.vhc_image);
        }
    }
}
