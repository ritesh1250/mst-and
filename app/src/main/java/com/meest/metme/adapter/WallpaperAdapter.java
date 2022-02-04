package com.meest.metme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.WallpaperGridItemBinding;
import com.meest.metme.model.WallpaperNewModel;

import java.util.ArrayList;
import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder> {
    List<WallpaperNewModel.Row> solidColorList = new ArrayList<>();
    private WallpaperAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick;
    private int clickPosition;
    private int row_index;
    Context context;

    @NonNull
    @Override
    public WallpaperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperAdapter.ViewHolder holder, int position) {
        holder.setModel(solidColorList.get(position), position, holder);
    }

    @Override
    public int getItemCount() {
        return solidColorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WallpaperGridItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(WallpaperNewModel.Row row, int position, WallpaperAdapter.ViewHolder holder) {
            binding.setRow(row);
            binding.selectWallpaper.setOnClickListener(v-> onRecyclerViewItemClick.onWallpaperClick(row, position, binding, holder));
        }
    }

    public void loadMore(List<WallpaperNewModel.Row> data) {
        for (int i = 0; i < data.size(); i++) {
            solidColorList.add(data.get(i));
            notifyItemInserted(solidColorList.size() - 1);
        }
    }
    public interface OnRecyclerViewItemClick {
        void onWallpaperClick(WallpaperNewModel.Row data, int position, WallpaperGridItemBinding binding, WallpaperAdapter.ViewHolder holder);
    }

    public WallpaperAdapter.OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(WallpaperAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

}
