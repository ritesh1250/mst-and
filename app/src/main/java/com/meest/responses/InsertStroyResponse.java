package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InsertStroyResponse {

    public class Data {

        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("story")
        @Expose
        private String story;
        @SerializedName("caption")
        @Expose
        private String caption;
        @SerializedName("hashTags")
        @Expose
        private List<Object> hashTags = null;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("image")
        @Expose
        private Boolean image;
        @SerializedName("expires")
        @Expose
        private String expires;
        @SerializedName("status")
        @Expose
        private Boolean status;


        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStory() {
            return story;
        }

        public void setStory(String story) {
            this.story = story;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public List<Object> getHashTags() {
            return hashTags;
        }

        public void setHashTags(List<Object> hashTags) {
            this.hashTags = hashTags;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Boolean getImage() {
            return image;
        }

        public void setImage(Boolean image) {
            this.image = image;
        }

        public String getExpires() {
            return expires;
        }

        public void setExpires(String expires) {
            this.expires = expires;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }



    }




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





}
