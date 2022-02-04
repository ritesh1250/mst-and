package com.meest.social.socialViewModel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ActivityAdapterBinding;
import com.meest.responses.UserActivityResponse;
import com.meest.social.socialViewModel.model.MediaPostResponse1;

import java.util.ArrayList;
import java.util.List;

public class UserActivityAdapterviewModel extends RecyclerView.Adapter<UserActivityAdapterviewModel.PeopleViewHolder> {
    List<UserActivityResponse.Row> userResponseList=new ArrayList<>();
    UserActivityAdapterviewModel.OnItemClickListenerUnFollow mOnItemClickListenerUnFollow;

    @NonNull
    @Override
    public UserActivityAdapterviewModel.PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserActivityAdapterviewModel.PeopleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull UserActivityAdapterviewModel.PeopleViewHolder holder, int position) {
        holder.userActivityList(userResponseList.get(position));
    }
    @Override
    public int getItemCount() {
        return userResponseList == null ? 0 : userResponseList.size();
    }

    public void addLoading(List<UserActivityResponse.Row> rows) {
        this.userResponseList.addAll(rows);
        notifyDataSetChanged();
    }
    public void update(List<UserActivityResponse.Row> rows) {
        this.userResponseList.clear();
        this.userResponseList.addAll(rows);
        notifyDataSetChanged();
    }
    public void loadMore(List<UserActivityResponse.Row> data) {
        for (int i = 0; i < data.size(); i++) {
            userResponseList.add(data.get(i));
            notifyItemInserted(data.size() - 1);
        }

    }
    public class PeopleViewHolder extends RecyclerView.ViewHolder {
        ActivityAdapterBinding activitylistBinding;
        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            activitylistBinding= DataBindingUtil.bind(itemView);        }
        public void userActivityList(UserActivityResponse.Row row) {
            activitylistBinding.setMActivityadaptermodel(row);
       }
    }

    public void setOnItemClickListenerUnFollow(final UserActivityAdapterviewModel.OnItemClickListenerUnFollow mOnItemClickListenerUnFollow) {
        this.mOnItemClickListenerUnFollow = mOnItemClickListenerUnFollow;
    }
    public interface OnItemClickListenerUnFollow {
        void onItemClick(int position);
    }
}
