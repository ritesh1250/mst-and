package com.meest.metme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SaveTextAppearanceModel {
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("success")
    @Expose
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public class Data {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("toUserId")
        @Expose
        private String toUserId;
        @SerializedName("fontId")
        @Expose
        private Integer fontId;
        @SerializedName("firstColor")
        @Expose
        private String firstColor;
        @SerializedName("secondColor")
        @Expose
        private String secondColor;
        @SerializedName("bgColor")
        @Expose
        private String bgColor;
        @SerializedName("gradient")
        @Expose
        private String gradient;
        @SerializedName("fontName")
        @Expose
        private String fontName;
        @SerializedName("wallPaper")
        @Expose
        private String wallPaper;
        @SerializedName("bgFirstColor")
        @Expose
        private String bgFirstColor;
        @SerializedName("bgSecondColor")
        @Expose
        private String bgSecondColor;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("fontData")
        @Expose
        private FontData fontData;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getToUserId() {
            return toUserId;
        }

        public void setToUserId(String toUserId) {
            this.toUserId = toUserId;
        }

        public Integer getFontId() {
            return fontId;
        }

        public void setFontId(Integer fontId) {
            this.fontId = fontId;
        }

        public String getFirstColor() {
            return firstColor;
        }

        public void setFirstColor(String firstColor) {
            this.firstColor = firstColor;
        }

        public String getSecondColor() {
            return secondColor;
        }

        public void setSecondColor(String secondColor) {
            this.secondColor = secondColor;
        }

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        public String getGradient() {
            return gradient;
        }

        public void setGradient(String gradient) {
            this.gradient = gradient;
        }

        public String getFontName() {
            return fontName;
        }

        public void setFontName(String fontName) {
            this.fontName = fontName;
        }

        public String getWallPaper() {
            return wallPaper;
        }

        public void setWallPaper(String wallPaper) {
            this.wallPaper = wallPaper;
        }

        public String getBgFirstColor() {
            return bgFirstColor;
        }

        public void setBgFirstColor(String bgFirstColor) {
            this.bgFirstColor = bgFirstColor;
        }

        public String getBgSecondColor() {
            return bgSecondColor;
        }

        public void setBgSecondColor(String bgSecondColor) {
            this.bgSecondColor = bgSecondColor;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public FontData getFontData() {
            return fontData;
        }

        public void setFontData(FontData fontData) {
            this.fontData = fontData;
        }
    }
    public class FontData {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("size1")
        @Expose
        private Integer size1;
        @SerializedName("size2")
        @Expose
        private Integer size2;
        @SerializedName("size3")
        @Expose
        private Integer size3;
        @SerializedName("size4")
        @Expose
        private Integer size4;
        @SerializedName("size5")
        @Expose
        private Integer size5;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getSize1() {
            return size1;
        }

        public void setSize1(Integer size1) {
            this.size1 = size1;
        }

        public Integer getSize2() {
            return size2;
        }

        public void setSize2(Integer size2) {
            this.size2 = size2;
        }

        public Integer getSize3() {
            return size3;
        }

        public void setSize3(Integer size3) {
            this.size3 = size3;
        }

        public Integer getSize4() {
            return size4;
        }

        public void setSize4(Integer size4) {
            this.size4 = size4;
        }

        public Integer getSize5() {
            return size5;
        }

        public void setSize5(Integer size5) {
            this.size5 = size5;
        }
    }
}
