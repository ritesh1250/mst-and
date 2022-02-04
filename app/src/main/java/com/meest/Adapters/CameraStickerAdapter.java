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

public class CameraStickerAdapter  extends RecyclerView.Adapter<CameraStickerAdapter.ViewHolder> {

    Context context;
    List<FilterType> stickerList;
    CameraFilterAdapter.OnItemClickListener mItemClickListener;

    public CameraStickerAdapter(Context context, List<FilterType> stickerList) {
        this.context = context;
        this.stickerList = stickerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sticker_item, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.viewStickMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {

        return 6;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View viewStickMain;

        ImageView ivSticker;

        public ViewHolder(View itemView) {
            super(itemView);

            viewStickMain = itemView.findViewById(R.id.viewStickMain);
            ivSticker = (ImageView) itemView.findViewById(R.id.ivSticker);


        }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        //  this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }
}
