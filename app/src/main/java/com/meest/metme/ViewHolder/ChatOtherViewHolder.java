package com.meest.metme.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemChatMyBinding;
import com.meest.databinding.ItemChatOtherBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatOtherViewHolder extends RecyclerView.ViewHolder {
    ItemChatOtherBinding binding;

    public ChatOtherViewHolder(ItemChatOtherBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ItemChatOtherBinding getBinding() {
        return binding;
    }
}
