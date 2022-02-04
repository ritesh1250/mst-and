package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;

import java.util.ArrayList;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.ViewHolder> {

    ArrayList<String> productList;

    Context context;
    boolean one_time = true;
    DiscoverAdapter.OnItemClickListener mItemClickListener;
    int selectedPosition = -1;



    public DiscoverAdapter(Context context,ArrayList<String> productList) {
        this.context = context;
        this.productList = productList;

    }


    @NonNull
    @Override
    public DiscoverAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_adapter, null);
        DiscoverAdapter.ViewHolder viewHolder = new DiscoverAdapter.ViewHolder(itemLayoutView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverAdapter.ViewHolder holder, final int position) {


    }


    public void setOnItemClickListener(final DiscoverAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }
    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }


    }


}

