package com.meest.videomvvmmodule.adapter;

import android.app.Dialog;
import android.util.Log;
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
import com.meest.databinding.ItemFollowerBinding;
import com.meest.videomvvmmodule.model.follower.Follower;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.viewmodel.FragmentFollowersViewModel;

import java.util.ArrayList;
import java.util.List;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder> {
    private List<Follower.Data> mList = new ArrayList<>();
    private OnRecyclerViewItemClick onRecyclerViewItemClick;
    private String itemType = "";

    public List<Follower.Data> getmList() {
        return mList;
    }

    public void setmList(List<Follower.Data> mList) {
        this.mList = mList;
    }

    public FollowersAdapter(String itemType) {
        this.itemType = itemType;
    }

    public OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    @NonNull
    @Override
    public FollowersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follower, parent, false);
        return new FollowersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersViewHolder holder, int position) {
        holder.setModel(mList.get(position), position);

        holder.binding.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemType.equals("0")) {
                    new FragmentFollowersViewModel().follow(mList.get(position).getFromUserId(), view.getContext(), holder.binding);
                } else {
                    new FragmentFollowersViewModel().follow(mList.get(position).getToUserId(), view.getContext(), holder.binding);
                }
//                Toast.makeText(view.getContext(), mList.get(position).getToUserId(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.binding.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_unfollow_layout);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ImageView imageView = dialog.findViewById(R.id.unFollowImage);
                TextView tvNamePeople = dialog.findViewById(R.id.tvNamePeople);
                TextView tvYes = dialog.findViewById(R.id.tvYes);
                TextView tvNo = dialog.findViewById(R.id.tvNo);
                tvNamePeople.setText(mList.get(position).getFirstName());
                Log.e("position====", mList.get(position).getUserProfile() + "," + position);
                Glide.with(view.getContext()).load(mList.get(position).getUserProfile()).into(imageView);
//                loadMediaImage(imageView,mList.get(position).getUserProfile(),false);
                dialog.show();
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemType.equals("0")) {
                            new FragmentFollowersViewModel().unfollow(mList.get(position).getFromUserId(), view.getContext(), holder.binding,position,itemType);
                        } else {
                            new FragmentFollowersViewModel().unfollow(mList.get(position).getToUserId(), view.getContext(), holder.binding,position,itemType);
                        }
                        dialog.dismiss();
                    }
                });
                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(List<Follower.Data> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<Follower.Data> data) {
        for (int i = 0; i < data.size(); i++) {
            mList.add(data.get(i));
            notifyItemInserted(mList.size() - 1);
        }

    }

    public interface OnRecyclerViewItemClick {
        void onItemClick(Follower.Data data, int i, int position);

    }


    class FollowersViewHolder extends RecyclerView.ViewHolder {
        ItemFollowerBinding binding;

        FollowersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(Follower.Data follower, int position) {
            binding.setFollower(follower);
            if (follower.isFollower() && follower.isFollowing()) {
                binding.btnRemove.setVisibility(View.VISIBLE);
                binding.follow.setVisibility(View.GONE);
            } else if (follower.isFollowing() && follower.getFriend_status().equals("following")) {
                binding.btnRemove.setVisibility(View.VISIBLE);
                binding.follow.setVisibility(View.GONE);
            } else if (follower.isFollower() && follower.getFriend_status().equals("follower")) {
                binding.follow.setVisibility(View.VISIBLE);
                binding.btnRemove.setVisibility(View.GONE);
            } else if (!follower.isFollower() && !follower.isFollowing() && follower.getFriend_status().equals("other")) {
                binding.follow.setVisibility(View.VISIBLE);
                binding.btnRemove.setVisibility(View.GONE);
            }

//            if (Global.followUnfollow.containsKey(follower.getFollowerId())) {
//                if (Global.followUnfollow.get(follower.getFollowerId())) {
//                    binding.follow.setVisibility(View.GONE);
//                    binding.btnRemove.setVisibility(View.VISIBLE);
//                } else {
//                    binding.follow.setVisibility(View.VISIBLE);
//                    binding.btnRemove.setVisibility(View.GONE);
//                }
//            }
            binding.getRoot().setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(follower, 1, position));
        }
    }

    public void removeAt(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());
    }
}
