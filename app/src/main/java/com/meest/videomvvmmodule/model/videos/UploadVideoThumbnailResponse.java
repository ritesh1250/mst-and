package com.meest.videomvvmmodule.model.videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadVideoThumbnailResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    private Data data;

    public class Data {

        @SerializedName("video_url")
        private String video_url;
        @SerializedName("thumbnail_path")
        private String thumbnail_path;

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getThumbnail_path() {
            return thumbnail_path;
        }

        public void setThumbnail_path(String thumbnail_path) {
            this.thumbnail_path = thumbnail_path;
        }
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
