package com.meest.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.UserBlockListResponse;
import com.bumptech.glide.Glide;

import java.util.List;

public class BlockAccountAdapter extends RecyclerView.Adapter<BlockAccountAdapter.BlackAccountViewHolder> {

    List<UserBlockListResponse.Row> blackListData;
    BlockAccountAdapter.onItemClickListener onItemClickListener;

    public BlockAccountAdapter(List<UserBlockListResponse.Row> blackListData) {
        this.blackListData = blackListData;
    }

    @NonNull
    @Override
    public BlackAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_account_item_list, parent, false);
        return new BlackAccountViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BlackAccountViewHolder holder, int position) {
        try {
            holder.acc_name.setText(String.format("%s %s", blackListData.get(position).getBlockedUser().getFirstName(), blackListData.get(position).getBlockedUser().getLastName()));
            Glide.with(holder.profile_img.getContext())
                    .load(blackListData.get(position).getBlockedUser().getDisplayPicture())
                    .into(holder.profile_img);
            holder.crossIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(position, blackListData.get(position).getId());
                }

            });

            holder.acc_user_name.setText(blackListData.get(position).getBlockedUser().getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return blackListData.size();
    }

    public class BlackAccountViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_img;
        public TextView acc_name;
        public TextView acc_user_name;
        public ImageView crossIV;

        public BlackAccountViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_img = itemView.findViewById(R.id.img_Profile_block);
            acc_name = (TextView) itemView.findViewById(R.id.txt_name_block_acc);
            acc_user_name = (TextView) itemView.findViewById(R.id.txt_user_name_block_acc);
            crossIV = (ImageView) itemView.findViewById(R.id.img_block_icon);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position, String userId);
    }

    public void setItemClickListener(final onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
