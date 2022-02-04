package com.meest.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.model.SeeMoreItem;
import com.meest.R;

import java.util.ArrayList;

public class SeeMoreAdapter extends RecyclerView.Adapter<SeeMoreAdapter.SeeMoreViewHolder> {
    private ArrayList<SeeMoreItem> SeeMoreList;

    @NonNull
    @Override
    public SeeMoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.seemore_item_list,parent,false);
        SeeMoreViewHolder SVH = new SeeMoreViewHolder(v);
        return SVH;
    }

    @Override
    public void onBindViewHolder(@NonNull SeeMoreViewHolder holder, int position) {
        SeeMoreItem seemore = SeeMoreList.get(position);
        holder.seemoretxt1.setText(seemore.getTxt1());
        holder.seemoretxt2.setText(seemore.getTxt2());

    }

    @Override
    public int getItemCount() {
        return SeeMoreList.size();
    }

    public class SeeMoreViewHolder extends RecyclerView.ViewHolder{
        public TextView seemoretxt1;
        public TextView seemoretxt2;


        public SeeMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            seemoretxt1 = (TextView)itemView.findViewById(R.id.txt1_seemore);
            seemoretxt2 = (TextView)itemView.findViewById(R.id.txt2_seemore);
        }
    }
    public SeeMoreAdapter(ArrayList<SeeMoreItem> seemorelist){
        SeeMoreList = seemorelist;
    }

}
