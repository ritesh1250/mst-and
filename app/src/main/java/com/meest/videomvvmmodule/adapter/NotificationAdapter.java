package com.meest.videomvvmmodule.adapter;


import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.databinding.ItemNotificationBinding;
import com.meest.videomvvmmodule.model.notification.Notification;
import com.meest.videomvvmmodule.view.search.FetchUserActivity;
import com.meest.videomvvmmodule.view.video.PlayerActivity;
import com.meest.videomvvmmodule.viewmodel.NotificationViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
    private List<Notification.Data> dataList = new ArrayList<>();
    private OnRecyclerViewItemClick onRecyclerViewItemClick;

    public List<Notification.Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Notification.Data> dataList) {
        this.dataList = dataList;
    }

    public OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.setModel(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateData(List<Notification.Data> list) {
        dataList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<Notification.Data> data) {
        for (int i = 0; i < data.size(); i++) {
            dataList.add(data.get(i));
            notifyItemInserted(dataList.size() - 1);
        }
    }

    public interface OnRecyclerViewItemClick {
        void onItemClick(Notification.Data data);
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationBinding binding;

        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(Notification.Data notificationModel) {
            binding.setNotification(notificationModel);
//            if(notificationModel.getNotificationType().equals("1")){
//                binding.btnFollow.setVisibility(View.GONE);
//                binding.cvImage.setVisibility(View.VISIBLE);
//                binding.ivIconLikecomment.setBackgroundResource(R.drawable.like_notification);
//            }else if(notificationModel.getNotificationType().equals("2")){
//                binding.btnFollow.setVisibility(View.GONE);
//                binding.cvImage.setVisibility(View.VISIBLE);
//                binding.ivIconLikecomment.setBackgroundResource(R.drawable.comment_notification);
//            }else {
//                binding.btnFollow.setVisibility(View.VISIBLE);
//                binding.cvImage.setVisibility(View.GONE);
//                binding.ivIconLikecomment.setVisibility(View.GONE);
//            }
            String url = notificationModel.getPostVideo();
            Glide.with(binding.getRoot().getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_edit_profile_img)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.ivThumbnail);
            binding.btnUnfollow.setOnClickListener(v -> {
                final Dialog dialog = new Dialog(binding.getRoot().getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_unfollow_layout);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ImageView imageView = dialog.findViewById(R.id.unFollowImage);
                TextView tvNamePeople = dialog.findViewById(R.id.tvNamePeople);
                TextView tvYes = dialog.findViewById(R.id.tvYes);
                TextView tvNo = dialog.findViewById(R.id.tvNo);
                tvNamePeople.setText(notificationModel.getUserName());
                Glide.with(binding.getRoot().getContext()).load(notificationModel.getUserProfile()).into(imageView);
                dialog.show();
                tvYes.setOnClickListener(v12 -> {
                    new NotificationViewModel().followUnfollow(notificationModel, binding);
                    dialog.dismiss();
                });
                tvNo.setOnClickListener(v1 -> dialog.dismiss());

            });
            binding.btnFollow.setOnClickListener(v -> new NotificationViewModel().followUnfollow(notificationModel, binding));
            binding.parentCard.setOnClickListener(v -> {
//                SharedPrefreances.setSharedPreferenceString(binding.getRoot().getContext(), SharedPrefreances.GET_NOTIFICATION_FRAG_INTENT_VIDEO, notificationModel.getItemId());
//                Utilss.postId = notificationModel.getItemId();
//                ((Activity) binding.getRoot().getContext()).onBackPressed();
                Intent intent = new Intent(binding.getRoot().getContext(), PlayerActivity.class);
//                intent.putExtra("video_list", new Gson().toJson(mList));
//                List<Video.Data> singleList = new ArrayList<>();
//                singleList.add(mList.get(position));
                intent.putExtra("postId", notificationModel.getItemId());

                intent.putExtra("isSingleVideo", true);

                if (notificationModel.getNotificationType().equals("1") ||
                        notificationModel.getNotificationType().equals("2") ||
                        notificationModel.getNotificationType().equals("4") ||
                        notificationModel.getNotificationType().equals("5") ||
                        notificationModel.getNotificationType().equals("6") ||
                        notificationModel.getNotificationType().equals("7"))
                    binding.getRoot().getContext().startActivity(intent);
            });
            binding.profileCard.setOnClickListener(v -> {
                Intent intent = new Intent(binding.getRoot().getContext(), FetchUserActivity.class);
                intent.putExtra("userid", notificationModel.getSenderUserId());
                binding.getRoot().getContext().startActivity(intent);
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
