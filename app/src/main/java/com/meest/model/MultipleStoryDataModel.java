package com.meest.model;

import com.meest.Paramaters.TextPostUploadParam;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class  MultipleStoryDataModel{

    @SerializedName("story")
    @Expose
    private List<String> story;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("hashTags")
    @Expose
    private List<String> hashTags;
    @SerializedName("location")
    @Expose
    private TextPostUploadParam.Location location;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("image")
    @Expose
    private Boolean image;

    public List<String> getStory() {
        return story;
    }

    public void setStory(List<String> story) {
        this.story = story;
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

    public TextPostUploadParam.Location getLocation() {
        return location;
    }

    public void setLocation(TextPostUploadParam.Location location) {
        this.location = location;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getImage() {
        return image;
    }

    public void setImage(Boolean image) {
        this.image = image;
    }
}
