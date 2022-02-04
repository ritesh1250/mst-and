package com.meest.svs.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MusicVideoResponse {

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

    public class Data {

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("rows")
        @Expose
        private List<VideoDetailModel> rows;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<VideoDetailModel> getRows() {
            return rows;
        }

        public void setRows(List<VideoDetailModel> rows) {
            this.rows = rows;
        }
    }
}
