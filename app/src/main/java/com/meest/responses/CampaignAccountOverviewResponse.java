package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CampaignAccountOverviewResponse {

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

        @SerializedName("peoplesReached")
        @Expose
        private Integer peoplesReached;
        @SerializedName("totalSpent")
        @Expose
        private Integer totalSpent;
        @SerializedName("runnung")
        @Expose
        private Integer runnung;
        @SerializedName("completed")
        @Expose
        private Integer completed;

        public Integer getPeoplesReached() {
            return peoplesReached==null? 0:peoplesReached;
        }

        public void setPeoplesReached(Integer peoplesReached) {
            this.peoplesReached = peoplesReached;
        }

        public Integer getTotalSpent() {
            return totalSpent == null?0:totalSpent;
        }

        public void setTotalSpent(Integer totalSpent) {
            this.totalSpent = totalSpent;
        }

        public Integer getRunnung() {
            return runnung;
        }

        public void setRunnung(Integer runnung) {
            this.runnung = runnung;
        }

        public Integer getCompleted() {
            return completed;
        }

        public void setCompleted(Integer completed) {
            this.completed = completed;
        }

    }
}
