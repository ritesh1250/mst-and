package com.meest.metme.ViewHolder;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.databinding.ChatItemReplyOtherBinding;
import com.meest.databinding.ItemChatDateBinding;

public class DateLayoutViewHolder extends RecyclerView.ViewHolder {

    ItemChatDateBinding binding;
    public DateLayoutViewHolder(ItemChatDateBinding itemView) {
        super(itemView.getRoot());
        binding=itemView;
    }

    public ItemChatDateBinding getBinding() {
        return binding;
    }
}