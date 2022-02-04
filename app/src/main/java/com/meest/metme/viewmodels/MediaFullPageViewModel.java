package com.meest.metme.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.responses.GetDocsMediaChatsResponse;
import com.meest.metme.ChatImageViewActivity;
import com.meest.metme.encription.AESHelper;

public class MediaFullPageViewModel {

    public GetDocsMediaChatsResponse.Data.LastWeek response;
    static Context context;
    public String image;
    String fullName;
    public boolean isVideo;

    public MediaFullPageViewModel(GetDocsMediaChatsResponse.Data.LastWeek response, String fullName, Context context) {

        this.response = response;
        this.image = AESHelper.msgDecrypt(response.getFileURL(), response.getChatHeadId());
        this.fullName = fullName;
        this.context = context;
    }

    public void onClick() {
        Intent intent = new Intent(context, ChatImageViewActivity.class);
        intent.putExtra("title", fullName);
        intent.putExtra("iv_url", response.getFileURL());
        intent.putExtra("type", response.getAttachmentType());
        intent.putExtra("dateAndTime", response.getCreatedAt());
        intent.putExtra("username", fullName);
        intent.putExtra("messageId", "");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @BindingAdapter("bindImage")
    public static void bindImage(ImageView imageView, String imageUrl) {
            Glide.with(imageView.getContext()).
                    load(imageUrl).
                    placeholder(context.getResources().getDrawable(R.drawable.placeholder))
                    .into(imageView);

    }

}
