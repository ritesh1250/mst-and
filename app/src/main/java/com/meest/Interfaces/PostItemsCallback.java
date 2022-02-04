package com.meest.Interfaces;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meest.responses.FeedResponse;

import java.util.List;

public interface PostItemsCallback {
    public void openTagsBottomSheet(List<FeedResponse.UserTags> list);

    public void openOtherUserProfile(String userId);

    public void likePost(String userId, TextView txt_like_txt, ImageView img_like, TextView txt_like, LinearLayout layout_likes_details,
                         TextView txt_recent_user_name, TextView text_others_like, TextView and_text, int position,LinearLayout layout_likes_list);

    public void userCommentClicked(int position, String id, String caption, boolean isCommentAllowed, boolean isAd, int commentCount);

    public void shareClicked();
    public void sharePostClicked(int position);

    public void saveClicked(String postId, int position, ImageView img_save);

    public void menuClicked(int position);

    public void onLongClicked(String id, TextView txt_like_txt, ImageView img_like, TextView txt_like, LinearLayout layout_likes_details, TextView txt_recent_user_name, TextView text_others_like, TextView and_text, int position, ImageView img_save);
}


