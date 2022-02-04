package com.meest.videomvvmmodule.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemTrendingUserBinding;
import com.meest.videomvvmmodule.model.user.TopCreatorResponse;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.search.FetchUserActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FamousCreatorAdapter extends RecyclerView.Adapter<FamousCreatorAdapter.VideoFullViewHolder> {
    private List<TopCreatorResponse.User> mList = new ArrayList<>();
    //    private OnRecyclerViewItemClick onRecyclerViewItemClick;
//
//    public OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
//        return onRecyclerViewItemClick;
//    }
//
//    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
//        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
//    }
    CompositeDisposable disposable = new CompositeDisposable();

    int count = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<TopCreatorResponse.User> getmList() {
        return mList;
    }

    public void setmList(List<TopCreatorResponse.User> mList) {
        this.mList.clear();
        this.mList.addAll(mList);
    }

    @NonNull
    @Override
    public VideoFullViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trending_user, parent, false);
        return new VideoFullViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoFullViewHolder holder, int position) {
        holder.setModel(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(List<TopCreatorResponse.User> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public interface OnRecyclerViewItemClick {
        void onItemClick(TopCreatorResponse.User model, int position, ItemTrendingUserBinding binding, int type);
    }

    class VideoFullViewHolder extends RecyclerView.ViewHolder {
        ItemTrendingUserBinding binding;

        VideoFullViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(int position) {
            binding.setModel(mList.get(position));
            binding.imgClose.setOnClickListener(v -> removeAt(position));

            binding.btnFollow.setOnClickListener(v -> {
                followUnFollow(mList.get(position).getUserId(), binding);
            });

            binding.btnUnFollow.setOnClickListener(v -> {
                followUnFollow(mList.get(position).getUserId(), binding);
            });
            binding.cardProfile.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), FetchUserActivity.class);
                intent.putExtra("userid", mList.get(position).getUserId());
                itemView.getContext().startActivity(intent);
            });
        }
    }

    public void removeAt(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());
    }

    public void followUnFollow(String userID, ItemTrendingUserBinding binding) {
        disposable.add(Global.initRetrofit().followUnFollow(Global.apikey, Global.userId, userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((followRequest, throwable) -> {
                    if (followRequest != null && followRequest.getStatus() != null) {
                        if (followRequest.getMessage().equalsIgnoreCase("Follow Successfully")) {
                            binding.btnFollow.setVisibility(View.GONE);
                            binding.btnUnFollow.setVisibility(View.VISIBLE);
                            count++;
                            Global.followUnfollow.put(userID, true);
                        } else if (followRequest.getMessage().equals("Unfollow Successfully")) {
                            binding.btnFollow.setVisibility(View.VISIBLE);
                            binding.btnUnFollow.setVisibility(View.GONE);
                            count--;
                            Global.followUnfollow.put(userID, false);
                        }
                    }
                }));
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
