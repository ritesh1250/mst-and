package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.utils.FilterType;
import com.meest.Widget.CameraFilterAdapter;

import java.util.List;

public class CameraEffectsAdapter extends RecyclerView.Adapter<CameraEffectsAdapter.ViewHolder> {

    Context context;
    List<FilterType> effectsList;
    CameraFilterAdapter.OnItemClickListener mItemClickListener;

    public CameraEffectsAdapter(Context context, List<FilterType> effectsList) {
        this.context = context;
        this.effectsList = effectsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_effects_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // holder.tvSticker.setText(filterList.get(position).name());
        holder.viewEffectsMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View viewEffectsMain;
        ImageView ivEffects;

        public ViewHolder(View itemView) {
            super(itemView);

            viewEffectsMain = itemView.findViewById(R.id.viewEffectsMain);
            ivEffects = (ImageView) itemView.findViewById(R.id.ivEffects);
        }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        // this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }
}

