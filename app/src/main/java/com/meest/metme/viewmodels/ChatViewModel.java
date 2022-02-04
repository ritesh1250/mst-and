package com.meest.metme.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.metme.ChatBoatActivity;
import com.meest.metme.encription.AESHelper;
import com.meest.metme.model.chat.NewChatListResponse;
import com.meest.videomvvmmodule.utils.Global;

public class ChatViewModel extends ViewModel {

    public static Activity activity;
    public String username, image, msg, time, readStatus;
    public int msgCount;
    public boolean read, sent, isBlock;
    public NewChatListResponse chatListModel;

    public ChatViewModel(NewChatListResponse chatListModel, Activity activity) {

        this.activity = activity;
        this.chatListModel = chatListModel;
        this.username = chatListModel.getUsername();
        this.time = chatListModel.getChats().get(0).getCreatedAt();
        this.msgCount = chatListModel.getChats().get(0).getUnreadCount();
        this.read = chatListModel.getChats().get(0).getRead();
        this.readStatus = chatListModel.getChats().get(0).getReadStatus();
        this.sent = chatListModel.getChats().get(0).getSent();
//        this.isBlock = false;
        if (chatListModel.getBlocked() != null)
            this.isBlock = chatListModel.getBlocked();
        else
            this.isBlock = false;

        if (chatListModel.getThumbnail() != null || chatListModel.getDisplayPicture() != null) {
            if (!chatListModel.getThumbnail().equals("")) {
                this.image = chatListModel.getThumbnail();
            } else {
                this.image = chatListModel.getDisplayPicture();
            }
        }
        this.msg = AESHelper.msgDecrypt(chatListModel.getChats().get(0).getLastMsg(), chatListModel.getChatHeadId());

    }

    @BindingAdapter("bindUserChatProfile")
    public static void userChatProfile(ImageView imageView, String imageUrl) {

        if (imageUrl != null) {
            if (!imageUrl.isEmpty()) {

                Glide.with(imageView.getContext()).load(imageUrl).placeholder(R.drawable.ic_edit_metme)
                        .into(imageView);
            } else {
                if (activity != null)
                    imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_edit_metme));
            }
        }
    }

    /*   @BindingAdapter("bindUserVisibility")public void userCountVisible() {if(msgCount>0){}}*/
    @BindingAdapter("bindSeenReadDelivered")
    public static void seenReadDelivered(ImageView imageView, String readStatus) {
        if (readStatus != null) {
            if (readStatus.equals(Global.ReadStatus.read.name())) {
                imageView.setImageResource(R.drawable.ic_chat_read_outside);
            } else if (readStatus.equals(Global.ReadStatus.delivered.name())) {
                imageView.setImageResource(R.drawable.ic_chat_delivered_outside);
            } else {
                // imageView.setImageResource(R.drawable.ic_chat_delivered_outside);
                imageView.setImageResource(R.drawable.ic_chat_send_outside);
            }

        } else {
            imageView.setImageResource(R.drawable.ic_chat_read_outside);
        }
    }

    public void onChatItemClick() {
        Intent intent = new Intent(activity, ChatBoatActivity.class);
        intent.putExtra("newChatListResponse", chatListModel);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT|Intent.FLAG_ACTIVITY_MULTIPLE_TASK /*| Intent.FLAG_ACTIVITY_CLEAR_TASK*/);
        activity.startActivity(intent);
    }
}
