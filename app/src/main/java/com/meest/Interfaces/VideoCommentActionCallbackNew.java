package com.meest.Interfaces;


import com.meest.social.socialViewModel.model.comment.VideoCommentResponse;

public interface VideoCommentActionCallbackNew {

    void replyClicked(VideoCommentResponse.Row model);

    void changedComment();
}
