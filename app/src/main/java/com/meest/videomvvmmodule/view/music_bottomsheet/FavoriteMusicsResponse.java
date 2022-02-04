package com.meest.videomvvmmodule.view.music_bottomsheet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class FavoriteMusicsResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("sound_id")
        @Expose
        private String soundId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("create_date")
        @Expose
        private String createDate;
        @SerializedName("sound_category_id")
        @Expose
        private String soundCategoryId;
        @SerializedName("sound_title")
        @Expose
        private String soundTitle;
        @SerializedName("sound")
        @Expose
        private String sound;
        @SerializedName("sound_image")
        @Expose
        private String soundImage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSoundId() {
            return soundId;
        }

        public void setSoundId(String soundId) {
            this.soundId = soundId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getSoundCategoryId() {
            return soundCategoryId;
        }

        public void setSoundCategoryId(String soundCategoryId) {
            this.soundCategoryId = soundCategoryId;
        }

        public String getSoundTitle() {
            return soundTitle;
        }

        public void setSoundTitle(String soundTitle) {
            this.soundTitle = soundTitle;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getSoundImage() {
            return soundImage;
        }

        public void setSoundImage(String soundImage) {
            this.soundImage = soundImage;
        }

    }
}