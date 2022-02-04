package com.meest.social.socialViewModel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateGroupResponse {


    public class Data {

        @SerializedName("groupAdmin")
        @Expose
        private String groupAdmin;
        @SerializedName("updateBy")
        @Expose
        private String updateBy;
        @SerializedName("groupName")
        @Expose
        private String groupName;
        @SerializedName("groupIcon")
        @Expose
        private String groupIcon;
        @SerializedName("groupMembers")
        @Expose
        private String groupMembers;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("isGroup")
        @Expose
        private Boolean isGroup;

        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        public String getGroupAdmin() {
            return groupAdmin;
        }

        public void setGroupAdmin(String groupAdmin) {
            this.groupAdmin = groupAdmin;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupIcon() {
            return groupIcon;
        }

        public void setGroupIcon(String groupIcon) {
            this.groupIcon = groupIcon;
        }

        public String getGroupMembers() {
            return groupMembers;
        }

        public void setGroupMembers(String groupMembers) {
            this.groupMembers = groupMembers;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Boolean getIsGroup() {
            return isGroup;
        }

        public void setIsGroup(Boolean isGroup) {
            this.isGroup = isGroup;
        }



        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }





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



}
