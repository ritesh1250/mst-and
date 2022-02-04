package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrendHashtagResponse {


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

    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("hashtag")
        @Expose
        private String hashtag;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("addedBy")
        @Expose
        private String addedBy;
        @SerializedName("status")
        @Expose
        private Boolean status;



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHashtag() {
            return hashtag;
        }

        public void setHashtag(String hashtag) {
            this.hashtag = hashtag;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getAddedBy() {
            return addedBy;
        }

        public void setAddedBy(String addedBy) {
            this.addedBy = addedBy;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }


    }
}
