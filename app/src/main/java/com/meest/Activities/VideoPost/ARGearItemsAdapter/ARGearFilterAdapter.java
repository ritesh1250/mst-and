package com.meest.Activities.VideoPost.ARGearItemsAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Activities.VideoPost.ARGearAsset.ARGearDataModel;
import com.meest.R;
import com.meest.Widget.CameraFilterAdapter;
import com.bumptech.glide.Glide;

import java.util.List;

public class ARGearFilterAdapter extends RecyclerView.Adapter<ARGearFilterAdapter.FilterHolder> {

    private Context context;
    private List<ARGearDataModel.ARGearItem> stickerList;
    CameraFilterAdapter.OnItemClickListener mItemClickListener;

    public ARGearFilterAdapter(Context context, List<ARGearDataModel.ARGearItem> stickerList) {
        this.context = context;
        this.stickerList = stickerList;
    }

    @NonNull
    @Override
    public FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_filter_item, null);
        return new FilterHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterHolder holder, int position) {
        final ARGearDataModel.ARGearItem model = stickerList.get(position);
        holder.tvFilter.setText(model.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });

        // loading thumbnail
        Glide.with(context).load(model.getThumbnail()).into(holder.filterImage);
    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    public class FilterHolder extends RecyclerView.ViewHolder {
        View viewMain;
        TextView tvFilter;
        ImageView filterImage;

        public FilterHolder(View itemView) {
            super(itemView);
            viewMain = itemView.findViewById(R.id.viewMain);
            filterImage = itemView.findViewById(R.id.ivFilter);
            tvFilter = itemView.findViewById(R.id.tvFilter);
        }
    }

    public void setOnItemClickListener(final CameraFilterAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
