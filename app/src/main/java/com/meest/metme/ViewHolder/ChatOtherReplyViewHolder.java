package com.meest.metme.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ChatItemReplyOtherBinding;

public class ChatOtherReplyViewHolder extends RecyclerView.ViewHolder {

    ChatItemReplyOtherBinding binding;
    public ChatOtherReplyViewHolder(ChatItemReplyOtherBinding itemView) {
        super(itemView.getRoot());
        binding=itemView;
    }

    public ChatItemReplyOtherBinding getBinding() {
        return binding;
    }
}