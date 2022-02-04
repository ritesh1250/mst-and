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
import com.meest.R;
import com.meest.responses.UserFollowerStoryResponse;

import java.util.List;

public class ChatStoryViewerListAdapter extends RecyclerView.Adapter<ChatStoryViewerListAdapter.CustomViewHolder> {
    private List<UserFollowerStoryResponse.StoryViewer> list;
    private Context context;

    public ChatStoryViewerListAdapter(Context context,List<UserFollowerStoryResponse.StoryViewer> list) {
        this.context = context;
        this.list = list;
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
        UserFollowerStoryResponse.StoryViewer data = list.get(position);
        holder.tvName.setText(data.getUser().getFirstName() + " " + list.get(position).getUser().getLastName());
        holder.tvTime.setText(data.getCreatedAt());
        if (data.getUser().getDisplayPicture()!=null){
            Glide.with(context).load(data.getUser().getDisplayPicture()).placeholder(R.drawable.chat_user_placeholder).into(holder.image_view);
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
