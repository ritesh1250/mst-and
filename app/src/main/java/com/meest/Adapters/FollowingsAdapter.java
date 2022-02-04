package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.FollowerListResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowingsAdapter   extends RecyclerView.Adapter<FollowingsAdapter.ViewHolder> {

    ArrayList<FollowerListResponse.Following> productList;

    Context context;
    boolean one_time = true;
    FollowingsAdapter.OnItemClickListener mItemClickListener;
    int selectedPosition = -1;


    public FollowingsAdapter(Context context, ArrayList<FollowerListResponse.Following> productList) {
        this.context = context;
        this.productList = productList;

    }


    @NonNull
    @Override
    public FollowingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.followings_adapter, null);
        FollowingsAdapter.ViewHolder viewHolder = new FollowingsAdapter.ViewHolder(itemLayoutView);
        if (!productList.get(i).getDisplayPicture().toString().isEmpty()){
            Glide.with(context).load(productList.get(i).getDisplayPicture().toString()).into(viewHolder.img_pro);
        }


        viewHolder.txt_name1.setText(productList.get(i).getUsername());
        viewHolder.txt_sub_name.setText(productList.get(i).getFirstName() + " " + productList.get(i).getLastName());


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingsAdapter.ViewHolder holder, final int position) {


    }


    public void setOnItemClickListener(final FollowingsAdapter.OnItemClickListener mItemClickListener) {
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

        CircleImageView img_pro;
        private TextView txt_name1,txt_sub_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_pro = itemView.findViewById(R.id.img_pro);
            txt_name1 = itemView.findViewById(R.id.txt_name1);
            txt_sub_name = itemView.findViewById(R.id.txt_sub_name);
        }


    }

    public void updateList(ArrayList<FollowerListResponse.Following> list){
        productList = list;
        notifyDataSetChanged();
    }

}

