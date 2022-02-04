package com.meest.metme.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.GradientThemeLayoutBinding;
import com.meest.metme.model.GradientColor;

import java.util.ArrayList;
import java.util.List;

public class GradientWallpaperAdapter extends RecyclerView.Adapter<GradientWallpaperAdapter.ViewHolder> {
    List<GradientColor.Data> solidColorList = new ArrayList<>();
    private GradientWallpaperAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick;
    private int clickPosition;
    private int row_index;

    @NonNull
    @Override
    public GradientWallpaperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gradient_theme_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradientWallpaperAdapter.ViewHolder holder, int position) {
        holder.setModel(solidColorList.get(position), position, holder);
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(solidColorList.get(position).getFirstcolor()), Color.parseColor(solidColorList.get(position).getSecondcolor()),Color.parseColor(solidColorList.get(position).getSecondcolor())});
        gd.setCornerRadius(0f);
        holder.binding.chatBg.setBackground(gd);
    }

    @Override
    public int getItemCount() {
        return solidColorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        GradientThemeLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(GradientColor.Data row, int position, GradientWallpaperAdapter.ViewHolder holder) {
            binding.setRow(row);
            binding.selectTheme.setOnClickListener(v -> onRecyclerViewItemClick.onGradientClick(row, position, binding, holder));
        }
    }

    public void loadMore(List<GradientColor.Data> data) {
        for (int i = 0; i < data.size(); i++) {
            solidColorList.add(data.get(i));
            notifyItemInserted(solidColorList.size() - 1);
        }
    }

    public interface OnRecyclerViewItemClick {
        void onGradientClick(GradientColor.Data data, int position, GradientThemeLayoutBinding binding, GradientWallpaperAdapter.ViewHolder holder);
    }

    public GradientWallpaperAdapter.OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(GradientWallpaperAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

}
