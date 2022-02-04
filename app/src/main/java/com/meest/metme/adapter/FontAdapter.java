package com.meest.metme.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.meest.R;
import com.meest.databinding.FontLayoutBinding;
import com.meest.metme.model.FontResponse;

import java.util.ArrayList;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.ViewHolder> {
    List<FontResponse.FontName> fontNames = new ArrayList<>();
    private FontAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick;
    private int clickPosition;
    private int row_index=-1;
    Context context;
    String secondColor;

    public FontAdapter() {
    }

    public FontAdapter(Context textAppearance,String secondColor) {
        this.context = textAppearance;
        this.secondColor=secondColor;
    }


    @NonNull
    @Override
    public FontAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.font_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FontAdapter.ViewHolder holder, int position) {
        holder.setModel(fontNames.get(position), position, holder);
        Glide.with(context).load(Uri.parse(fontNames.get(position).getFontimage())).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.binding.fontTheme);
        holder.binding.selectTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewItemClick != null) {
                    row_index = holder.getAdapterPosition();
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onRecyclerViewItemClick.onItemClick(fontNames.get(position), position, holder.binding, holder);
                    }
                    notifyDataSetChanged();
                }
            }
        });
        if (row_index == position) {
            holder.binding.selectTheme.setBackgroundResource(R.drawable.selected_theme);
//            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
//            gd.setCornerRadius(10f);
//            holder.binding.chatBg.setBackground(gd);
        } else {
            holder.binding.selectTheme.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return fontNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FontLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(FontResponse.FontName row, int position, FontAdapter.ViewHolder holder) {
//            binding.setRow(row);
            binding.selectTheme.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(row, position, binding, holder));
        }
    }

    public void loadMore(List<FontResponse.FontName> data) {
        for (int i = 0; i < data.size(); i++) {
            fontNames.add(data.get(i));
            notifyItemInserted(fontNames.size() - 1);
        }
    }

    public interface OnRecyclerViewItemClick {
        void onItemClick(FontResponse.FontName data, int position, FontLayoutBinding binding, FontAdapter.ViewHolder holder);
    }

    public FontAdapter.OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(FontAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

}
