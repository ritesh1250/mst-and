package com.meest.videomvvmmodule.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemReplyCommentListBinding;
import com.meest.videomvvmmodule.model.comment.VideoSubCommentResponse;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.List;

public class ReplyCommentAdapter extends RecyclerView.Adapter<ReplyCommentAdapter.CommentsViewHolder> {
    private List<VideoSubCommentResponse.Datum> mList = new ArrayList<>();
    private OnRecyclerViewItemClick onRecyclerViewItemClick;

    public List<VideoSubCommentResponse.Datum> getmList() {
        return mList;
    }

    public void setmList(List<VideoSubCommentResponse.Datum> mList) {
        this.mList = mList;
    }

    public OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply_comment_list, parent, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        holder.setModel(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(List<VideoSubCommentResponse.Datum> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<VideoSubCommentResponse.Datum> data) {
        for (int i = 0; i < data.size(); i++) {
            mList.add(data.get(i));
            notifyItemInserted(mList.size() - 1);
        }
    }

    public interface OnRecyclerViewItemClick {
        void onCommentItemClick(VideoSubCommentResponse.Datum data, int position, int type, ItemReplyCommentListBinding binding);
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {
        ItemReplyCommentListBinding binding;

        CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(VideoSubCommentResponse.Datum comment, int position) {
            binding.setComment(comment);

            binding.txtDelete.setOnClickListener(v -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 1,binding));
            binding.tvUsername.setOnClickListener(view -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 2, binding));
            binding.imgProfile.setOnClickListener(view -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 2, binding));
            binding.txtReport.setOnClickListener(view -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 3, binding));

            if(!TextUtils.isEmpty(Global.userId) && comment.getUserId().equalsIgnoreCase(Global.userId)){
                binding.txtReport.setVisibility(View.GONE);
                binding.txtDelete.setVisibility(View.VISIBLE);
            }else {
                binding.txtReport.setVisibility(View.VISIBLE);
                binding.txtDelete.setVisibility(View.GONE);
            }
        }
    }
}
