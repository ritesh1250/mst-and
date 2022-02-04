package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<String> imageModelArrayList;
    
    public ListItemAdapter(Context ctx, ArrayList<String> imageModelArrayList){
        inflater = LayoutInflater.from(ctx);
        this.imageModelArrayList = imageModelArrayList;
    }
    public void removeItem(int position) {
        imageModelArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, imageModelArrayList.size());
    }
    public void restoreItem(String model, int position) {
        imageModelArrayList.add(position, model);
        // notify item added by position
        notifyItemInserted(position);
    }
    @Override
    public ListItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.like_list_adapter, parent, false);
        ListItemAdapter.MyViewHolder holder = new ListItemAdapter.MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ListItemAdapter.MyViewHolder holder, int position) {
        // holder.time.setText(imageModelArrayList.get(position).getName());
    }
    @Override
    public int getItemCount() {
        return imageModelArrayList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        public MyViewHolder(View itemView) {
            super(itemView);
            //time = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}