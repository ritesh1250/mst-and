package com.meest.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.model.DuplicateItem;
import com.meest.R;

import java.util.ArrayList;

public class Duplicate_Adapter extends RecyclerView.Adapter<Duplicate_Adapter.DuplicateViewHolder> {
    private ArrayList<DuplicateItem> DuplicateList;


    @NonNull
    @Override
    public DuplicateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.duplicate_item_list,parent,false);
        DuplicateViewHolder DVH = new DuplicateViewHolder(v);
        return DVH;
    }

    @Override
    public void onBindViewHolder(@NonNull DuplicateViewHolder holder, int position) {



        DuplicateItem duplicate = DuplicateList.get(position);
        holder.duplicate_img.setImageResource(duplicate.getImg_duplicate());
        holder.duplicate_txt1.setText(duplicate.getTxt1_duplicate());
        holder.duplicate_txt2.setText(duplicate.getTxt2_duplicate());
        holder.duplicate_txt3.setText(duplicate.getTxt3_duplicate());
        holder.duplicate_txt4.setText(duplicate.getTxt4_duplicate());


    }

    @Override
    public int getItemCount() {
        return DuplicateList.size();
    }

    public class DuplicateViewHolder extends RecyclerView.ViewHolder{
        public ImageView duplicate_img;
        public TextView duplicate_txt1;
        public TextView duplicate_txt2;
        public TextView duplicate_txt3;
        public TextView duplicate_txt4;

        public DuplicateViewHolder(@NonNull View itemView) {
            super(itemView);

            duplicate_img= (ImageView)itemView.findViewById(R.id.img_duplicate_profile);
            duplicate_txt1 = (TextView)itemView.findViewById(R.id.txt_postname);
            duplicate_txt2 = (TextView)itemView.findViewById(R.id.txt_postEngagement);
            duplicate_txt3 = (TextView)itemView.findViewById(R.id.txt_rs_par_postEngagement);
            duplicate_txt4 = (TextView)itemView.findViewById(R.id.txt_complate);

        }
    }

    public Duplicate_Adapter(ArrayList<DuplicateItem> duplicatelist){
        DuplicateList= duplicatelist;
    }

}
