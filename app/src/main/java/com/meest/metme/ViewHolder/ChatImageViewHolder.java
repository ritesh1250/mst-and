package com.meest.metme.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemChatImageMyBinding;

public class ChatImageViewHolder extends RecyclerView.ViewHolder {
    ItemChatImageMyBinding binding;

    public ChatImageViewHolder(ItemChatImageMyBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ItemChatImageMyBinding getBinding() {
        return binding;
    }
}
