package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupInfoResponse {
    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("data")
    @Expose
    private List<Data> data;

    @SerializedName("success")
    @Expose
    private String success;

    public String getCode ()
    {
        return code;
    }

    public void setCode (String code)
    {
        this.code = code;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getSuccess ()
    {
        return success;
    }

    public void setSuccess (String success)
    {
        this.success = success;
    }

    public class User {
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;

        @SerializedName("firstName")
        @Expose
        private String firstName;

        @SerializedName("lastName")
        @Expose
        private String lastName;

        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("username")
        @Expose
        private String username;

        public String getDisplayPicture() {
            return displayPicture;
        }

        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
    public class Data {
        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        @SerializedName("deletedAt")
        @Expose
        private String deletedAt;

        @SerializedName("deleteStatus")
        @Expose
        private String deleteStatus;

        @SerializedName("groupId")
        @Expose
        private String groupId;

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("isAdmin")
        @Expose
        private Boolean isAdmin;

        @SerializedName("userId")
        @Expose
        private String userId;

        @SerializedName("user")
        @Expose
        private User user;

        @SerializedName("exitDate")
        @Expose
        private String exitDate;

        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        @SerializedName("chatHead")
        @Expose
        private ChatHead chatHead;

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
        }

        public String getDeleteStatus() {
            return deleteStatus;
        }

        public void setDeleteStatus(String deleteStatus) {
            this.deleteStatus = deleteStatus;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getAdmin() {
            return isAdmin;
        }

        public void setAdmin(Boolean admin) {
            isAdmin = admin;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getExitDate() {
            return exitDate;
        }

        public void setExitDate(String exitDate) {
            this.exitDate = exitDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public ChatHead getChatHead() {
            return chatHead;
        }

        public void setChatHead(ChatHead chatHead) {
            this.chatHead = chatHead;
        }
    }

    public class ChatHead {
        @SerializedName("groupName")
        @Expose
        private String groupName;

        @SerializedName("groupDescription")
        @Expose
        private String groupDescription;

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("groupIcon")
        @Expose
        private String groupIcon;

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getId() {
            return id;
        }

        public String getGroupDescription() {
            return groupDescription;
        }

        public void setGroupDescription(String groupDescription) {
            this.groupDescription = groupDescription;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroupIcon() {
            return groupIcon;
        }

        public void setGroupIcon(String groupIcon) {
            this.groupIcon = groupIcon;
        }

        public class User {
            @SerializedName("displayPicture")
            @Expose
            private String displayPicture;

            @SerializedName("firstName")
            @Expose
            private String firstName;

            @SerializedName("lastName")
            @Expose
            private String lastName;

            @SerializedName("thumbnail")
            @Expose
            private String thumbnail;

            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("username")
            @Expose
            private String username;

            public String getDisplayPicture() {
                return displayPicture;
            }

            public void setDisplayPicture(String displayPicture) {
                this.displayPicture = displayPicture;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

        }
    }
    }
