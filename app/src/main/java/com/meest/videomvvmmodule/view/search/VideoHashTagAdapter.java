package com.meest.videomvvmmodule.view.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;

/**
 * Created by sanket kumar.
 */
public class VideoHashTagAdapter extends RecyclerView.Adapter<VideoHashTagAdapter.MyViewHolder>{

    public VideoHashTagAdapter() {

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_grid, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position == 0)
            holder.image.setImageResource(R.drawable.placeholder);
        else if (position == 1)
            holder.image.setImageResource(R.drawable.placeholder);
        else if (position == 2)
            holder.image.setImageResource(R.drawable.placeholder);
        else if (position == 4)
            holder.image.setImageResource(R.drawable.placeholder);
        else if (position == 7)
            holder.image.setImageResource(R.drawable.placeholder);
        else if (position == 8)
            holder.image.setImageResource(R.drawable.placeholder);
        else if (position == 9)
            holder.image.setImageResource(R.drawable.placeholder);
    }


    @Override
    public int getItemCount() {
        return 25;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            image = (ImageView) itemView.findViewById(R.id.userImg);
        }
    }
}
