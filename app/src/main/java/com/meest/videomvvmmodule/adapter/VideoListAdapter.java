package com.meest.videomvvmmodule.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemSearchVideosBinding;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.HashTagVideoViewHolder> {
    private List<Video.Data> mList = new ArrayList<>();
    private boolean isHashTag = false;
    private String word = "";

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isHashTag() {
        return isHashTag;
    }

    public void setHashTag(boolean hashTag) {
        isHashTag = hashTag;
    }

    public List<Video.Data> getmList() {
        return mList;
    }

    public void setmList(List<Video.Data> mList) {
        this.mList = mList;
    }

    OnRecyclerViewItemClick onRecyclerViewItemClick;

    public VideoListAdapter(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    @NonNull
    @Override
    public HashTagVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_videos, parent, false);
        return new HashTagVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HashTagVideoViewHolder holder, int position) {
        holder.setModel(position);
    }

    public interface OnRecyclerViewItemClick {

        void OnItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(List<Video.Data> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<Video.Data> data) {
        for (int i = 0; i < data.size(); i++) {
            mList.add(data.get(i));
            notifyItemInserted(mList.size() - 1);
        }
    }

    class HashTagVideoViewHolder extends RecyclerView.ViewHolder {
        ItemSearchVideosBinding binding;

        HashTagVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }

        public void setModel(int position) {
            binding.setModel(mList.get(position));
            binding.tvLikeCount.setText(Global.prettyCount(Integer.parseInt(mList.get(position).getPostLikesCount())));
            binding.getRoot().setOnClickListener(v -> {
                onRecyclerViewItemClick.OnItemClick(position);
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
