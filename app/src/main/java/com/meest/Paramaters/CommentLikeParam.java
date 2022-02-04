package com.meest.Paramaters;

public class CommentLikeParam {

    String postId;
    String commentId;
    Boolean status;

    public CommentLikeParam(String postId, String commentId, Boolean status) {
        this.postId = postId;
        this.commentId = commentId;
        this.status = status;
    }
}
