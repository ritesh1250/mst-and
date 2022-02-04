
package com.meest.Paramaters;

import com.meest.responses.FeedResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TextPostUploadParam {

    @SerializedName("postData")
    @Expose
    private List<PostDatum> postData = null;
    @SerializedName("postType")
    @Expose
    private String postType;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("hashTags")
    @Expose
    private List<String> hashTags = null;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("viewPost")
    @Expose
    private String viewPost;
    @SerializedName("allowComment")
    @Expose
    private Boolean allowComment;
    @SerializedName("sensitiveContent")
    @Expose
    private Boolean sensitiveContent;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("feeling")
    @Expose
    private String feeling;
    @SerializedName("subFeeling")
    @Expose
    private String subFeeling;
    @SerializedName("tagLocation")
    @Expose
    private String tagLocation;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("tags")
    @Expose
    private List<FeedResponse.UserTags> taggedPersons = new ArrayList<>();

    public List<PostDatum> getPostData() {
        return postData;
    }

    public void setPostData(List<PostDatum> postData) {
        this.postData = postData;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getViewPost() {
        return viewPost;
    }

    public void setViewPost(String viewPost) {
        this.viewPost = viewPost;
    }

    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTagLocation() {
        return tagLocation;
    }

    public void setTagLocation(String tagLocation) {
        this.tagLocation = tagLocation;
    }

    public List<FeedResponse.UserTags> getTaggedPersons() {
        return taggedPersons;
    }

    public void setTaggedPersons(List<FeedResponse.UserTags> taggedPersons) {
        this.taggedPersons = taggedPersons;
    }

    public Boolean getSensitiveContent() {
        return sensitiveContent;
    }

    public void setSensitiveContent(Boolean sensitiveContent) {
        this.sensitiveContent = sensitiveContent;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public String getSubFeeling() {
        return subFeeling;
    }

    public void setSubFeeling(String subFeeling) {
        this.subFeeling = subFeeling;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public static class PostDatum {

        @SerializedName("post")
        @Expose
        private String post;
        @SerializedName("image")
        @Expose
        private Integer image;
        @SerializedName("video")
        @Expose
        private Integer video;
        @SerializedName("isText")
        @Expose
        private Integer isText;
        @SerializedName("textColorCode")
        @Expose
        private String textColorCode;
        @SerializedName("status")
        private boolean status;

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public Integer getImage() {
            return image;
        }

        public void setImage(Integer image) {
            this.image = image;
        }

        public Integer getVideo() {
            return video;
        }

        public void setVideo(Integer video) {
            this.video = video;
        }

        public Integer getIsText() {
            return isText;
        }

        public void setIsText(Integer isText) {
            this.isText = isText;
        }

        public String getTextColorCode() {
            return textColorCode;
        }

        public void setTextColorCode(String textColorCode) {
            this.textColorCode = textColorCode;
        }
    }


    public static class Location {

        @SerializedName("lat")
        @Expose
        private Double lat;
        @SerializedName("long")
        @Expose
        private Double _long;

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLong() {
            return _long;
        }

        public void setLong(Double _long) {
            this._long = _long;
        }

    }


}
