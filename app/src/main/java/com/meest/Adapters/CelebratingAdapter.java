package com.meest.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.model.Celebrating_item;
import com.meest.R;

import java.util.ArrayList;

public class CelebratingAdapter extends RecyclerView.Adapter<CelebratingAdapter.CelebratingViewHolder> {

    private ArrayList<Celebrating_item> CelebratingList;



    @NonNull
    @Override
    public CelebratingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.celebrating_list_item,parent,false);
        CelebratingViewHolder CVH = new CelebratingViewHolder(v);
        return CVH;
    }

    @Override
    public void onBindViewHolder(@NonNull CelebratingViewHolder holder, int position) {

        Celebrating_item celebrating = CelebratingList.get(position);
        holder.celebrating_name.setText(celebrating.getCelebrating_name());
        holder.celebrating_icon.setImageResource(celebrating.getCelebratingA_img());



    }

    @Override
    public int getItemCount() {
        return CelebratingList.size();
    }

    public class CelebratingViewHolder extends RecyclerView.ViewHolder{
        public ImageView celebrating_icon;
        public TextView celebrating_name;

        public CelebratingViewHolder(@NonNull View itemView) {
            super(itemView);


            celebrating_name = (TextView)itemView.findViewById(R.id.txt_celebrating_activity);
            celebrating_icon= (ImageView)itemView.findViewById(R.id.img_celebrating_activity);

        }
    }
    public CelebratingAdapter(ArrayList<Celebrating_item> celebratinglist){
        CelebratingList= celebratinglist;
    }



}
