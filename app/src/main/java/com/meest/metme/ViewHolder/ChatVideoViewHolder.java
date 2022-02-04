package com.meest.metme.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemChatVideoMyBinding;

public class ChatVideoViewHolder extends RecyclerView.ViewHolder {
    ItemChatVideoMyBinding binding;

    public ChatVideoViewHolder(ItemChatVideoMyBinding itemView) {
        super(itemView.getRoot());
        binding = itemView;
    }

    public ItemChatVideoMyBinding getBinding() {
        return binding;
    }
}
