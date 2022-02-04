package com.meest.metme.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemChatAudioMyBinding;

public class ChatAudioViewHolder extends RecyclerView.ViewHolder {
    ItemChatAudioMyBinding binding;

    public ChatAudioViewHolder(ItemChatAudioMyBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ItemChatAudioMyBinding getBinding() {
        return binding;
    }
}