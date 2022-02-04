package com.meest.model;

import com.google.gson.annotations.SerializedName;

public class SharedPost {

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("originalPostId")
    private String originalPostId;

    @SerializedName("id")
    private String id;

    @SerializedName("postId")
    private String postId;

    @SerializedName("userId")
    private String userId;

    @SerializedName("user")
    private User user;

    @SerializedName("status")
    private boolean status;

    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("originalPostDate")
    private String originalPostDate;

    public String getOriginalPostDate() {
        return originalPostDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getOriginalPostId() {
        return originalPostId;
    }

    public String getId() {
        return id;
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public boolean isStatus() {
        return status;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}