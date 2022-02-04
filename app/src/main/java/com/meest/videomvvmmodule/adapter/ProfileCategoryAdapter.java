package com.meest.videomvvmmodule.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemProfileCategoryListBinding;
import com.meest.videomvvmmodule.model.user.ProfileCategory;

import java.util.ArrayList;
import java.util.List;

public class ProfileCategoryAdapter extends RecyclerView.Adapter<ProfileCategoryAdapter.ProfileCategoryViewHolder> {
    private List<ProfileCategory.Data> mList = new ArrayList<>();
    private OnRecyclerViewItemClick onRecyclerViewItemClick;

    public OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    @NonNull
    @Override
    public ProfileCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_category_list, parent, false);
        return new ProfileCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileCategoryViewHolder holder, int position) {
        holder.setModel(mList.get(position));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(List<ProfileCategory.Data> list) {
        mList = list;
        notifyDataSetChanged();

    }

    public void loadMore(List<ProfileCategory.Data> data) {
        for (int i = 0; i < data.size(); i++) {
            mList.add(data.get(i));
            notifyItemInserted(mList.size() - 1);
        }

    }

    public interface OnRecyclerViewItemClick {
        void onCategoryClick(ProfileCategory.Data data);
    }


    class ProfileCategoryViewHolder extends RecyclerView.ViewHolder {
        ItemProfileCategoryListBinding binding;


        ProfileCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }

        public void setModel(ProfileCategory.Data data) {
            binding.setModel(data);
            binding.getRoot().setOnClickListener(view -> onRecyclerViewItemClick.onCategoryClick(data));
        }
    }
}
