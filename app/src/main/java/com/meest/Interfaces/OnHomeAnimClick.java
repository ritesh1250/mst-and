package com.meest.Interfaces;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meest.utils.HomeAnimDialog;
import com.meest.logPressAnim.enums.ButtonPosition;

public interface OnHomeAnimClick {

    void onHomeAnimClick(ButtonPosition viewId, String id, TextView txt_like_txt, ImageView img_like, TextView txt_like, LinearLayout layout_likes_details, TextView txt_recent_user_name, TextView text_others_like, TextView and_text, int postPosition, HomeAnimDialog homeAnimDialog, ImageView img_save);
}
