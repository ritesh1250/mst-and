package com.meest.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.model.DraftItem;
import com.meest.R;

import java.util.ArrayList;

public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.DraftViewHolder> {

    private ArrayList<DraftItem> DraftList;


    @NonNull
    @Override
    public DraftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.draft_item_list,parent,false);
        DraftViewHolder DVH = new DraftViewHolder(v);
        return DVH;

    }

    @Override
    public void onBindViewHolder(@NonNull DraftViewHolder holder, int position) {

        DraftItem draft = DraftList.get(position);
        holder.draft_img.setImageResource(draft.getImg_draft());
        holder.draft_txt1.setText(draft.getTxt1draft());
        holder.draft_txt2.setText(draft.getTxt2draft());
        holder.draft_txt3.setText(draft.getTxt3draft());
        holder.draft_txt4.setText(draft.getTxt4draft());



    }

    @Override
    public int getItemCount() {
        return DraftList.size();
    }

    public class DraftViewHolder extends RecyclerView.ViewHolder{
        public ImageView draft_img;
        public TextView draft_txt1;
        public TextView draft_txt2;
        public TextView draft_txt3;
        public TextView draft_txt4;

        public DraftViewHolder(@NonNull View itemView) {
            super(itemView);

            draft_img= (ImageView)itemView.findViewById(R.id.img_draft_profile);
            draft_txt1 = (TextView)itemView.findViewById(R.id.txt_postname);
            draft_txt2 = (TextView)itemView.findViewById(R.id.txt2);
            draft_txt3 = (TextView)itemView.findViewById(R.id.txt3);
            draft_txt4 = (TextView)itemView.findViewById(R.id.txt4);

        }
    }
    public DraftAdapter(ArrayList<DraftItem> draftlist){
        DraftList= draftlist;
    }

}
