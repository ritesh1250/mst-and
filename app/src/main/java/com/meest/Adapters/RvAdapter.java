package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.FeedResponse;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder> {
    Context context;
    ArrayList<FeedResponse.UserTags> models;
    Onclick onclick;
    public interface Onclick {
        void onEvent(FeedResponse.UserTags model, int pos);
    }
    public RvAdapter(Context context, ArrayList<FeedResponse.UserTags> models, Onclick onclick) {
        this.context = context;
        this.models = models;
        this.onclick = onclick;
    }
    View view;
    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.tagged_users_items_row, parent, false);
        RvViewHolder rvViewHolder = new RvViewHolder(view);
        return rvViewHolder;
    }
    @Override
    public void onBindViewHolder(RvViewHolder holder, final int position) {
        final FeedResponse.UserTags model = models.get(position);
        if (model.getUsername() != null) {
            holder.itemName.setText(model.getUsername());
        }
        holder.removeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                models.remove(position);
                onclick.onEvent(model,position);
                notifyDataSetChanged();
            }
        });
//        holder.removeImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onclick.onEvent(model,position);
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return models.size();
    }
    public class RvViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView removeImg;
        LinearLayout llItem;
        public RvViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tv_name);
            removeImg = itemView.findViewById(R.id.img_remove);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }
}