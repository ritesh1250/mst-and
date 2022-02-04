package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoryGradientResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public class GradientColor {

        @SerializedName("angle")
        @Expose
        private Integer angle;
        @SerializedName("endColor")
        @Expose
        private String endColor;
        @SerializedName("startColor")
        @Expose
        private String startColor;

        public Integer getAngle() {
            return angle;
        }

        public void setAngle(Integer angle) {
            this.angle = angle;
        }

        public String getEndColor() {
            return endColor;
        }

        public void setEndColor(String endColor) {
            this.endColor = endColor;
        }

        public String getStartColor() {
            return startColor;
        }

        public void setStartColor(String startColor) {
            this.startColor = startColor;
        }

    }
    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("gradientColor")
        @Expose
        private GradientColor gradientColor;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("deletedAt")
        @Expose
        private Object deletedAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public GradientColor getGradientColor() {
            return gradientColor;
        }

        public void setGradientColor(GradientColor gradientColor) {
            this.gradientColor = gradientColor;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }
}
