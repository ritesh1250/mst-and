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
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.List;

public class GradientColorAdapter extends RecyclerView.Adapter<GradientColorAdapter.ViewHolder> {
    List<GradientColor.Data> gradientColorList = new ArrayList<>();
    private GradientColorAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick;
    private int clickPosition;
    private int row_index=-1;

    @NonNull
    @Override
    public GradientColorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gradient_theme_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradientColorAdapter.ViewHolder holder, int position) {
        holder.setModel(gradientColorList.get(position), position, holder);
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(gradientColorList.get(position).getFirstcolor()), Color.parseColor(gradientColorList.get(position).getSecondcolor())});
        gd.setCornerRadius(0f);
        holder.binding.chatBg.setBackground(gd);
        holder.binding.selectTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewItemClick != null) {
                    row_index = holder.getAdapterPosition();
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onRecyclerViewItemClick.onItemClick(gradientColorList.get(position), position,holder.binding,holder);
                    }
                    notifyDataSetChanged();
                }
            }
        });
        if (row_index == position) {
            holder.binding.selectTheme.setBackgroundResource(R.drawable.selected_theme);
        } else {
            holder.binding.selectTheme.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return gradientColorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        GradientThemeLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(GradientColor.Data row, int position, GradientColorAdapter.ViewHolder holder) {
            binding.setRow(row);
            binding.selectTheme.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(row, position, binding, holder));
        }
    }

    public void loadMore(List<GradientColor.Data> data) {
        for (int i = 0; i < data.size(); i++) {
            gradientColorList.add(data.get(i));
            notifyItemInserted(gradientColorList.size() - 1);
        }
    }

    public interface OnRecyclerViewItemClick {
        void onItemClick(GradientColor.Data data, int position, GradientThemeLayoutBinding binding, GradientColorAdapter.ViewHolder holder);
    }

    public GradientColorAdapter.OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(GradientColorAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

}
