package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserUpdatedSelectedTopics {

    //    public Object Row;
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

//    public class Data {
//
//        @SerializedName("count")
//        @Expose
//        private Integer count;
////        @SerializedName("rows")
////        @Expose
////        private List<Row> rows = new ArrayList<>();
//
//        public Integer getCount() {
//            return count;
//        }
//
//        public void setCount(Integer count) {
//            this.count = count;
//        }
//
////        public List<Row> getRows() {
////            return rows;
////        }
////
////        public void setRows(List<Row> rows) {
////            this.rows = rows;
////        }
//
//    }

    public class Data {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("topicId")
        @Expose
        private String topicId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("deletedAt")
        @Expose
        private String deletedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("topic")
        @Expose
        private Topic topic = new Topic();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
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

        public Topic getTopic() {
            return topic;
        }

        public void setTopic(Topic topic) {
            this.topic = topic;
        }
    }


    public class Topic {
        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("topic")
        @Expose
        private String _topic;

        @SerializedName("status")
        @Expose
        private Boolean status;

        @SerializedName("isDeleted")
        @Expose
        private Boolean isDeleted;

        @SerializedName("deletedAt")
        @Expose
        private String deletedAt;

        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String get_topic() {
            return _topic;
        }

        public void set_topic(String _topic) {
            this._topic = _topic;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public Boolean getDeleted() {
            return isDeleted;
        }

        public void setDeleted(Boolean deleted) {
            isDeleted = deleted;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
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
    }


}
