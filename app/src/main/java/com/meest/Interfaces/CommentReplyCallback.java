package com.meest.Interfaces;

import com.meest.responses.VideoCommentResponse;

public interface CommentReplyCallback {

    void replyClicked(VideoCommentResponse.Row model);

}