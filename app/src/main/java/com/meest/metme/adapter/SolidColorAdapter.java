package com.meest.metme.adapter;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ColorThemeLayoutBinding;
import com.meest.metme.model.SolidColor;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.List;

public class SolidColorAdapter extends RecyclerView.Adapter<SolidColorAdapter.ViewHolder> {
    List<SolidColor.Row> solidColorList = new ArrayList<>();
    private SolidColorAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick;
    private int clickPosition;
    private int row_index = -1;

//    public SolidColorAdapter(Context context, List<SolidColor.Row> solidColorList) {
//        this.context = context;
//        this.solidColorList = solidColorList;
//    }

    @NonNull
    @Override
    public SolidColorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_theme_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolidColorAdapter.ViewHolder holder, int position) {
        holder.setModel(solidColorList.get(position), position, holder);

        holder.binding.selectTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewItemClick != null) {
                    row_index = holder.getAdapterPosition();
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onRecyclerViewItemClick.onItemClick(solidColorList.get(position), position, holder.binding, holder);
                    }
                    notifyDataSetChanged();
                }
            }
        });
        Log.e("TAG", "row_index: " + row_index);
        Log.e("TAG", "position: " + position);
        if (row_index == position ) {
            holder.binding.selectTheme.setBackgroundResource(R.drawable.selected_theme);
        } else {
            holder.binding.selectTheme.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return solidColorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ColorThemeLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(SolidColor.Row row, int position, ViewHolder holder) {
            binding.setRow(row);
            binding.selectTheme.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(row, position, binding, holder));
        }
    }

    public void loadMore(List<SolidColor.Row> data) {
        for (int i = 0; i < data.size(); i++) {
            solidColorList.add(data.get(i));
            notifyItemInserted(solidColorList.size() - 1);
        }
    }

    public interface OnRecyclerViewItemClick {
        void onItemClick(SolidColor.Row data, int position, ColorThemeLayoutBinding binding, ViewHolder holder);
    }

    public SolidColorAdapter.OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(SolidColorAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }
}
