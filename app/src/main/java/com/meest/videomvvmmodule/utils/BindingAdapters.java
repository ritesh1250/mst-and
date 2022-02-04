package com.meest.videomvvmmodule.utils;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.meest.R;


public class BindingAdapters {

    @BindingAdapter({"app:profile_url"})
    public static void loadProfileImage(ImageView view, String profileUrl) {
        if (!TextUtils.isEmpty(profileUrl)) {
//            new GlideLoader(view.getContext()).loadWithCircleCrop(Const.ITEM_BASE_URL + profileUrl, view);
            new GlideLoader(view.getContext()).loadProfileImage(profileUrl, view);
        }
    }

    @BindingAdapter({"app:image_url"})
    public static void loadImage(ImageView view, String profileUrl) {
        if (!TextUtils.isEmpty(profileUrl)) {
            String url = profileUrl;
//            String url = Const.ITEM_BASE_URL + profileUrl;
            new GlideLoader(view.getContext()).loadImage(url, view);
        }
    }

    @BindingAdapter({"app:hashtag_url"})
    public static void loadHashTagImage(ImageView view, String profileUrl) {
        if (!TextUtils.isEmpty(profileUrl)) {
            String url = profileUrl;
//            String url = Const.ITEM_BASE_URL + profileUrl;
            new GlideLoader(view.getContext()).loadHashTagImage(url, view);
        }
    }

    @BindingAdapter({"app:notification_type"})
    public static void loadNotificationImage(ImageView view, String type) {
        Drawable drawable = view.getContext().getResources().getDrawable(R.drawable.ic_boss);
        switch (type) {
            case "1":
                drawable = view.getContext().getResources().getDrawable(R.drawable.ic_noti_like);
                break;
            case "2":
                drawable = view.getContext().getResources().getDrawable(R.drawable.ic_noti_comment);
                break;
            case "3":
                drawable = view.getContext().getResources().getDrawable(R.drawable.ic_noti_follow);
                break;
            case "4":
                drawable = view.getContext().getResources().getDrawable(R.drawable.notification_icon);
                break;

            default:
                break;
        }
        new GlideLoader(view.getContext()).loadNotificationImage(drawable, view);
    }

    @BindingAdapter({"app:media_image", "app:is_round"})
    public static void loadMediaImage(ImageView view, String profileUrl, boolean isRound) {
        if (isRound) {
            new GlideLoader(view.getContext()).loadMediaImage(profileUrl, view);
//            new GlideLoader(view.getContext()).loadMediaRoundImage(profileUrl, view);
        } else {
            new GlideLoader(view.getContext()).loadMediaImage(profileUrl, view);
        }
    }

    @BindingAdapter({"app:blur_image"})
    public static void loadBlurImage(ImageView view, String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            String url = imageUrl;
//            String url = Const.ITEM_BASE_URL + imageUrl;
            new GlideLoader(view.getContext()).loadBlurImage(url, view);
        }
    }

    @BindingAdapter({"app:blur_image"})
    public static void loadMediaRoundBitmap(ImageView view, Bitmap bitmap) {
        new GlideLoader(view.getContext()).loadMediaRoundBitmap(bitmap, view);
    }

    @BindingAdapter({"app:ads_icon"})
    public static void loadRoundDrawable(ImageView view, Drawable bitmap) {
        new GlideLoader(view.getContext()).loadRoundDrawable(bitmap, view);
    }

    @BindingAdapter({"app:setFont"})
    public static void setFont(TextView textView, String fontName) {
        try {
            if (fontName != null && !fontName.equals(""))
                textView.setTypeface(Typeface.createFromFile(fontName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter("android:textSize_bind")
    public static void bindTextSize(TextView textView, float size) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }
}


