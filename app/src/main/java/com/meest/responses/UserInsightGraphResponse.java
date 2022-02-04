package com.meest.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class UserInsightGraphResponse {

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

        @SerializedName("engagementGraph")
        @Expose
        private List<EngagementGraph> engagementGraph = null;
        @SerializedName("maleCount")
        @Expose
        private Integer maleCount;
        @SerializedName("femaleCount")
        @Expose
        private Integer femaleCount;

        public List<EngagementGraph> getEngagementGraph() {
            return engagementGraph;
        }

        public void setEngagementGraph(List<EngagementGraph> engagementGraph) {
            this.engagementGraph = engagementGraph;
        }

        public Integer getMaleCount() {
            return maleCount;
        }

        public void setMaleCount(Integer maleCount) {
            this.maleCount = maleCount;
        }

        public Integer getFemaleCount() {
            return femaleCount;
        }

        public void setFemaleCount(Integer femaleCount) {
            this.femaleCount = femaleCount;
        }

    }

    public class EngagementGraph {

        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("count")
        @Expose
        private Integer count;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

    }
}