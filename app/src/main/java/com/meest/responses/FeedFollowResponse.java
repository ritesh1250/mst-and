package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedFollowResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


    public class Data {

        @SerializedName("user")
        @Expose
        private UserResponse user;
        @SerializedName("follower")
        @Expose
        private List<UserResponse> follower = null;
        @SerializedName("following")
        @Expose
        private List<UserResponse> following = null;

        public UserResponse getUser() {
            return user;
        }

        public void setUser(UserResponse user) {
            this.user = user;
        }

        public List<UserResponse> getFollower() {
            return follower;
        }

        public void setFollower(List<UserResponse> follower) {
            this.follower = follower;
        }

        public List<UserResponse> getFollowing() {
            return following;
        }

        public void setFollowing(List<UserResponse> following) {
            this.following = following;
        }

    }
}
