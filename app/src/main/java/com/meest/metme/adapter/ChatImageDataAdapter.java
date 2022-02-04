package com.meest.metme.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
//import com.meest.Adapters.MessageAdapter;
import com.meest.R;
import com.meest.responses.GetDocsMediaChatsResponse;
import com.meest.metme.ChatImageViewActivity;
import com.meest.metme.encription.AESHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatImageDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    DateFormat formatter = new SimpleDateFormat("MMMM"); //If you need time just put specific format for time like 'HH:mm:ss'
    ArrayList<Integer> keyDate=new ArrayList<>();
    Map<Integer, ArrayList<GetDocsMediaChatsResponse.Data.LastWeek>> consolidatedList1 = new HashMap<>();
    String months[] = {"January", "February", "March", "April",
            "May", "June", "July", "August", "September",
            "October", "November", "December","Last Month","Last Week"};
    String name;
    OnItemClickListener mItemClickListener;

    public ChatImageDataAdapter(Map<Integer, ArrayList<GetDocsMediaChatsResponse.Data.LastWeek>> m1, Context context,String name) {
        this.consolidatedList1 = m1;
        this.context = context;
        this.name = name;
        keyDate = new ArrayList<>(m1.keySet());
    }

    @Override
    public int getItemCount() {
        return consolidatedList1 != null ? consolidatedList1.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v2 = inflater.inflate(R.layout.date_chat_media_item, parent, false);
        viewHolder = new DateViewHolder(v2);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;

        Log.e("data","data value: "+keyDate.get(position)+"::"+months[keyDate.get(position)-1]);

        dateViewHolder.textView.setText(months[keyDate.get(position)-1]);
//        GridLayoutManager GM = new GridLayoutManager(dateViewHolder.recycler_document.getContext(), 4);
        StaggeredGridLayoutManager GM = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
//        GM.setInitialPrefetchItemCount(consolidatedList1.get(keyDate.get(position)).size());
        ChildAdapter childRecycler = new ChildAdapter(consolidatedList1.get(keyDate.get(position)), context);

        dateViewHolder.recycler_document.setLayoutManager(GM);
        dateViewHolder.recycler_document.setAdapter(childRecycler);
    }

    // ViewHolder for date row item
    class DateViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        RecyclerView recycler_document;

        public DateViewHolder(View v) {
            super(v);
            textView = (TextView) itemView.findViewById(R.id.doctument_date);
            recycler_document = itemView.findViewById(R.id.recycler_document);
        }
    }

    public class ChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<GetDocsMediaChatsResponse.Data.LastWeek> consolidatedList = new ArrayList<>();
        Context context;

        public ChildAdapter(ArrayList<GetDocsMediaChatsResponse.Data.LastWeek> consolidatedList, Context context) {
            this.consolidatedList = consolidatedList;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return consolidatedList != null ? consolidatedList.size() : 0;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View v1 = inflater.inflate(R.layout.docyument_item_list, parent,
                    false);
            viewHolder = new ChildAdapter.GeneralViewHolder(v1);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                     int position) {
            GetDocsMediaChatsResponse.Data.LastWeek data = consolidatedList.get(position);
            String fileUrl = AESHelper.msgDecrypt(data.getFileURL(),data.getChatHeadId());
            ChildAdapter.GeneralViewHolder generalViewHolder = (ChildAdapter.GeneralViewHolder) viewHolder;

            generalViewHolder.rlMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatImageViewActivity.class);
                    intent.putExtra("title", name);
                    intent.putExtra("iv_url", fileUrl);
                    intent.putExtra("type", data.getAttachmentType());
                    intent.putExtra("dateAndTime", data.getCreatedAt());
                    intent.putExtra("username",name);
                    intent.putExtra("messageId", data.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            if(data.getAttachmentType().equals("Video")){
                generalViewHolder.iv_video_play.setVisibility(View.VISIBLE);
            }else {
                generalViewHolder.iv_video_play.setVisibility(View.GONE);
            }

            Glide.with(context).load(fileUrl).into(generalViewHolder.iv_document_images);
     /*       generalViewHolder.iv_document_images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null)
                        mItemClickListener.onItemClick(data);
                }
            });*/
        }

        // View holder for general row item
        class GeneralViewHolder extends RecyclerView.ViewHolder {
                public ImageView iv_document_images,iv_video_play;
                public RelativeLayout rlMedia;

                public GeneralViewHolder(View v) {
                super(v);
                iv_document_images = (ImageView) itemView.findViewById(R.id.iv_document_images);
                    iv_video_play = (ImageView) itemView.findViewById(R.id.iv_video_play);
                    rlMedia = (RelativeLayout) itemView.findViewById(R.id.rl_media);
            }
        }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

//        void onItemClick(int position, View view);
        void onItemClick(GetDocsMediaChatsResponse.Data.LastWeek data);
    }

}


