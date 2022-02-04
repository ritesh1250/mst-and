package com.meest.videomvvmmodule.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.databinding.ItemCommentReplyListBinding;
import com.meest.videomvvmmodule.model.comment.Comment;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.List;

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.CommentsViewHolder> {
    private List<Comment.Data.CommentsReplyData> mList = new ArrayList<>();
    private CommentReplyAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick;

    public CommentReplyAdapter(List<Comment.Data.CommentsReplyData> comments_reply) {
        mList = comments_reply;
    }

    public List<Comment.Data.CommentsReplyData> getmList() {
        return mList;
    }

    public void setmList(List<Comment.Data.CommentsReplyData> mList) {
        this.mList = mList;
    }

    public CommentReplyAdapter.OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(CommentReplyAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    @NonNull
    @Override
    public CommentReplyAdapter.CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_reply_list, parent, false);
        return new CommentReplyAdapter.CommentsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CommentReplyAdapter.CommentsViewHolder holder, int position) {
        holder.setModel(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(List<Comment.Data.CommentsReplyData> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<Comment.Data.CommentsReplyData> data) {
        for (int i = 0; i < data.size(); i++) {
            mList.add(data.get(i));
            notifyItemInserted(mList.size() - 1);
        }
    }

    public interface OnRecyclerViewItemClick {
        void onCommentItemClick(Comment.Data.CommentsReplyData data, int position, int type, ItemCommentReplyListBinding binding);
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {
        ItemCommentReplyListBinding binding;

        CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(Comment.Data.CommentsReplyData comment, int position) {
            binding.setComment(comment);
            String str = comment.getComment().replace("@"+comment.getFirstName(),"");
           /* Glide.with(binding.getRoot().getContext()).load(mList.get(position).getUserProfile())
                    .placeholder(R.drawable.ic_edit_profile_img)
                    .into(binding.imgProfile);*/
            binding.tvComment.setText(str);
            if (!TextUtils.isEmpty(Global.userId) && Global.userId.equals(comment.getUserId())) {
                binding.imgDelete.setVisibility(View.GONE);
            }

//            binding.imgDelete.setOnClickListener(v -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 1,binding));
            binding.tvUsername.setOnClickListener(view -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 2, binding));
            binding.imgProfile.setOnClickListener(view -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 2, binding));
//            binding.tvReply.setOnClickListener(view -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 3, binding));
        }
    }
}
