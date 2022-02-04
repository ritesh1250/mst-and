package com.meest.videomvvmmodule.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemCommentListBinding;
import com.meest.databinding.ItemCommentReplyListBinding;
import com.meest.videomvvmmodule.model.comment.Comment;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.search.FetchUserActivity;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentsViewHolder> {
    private List<Comment.Data> mList = new ArrayList<>();
    private OnRecyclerViewItemClick onRecyclerViewItemClick;

    public List<Comment.Data> getmList() {
        return mList;
    }

    CustomDialogBuilder customDialogBuilder;

    public void setmList(List<Comment.Data> mList) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_list, parent, false);
        customDialogBuilder = new CustomDialogBuilder(parent.getContext());
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

    public void updateData(List<Comment.Data> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<Comment.Data> data) {
        for (int i = 0; i < data.size(); i++) {
            mList.add(data.get(i));
            notifyItemInserted(mList.size() - 1);
        }
    }

    public interface OnRecyclerViewItemClick {
        void onCommentItemClick(Comment.Data data, int position, int type, ItemCommentListBinding binding);
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {
        ItemCommentListBinding binding;

        CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(Comment.Data comment, int position) {
            binding.setComment(comment);
            CommentReplyAdapter replyadapter = new CommentReplyAdapter(comment.getComments_reply());
//            replyadapter.updateData(comment.getComments_reply());
     /*       Glide.with(binding.getRoot().getContext()).load(mList.get(position).getUserProfile())
                    .placeholder(R.drawable.ic_edit_profile_img)
                    .into(binding.imgProfile);*/
            binding.replyRecyclerview.setAdapter(replyadapter);

            replyadapter.setOnRecyclerViewItemClick((Comment.Data.CommentsReplyData comment2, int position2, int type, ItemCommentReplyListBinding binding) -> {
                switch (type) {
                    // On delete click
                    case 1:
//                        viewModel.callApitoDeleteComment(comment.getCommentsId(), position, comment);
                        break;
                    // On user Profile Click
                    case 2:
//                        Intent intent = new Intent(get, FetchUserActivity.class);
//                        intent.putExtra("userid", comment.getUserId());
//                        startActivity(intent);
                        break;

                    case 3:
//                        viewModel.commentId = comment.getCommentsId();
//                        commentSheetBinding.etComment.setText("@" + comment.getFirstName() + " ");
//                        commentSheetBinding.etComment.setSelection(commentSheetBinding.etComment.length());
//                        commentSheetBinding.etComment.requestFocus();
                        break;

                    case 4:
//                        viewModel.likeUnlikePost(comment.getCommentsId(), comment, binding);
                        break;
                    default:
                        break;
                }
            });

            if (!TextUtils.isEmpty(Global.userId) && Global.userId.equals(comment.getUserId())) {
                binding.imgDelete.setVisibility(View.VISIBLE);
            }
            if (!comment.getUserId().equals(Global.userId)) {
                binding.imgDelete.setVisibility(View.INVISIBLE);
            }
            binding.imgDelete.setOnClickListener(v -> {
                customDialogBuilder.showSimpleDialogMeest(itemView.getContext().getString(R.string.areSure), itemView.getContext().getString(R.string.do_you_comment),
                        itemView.getContext().getString(R.string.no), itemView.getContext().getString(R.string.yes), new CustomDialogBuilder.OnDismissListener() {
                            @Override
                            public void onPositiveDismiss() {
                                onRecyclerViewItemClick.onCommentItemClick(comment, position, 1, binding);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });

            });
            binding.tvUsername.setOnClickListener(view -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 2, binding));
            binding.imgProfile.setOnClickListener(view -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 2, binding));
            binding.tvReply.setOnClickListener(view -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 3, binding));

            binding.likebtn.setOnClickListener(view -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 4, binding));

            if (mList.get(position).getCommentLike()) {
                binding.likebtn.setImageResource(R.drawable.comment_like);
            } else {
                binding.likebtn.setImageResource(R.drawable.diamond);
            }
          /*  binding.likebtn.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    onRecyclerViewItemClick.onCommentItemClick(comment, position, 4, binding);
//                    mList.get(position).setVideoIsLiked(1);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    onRecyclerViewItemClick.onCommentItemClick(comment, position, 4, binding);
//                    mList.get(position).setVideoIsLiked(0);
                }
            });*/
        }
    }
}
