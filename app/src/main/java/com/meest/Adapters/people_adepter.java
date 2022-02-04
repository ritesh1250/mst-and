package com.meest.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.model.people_item;
import com.meest.R;

import java.util.ArrayList;

public class people_adepter extends RecyclerView.Adapter<people_adepter.peopleViewHolder> {

    private ArrayList<people_item> People_List;


    @NonNull
    @Override
    public peopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item_list,parent,false);
        peopleViewHolder PVH = new peopleViewHolder(v);
        return PVH;
    }

    @Override
    public void onBindViewHolder(@NonNull peopleViewHolder holder, int position) {
        people_item current = People_List.get(position);
        holder.profile_img.setImageResource(current.getProfile_img());
        holder.name.setText(current.getName());
        holder.name2.setText(current.getName2());
        holder.follow.setText(current.getFollow());


    }

    @Override
    public int getItemCount() {
        return People_List.size();
    }

    public class peopleViewHolder extends RecyclerView.ViewHolder{

        public ImageView profile_img;
        public TextView name;
        public TextView name2;
        public TextView follow;

        public peopleViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_img = (ImageView)itemView.findViewById(R.id.img_profile);
            name = (TextView)itemView.findViewById(R.id.name);
            name2 = (TextView)itemView.findViewById(R.id.name2);
            follow = (TextView)itemView.findViewById(R.id.follow);


        }
    }
    public  people_adepter(ArrayList<people_item> peoplelist){
        People_List = peoplelist;
    }


}
