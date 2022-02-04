package com.meest.svs.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AudioDataModel {

    @SerializedName("audioURL")
    @Expose
    private String audioURL;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("catagoryId")
    @Expose
    private String catagoryId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("approvalStatus")
    @Expose
    private String approvalStatus;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public String getAudioURL() {
        return audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatagoryId() {
        return catagoryId;
    }

    public void setCatagoryId(String catagoryId) {
        this.catagoryId = catagoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
