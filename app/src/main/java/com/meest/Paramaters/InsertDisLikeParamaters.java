package com.meest.Paramaters;

public class InsertDisLikeParamaters {
    String postId;
    boolean status;
    boolean disliked;

    public InsertDisLikeParamaters(String postId, boolean status, boolean disliked) {
        this.postId = postId;
        this.status = status;
        this.disliked = disliked;
    }
}
