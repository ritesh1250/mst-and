package com.meest.metme.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemChatImageMyBinding;
import com.meest.databinding.ItemChatImageOtherBinding;

public class ChatOtherImageViewHolder extends RecyclerView.ViewHolder {
    ItemChatImageOtherBinding binding;

    public ChatOtherImageViewHolder(ItemChatImageOtherBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ItemChatImageOtherBinding getBinding() {
        return binding;
    }
}