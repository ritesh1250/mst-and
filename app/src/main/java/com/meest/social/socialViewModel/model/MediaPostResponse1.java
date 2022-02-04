package com.meest.social.socialViewModel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MediaPostResponse1 {

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


    public static class Data {

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("rows")
        @Expose
        private List<RowsFeed> rowsFeed = null;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<RowsFeed> getRowsFeed() {
            return rowsFeed;
        }

        public void setRowsFeed(List<RowsFeed> rowsFeed) {
            this.rowsFeed = rowsFeed;
        }


        public static class RowsFeed {

            @SerializedName("thumbnail")
            @Expose
            private String thumbnail;
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("userId")
            @Expose
            private String userId;

            public RowsFeed() {
            }

            private String viewType="";

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getId() {
                return id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getViewType() {
                return viewType;
            }

            public void setViewType(String viewType) {
                this.viewType = viewType;
            }
        }
    }



}
