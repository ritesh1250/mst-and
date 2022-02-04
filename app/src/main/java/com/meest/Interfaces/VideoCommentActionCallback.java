package com.meest.Interfaces;

import com.meest.responses.VideoCommentResponse;

public interface VideoCommentActionCallback {

    void replyClicked(VideoCommentResponse.Row model);

    void changedComment();
}
