package com.meest.metme.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.metme.MetMeGallery;
import com.meest.responses.GetDocsMediaChatsResponse;
import com.meest.metme.ChatImageViewActivity;
import com.meest.metme.encription.AESHelper;

public class MediaLinkDocAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    GetDocsMediaChatsResponse.Data response;
    String fullName;
    String chatHeadId;
    String secondColor;


    public MediaLinkDocAdapter(GetDocsMediaChatsResponse.Data response, String fullName, Context context, String secondColor, String chatHeadId) {
        this.response = response;
        this.context = context;
        this.fullName = fullName;
        this.secondColor = secondColor;
        this.chatHeadId = chatHeadId;
    }

    @Override
    public int getItemCount() {
        try {
            return response.getAll().size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v2 = inflater.inflate(R.layout.row_media_link_doc, parent, false);
        viewHolder = new DoctumentViewHolder(v2);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        DoctumentViewHolder holder = (DoctumentViewHolder) viewHolder;
        String fileUrl=AESHelper.msgDecrypt(response.getAll().get(position).getFileURL(),response.getAll().get(position).getChatHeadId());
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatImageViewActivity.class);
                intent.putExtra("title", fullName);
                intent.putExtra("iv_url", fileUrl);
                intent.putExtra("type", response.getAll().get(position).getAttachmentType());
                intent.putExtra("dateAndTime", response.getAll().get(position).getCreatedAt());
                intent.putExtra("username",fullName);
                intent.putExtra("messageId", response.getAll().get(position).getId());
                intent.putExtra("chatHeadId", chatHeadId);
                intent.putExtra("secondColor", secondColor);
                intent.putExtra("whereFrom","allMedia");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        if(response.getAll().get(position).getAttachmentType().equals("Video")){
            holder.iv_video_play.setVisibility(View.VISIBLE);
        }else {
            holder.iv_video_play.setVisibility(View.GONE);
        }

        Glide.with(context).
                load(fileUrl)
                .placeholder(R.drawable.ic_bg_theme_profile)
                .into(holder.image);
    }

    public class DoctumentViewHolder extends RecyclerView.ViewHolder {

        public ImageView image,iv_video_play;
        LinearLayout mainLayout;

        public DoctumentViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            iv_video_play = itemView.findViewById(R.id.iv_video_play);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

}



