package com.meest.Activities.StoryPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.Interfaces.FeedDataCallback;
import com.meest.R;
import com.meest.responses.UserFollowerStoryResponse;

import java.util.List;

public class ChatStoryListAdapter extends RecyclerView.Adapter<ChatStoryListAdapter.CustomViewHolder> {
    private List<UserFollowerStoryResponse.Row> list;
    private Context context;
    private FeedDataCallback feedDataCallback;

    public ChatStoryListAdapter(Context context, List<UserFollowerStoryResponse.Row> list, FeedDataCallback feedDataCallback) {
        this.context = context;
        this.list = list;
        this.feedDataCallback = feedDataCallback;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_story_item, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position, @NonNull List payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        UserFollowerStoryResponse.Row data = list.get(position);
        holder.tvName.setText(data.getUser().getFirstName() + " " + list.get(position).getUser().getLastName());
        if (data.getAllStories().size() > 0)
            holder.tvTime.setText(data.getAllStories().get(0).getCreatedAt());
        if (data.getAllStories().size()>0 && data.getAllStories().get(0).getImage()==true){
            Glide.with(context).load(list.get(position).getAllStories().get(0).getStory()).placeholder(R.drawable.chat_user_placeholder).into(holder.image_view);
        }else {
            Glide.with(context).load(list.get(position).getUser().getDisplayPicture()).placeholder(R.drawable.chat_user_placeholder).into(holder.image_view);
        }

        holder.topLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedDataCallback.storyClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mainLayout, topLayout;
        TextView tvName, tvTime;
        ImageView image_view;

        public CustomViewHolder(View itemView) {
            super(itemView);
            topLayout = (LinearLayout) itemView.findViewById(R.id.topLayout);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.mainLayout);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            image_view = (ImageView) itemView.findViewById(R.id.image_view);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
}
