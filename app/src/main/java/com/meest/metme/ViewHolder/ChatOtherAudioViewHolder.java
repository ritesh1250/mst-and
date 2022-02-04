package com.meest.metme.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemChatAudioOtherBinding;

public class ChatOtherAudioViewHolder extends RecyclerView.ViewHolder {
    ItemChatAudioOtherBinding binding;

    public ChatOtherAudioViewHolder(ItemChatAudioOtherBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public ItemChatAudioOtherBinding getBinding() {
        return binding;
    }
}