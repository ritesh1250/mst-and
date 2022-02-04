package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowsPeoplesResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    //    @SerializedName("data")
//    @Expose
//    private List<Datum> data = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

//    public List<Datum> getData() {
//        return data;
//    }
//
//    public void setData(List<Datum> data) {
//        this.data = data;
//    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


//    public class Datum {
//
//        @SerializedName("id")
//        @Expose
//        private String id;
//        @SerializedName("status")
//        @Expose
//        private Boolean status;
//        @SerializedName("userId")
//        @Expose
//        private String userId;
//        @SerializedName("followingId")
//        @Expose
//        private String followingId;
//        @SerializedName("accepted")
//        @Expose
//        private Boolean accepted;
//        @SerializedName("updatedAt")
//        @Expose
//        private String updatedAt;
//        @SerializedName("createdAt")
//        @Expose
//        private String createdAt;
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public Boolean getStatus() {
//            return status;
//        }
//
//        public void setStatus(Boolean status) {
//            this.status = status;
//        }
//
//        public String getUserId() {
//            return userId;
//        }
//
//        public void setUserId(String userId) {
//            this.userId = userId;
//        }
//
//        public String getFollowingId() {
//            return followingId;
//        }
//
//        public void setFollowingId(String followingId) {
//            this.followingId = followingId;
//        }
//
//        public Boolean getAccepted() {
//            return accepted;
//        }
//
//        public void setAccepted(Boolean accepted) {
//            this.accepted = accepted;
//        }
//
//        public String getUpdatedAt() {
//            return updatedAt;
//        }
//
//        public void setUpdatedAt(String updatedAt) {
//            this.updatedAt = updatedAt;
//        }
//
//        public String getCreatedAt() {
//            return createdAt;
//        }
//
//        public void setCreatedAt(String createdAt) {
//            this.createdAt = createdAt;
//        }
//
//    }


}
