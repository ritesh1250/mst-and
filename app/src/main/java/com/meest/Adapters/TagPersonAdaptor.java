package com.meest.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.responses.FeedResponse;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;

import java.util.List;

public class TagPersonAdaptor extends RecyclerView.Adapter<TagPersonAdaptor.mViewHolder> {

    List<FeedResponse.UserTags> list;

    public TagPersonAdaptor(List<FeedResponse.UserTags> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TagPersonAdaptor.mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_tagged_person, parent, false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class mViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tvUserName;
        TextView tvFullName;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvUserName = itemView.findViewById(R.id.tv_username);
            tvFullName = itemView.findViewById(R.id.tv_full_name);
        }

        public void setData(FeedResponse.UserTags a) {
            Glide.with(itemView.getContext()).load(a.getDisplayPicture()).placeholder(R.drawable.image_placeholder).into(ivProfile);
            tvUserName.setText(a.getUsername());
            tvFullName.setText(String.format("%s %s", a.getFirstName(), a.getLastName()));
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), OtherUserAccount.class);
                intent.putExtra("userId", a.getId());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
