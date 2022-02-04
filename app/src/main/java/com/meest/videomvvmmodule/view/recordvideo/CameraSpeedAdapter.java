package com.meest.videomvvmmodule.view.recordvideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;

import java.util.List;

public class CameraSpeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    int selected_position = 1;
    List<String> speedList;
    OnItemClickListener mItemClickListener;

    public CameraSpeedAdapter(Context context, List<String> speedList) {
        this.context = context;
        this.speedList = speedList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_camera_speed_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvName.setText(speedList.get(position));
        if (selected_position == position) {
            viewHolder.tvName.setBackground(context.getDrawable(R.drawable.dr_speed_rv_item_bg));
        } else {
            viewHolder.tvName.setBackground(null);
        }

        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_position = position;
                notifyDataSetChanged();
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return speedList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
        }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }
}
