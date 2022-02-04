package com.meest.metme.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemChatMyBinding;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    ItemChatMyBinding binding;

    public ChatViewHolder(ItemChatMyBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ItemChatMyBinding getBinding() {
        return binding;
    }

    public void setChat() {
    }
}
