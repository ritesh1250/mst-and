package com.meest.Widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.utils.FilterType;

import java.util.List;

public class CameraFilterAdapter extends RecyclerView.Adapter<CameraFilterAdapter.ViewHolder> {

    Context context;
    List<FilterType> filterList;
    OnItemClickListener mItemClickListener;
    int[] filterImgs = {R.drawable.ic_filter_1,
            R.drawable.ic_filter_2,
            R.drawable.ic_filter_3,
            R.drawable.ic_filter_4,
            R.drawable.ic_filter_5,
            R.drawable.ic_filter_6,
            R.drawable.ic_filter_7,
            R.drawable.ic_filter_8,
            R.drawable.ic_filter_9,
            R.drawable.ic_filter_10,
            R.drawable.ic_filter_11,
            R.drawable.ic_filter_12,
            R.drawable.ic_filter_13,
            R.drawable.ic_filter_14,
            R.drawable.ic_filter_15,
            R.drawable.ic_filter_16,
            R.drawable.ic_filter_17,
            R.drawable.ic_filter_18};

    public CameraFilterAdapter(Context context, List<FilterType> filterList) {
        this.context = context;
        this.filterList = filterList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_filter_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tvFilter.setText(filterList.get(position).name());
        holder.ivFilter.setImageResource(filterImgs[position]);
        holder.viewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View viewMain;
        TextView tvFilter;
        ImageView ivFilter;

        public ViewHolder(View itemView) {
            super(itemView);

            viewMain = itemView.findViewById(R.id.viewMain);
            ivFilter = (ImageView) itemView.findViewById(R.id.ivFilter);
            tvFilter = (TextView) itemView.findViewById(R.id.tvFilter);

        }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }
}
