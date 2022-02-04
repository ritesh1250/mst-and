package com.meest.meestbhart.register.fragment.username.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.meestbhart.model.AllNotificationResponse;

import java.util.ArrayList;
import java.util.List;

public class UsernameAdapter extends RecyclerView.Adapter<UsernameAdapter.ViewHolder> {
    private final Context mContext;
    private List<String> mData = new ArrayList<>();
    OnItemClickListener mItemClickListener;

    public UsernameAdapter(Context context, List<String> mData, OnItemClickListener mItemClickListener) {
        mContext = context;
        this.mData = mData;
        this.mItemClickListener = mItemClickListener;
    }

    public UsernameAdapter(Context context,OnItemClickListener mItemClickListener) {
        this.mContext = context;
        this.mItemClickListener = mItemClickListener;
    }


    @NonNull
    @Override
    public UsernameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.username_layout, null);
        UsernameAdapter.ViewHolder viewHolder = new UsernameAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final UsernameAdapter.ViewHolder holder, final int position) {
        holder.txt_name.setText(mData.get(position));
        holder.txt_name.setOnClickListener(v -> {
            mItemClickListener.onItemClick(holder.txt_name.getText().toString());
        });


    }
    public void updateData(List<String> list) {
        this.mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;


        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
        }
    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(String name);
    }


}
