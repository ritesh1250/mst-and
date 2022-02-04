package com.meest.metme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FontResponse {
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("data")
    @Expose
    private List<FontName> data;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<FontName> getData() {
        return data;
    }

    public void setData(List<FontName> data) {
        this.data = data;
    }

    public class FontName {

        @SerializedName("fonturl")
        @Expose
        private String fonturl;
        @SerializedName("fontimage")
        @Expose
        private String fontimage;
        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("size1")
        @Expose
        private float size1;
        @SerializedName("size2")
        @Expose
        private float size2;
        @SerializedName("size3")
        @Expose
        private float size3;
        @SerializedName("size4")
        @Expose
        private float size4;
        @SerializedName("size5")
        @Expose
        private float size5;

        public String getFonturl() {
            return fonturl;
        }

        public void setFonturl(String fonturl) {
            this.fonturl = fonturl;
        }

        public String getFontimage() {
            return fontimage;
        }

        public void setFontimage(String fontimage) {
            this.fontimage = fontimage;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public float getSize1() {
            return size1;
        }

        public void setSize1(float size1) {
            this.size1 = size1;
        }

        public float getSize2() {
            return size2;
        }

        public void setSize2(float size2) {
            this.size2 = size2;
        }

        public float getSize3() {
            return size3;
        }

        public void setSize3(float size3) {
            this.size3 = size3;
        }

        public float getSize4() {
            return size4;
        }

        public void setSize4(float size4) {
            this.size4 = size4;
        }

        public float getSize5() {
            return size5;
        }

        public void setSize5(float size5) {
            this.size5 = size5;
        }
    }
}

