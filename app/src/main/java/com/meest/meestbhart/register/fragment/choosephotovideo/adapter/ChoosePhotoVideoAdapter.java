package com.meest.meestbhart.register.fragment.choosephotovideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.meestbhart.register.fragment.choosephotovideo.model.ChooseVideoPhotoResponse;

import java.util.ArrayList;

public class ChoosePhotoVideoAdapter extends RecyclerView.Adapter<ChoosePhotoVideoAdapter.ViewHolder> {

    private final static int sBtnCloseSize = 16;
    private final static int sBtnCloseLeftMargin = 5;
    private final Context mContext;
    private ArrayList<ChooseVideoPhotoResponse.Row> mData = new ArrayList<>();
    ChoosePhotoVideoAdapter.OnItemClickListener mItemClickListener;
    ChoosePhotoVideoAdapter.OnItemClickListener mItemClickListener_two;

    public ChoosePhotoVideoAdapter(Context context,ArrayList<ChooseVideoPhotoResponse.Row> mData) {
        mContext = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ChoosePhotoVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_photo_video_activity_adapter, null);
        ChoosePhotoVideoAdapter.ViewHolder viewHolder = new ChoosePhotoVideoAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull final ChoosePhotoVideoAdapter.ViewHolder holder, final int position) {
        /*holder.text_done.setText(mData.get(position).getTopic());


        holder.layout_view.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
              *//*  if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);*//*
                Constant.array_list_.add(mData.get(position).getId());
                holder.layout_view.setBackground(mContext.getDrawable(R.drawable.color_fav));
                holder.text_done.setTextColor(mContext.getColor(R.color.white));
            }
        });*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title,layout_line1,txt_title_new,layout_line1_new;
        ImageView img_2,img_1;
        LinearLayout layut_1,layut_2;
        public ViewHolder(View itemView) {
            super(itemView);

            txt_title = (TextView)itemView.findViewById(R.id.txt_title);
            layout_line1 = (TextView)itemView.findViewById(R.id.layout_line1);

            txt_title_new = (TextView)itemView.findViewById(R.id.txt_title_new);
            layout_line1_new = (TextView)itemView.findViewById(R.id.layout_line1_new);

            layut_1 = (LinearLayout)itemView.findViewById(R.id.layut_1);
            layut_2 = (LinearLayout)itemView.findViewById(R.id.layut_2);

            img_1 = (ImageView)itemView.findViewById(R.id.img_1);
            img_2 = (ImageView)itemView.findViewById(R.id.img_2);

        }


    }


    public void setOnItemClickListener(final ChoosePhotoVideoAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }


}
