package com.meest.Paramaters;

public class InsertCommentParam {
    String postId;
    String comment;
    boolean status;


    public InsertCommentParam(String postId, String comment, boolean status) {
        this.postId = postId;
        this.comment = comment;
        this.status = status;
    }
}
