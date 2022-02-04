package com.meest.metme.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemChatVideoOtherBinding;

public class ChatOtherVideoViewHolder extends RecyclerView.ViewHolder {

    ItemChatVideoOtherBinding binding;

    public ChatOtherVideoViewHolder(ItemChatVideoOtherBinding itemView) {
        super(itemView.getRoot());
        binding = itemView;
    }

    public ItemChatVideoOtherBinding getBinding() {
        return binding;
    }
}
