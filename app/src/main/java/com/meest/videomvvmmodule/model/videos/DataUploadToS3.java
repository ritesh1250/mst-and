package com.meest.videomvvmmodule.model.videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataUploadToS3 {
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
        @SerializedName("sound_path")
        private String sound_path;
        @SerializedName("sound_id")
        private String sound_id;

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

        public String getSound_path() {
            return sound_path;
        }

        public void setSound_path(String sound_path) {
            this.sound_path = sound_path;
        }

        public String getSound_id() {
            return sound_id;
        }

        public void setSound_id(String sound_id) {
            this.sound_id = sound_id;
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
