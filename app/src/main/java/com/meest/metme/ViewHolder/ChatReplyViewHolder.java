package com.meest.metme.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ChatItemReplyMyBinding;

public class ChatReplyViewHolder extends RecyclerView.ViewHolder {

    ChatItemReplyMyBinding binding;
    public ChatReplyViewHolder(ChatItemReplyMyBinding itemView) {
        super(itemView.getRoot());
        binding = itemView;
    }

    public ChatItemReplyMyBinding getBinding() {
        return binding;
    }
}