
package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatSettingResponse {

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


        @SerializedName("chatHeadId")
        @Expose
        private String chatHeadId;
        @SerializedName("settingType")
        @Expose
        private String settingType;

        @SerializedName("markPriority")
        @Expose
        private Boolean markPriority;

        @SerializedName("isNotificationMute")
        @Expose
        private Boolean isNotificationMute;

        @SerializedName("isReported")
        @Expose
        private Boolean isReported;

        public String getReportText() {
            return reportText;
        }

        public void setReportText(String reportText) {
            this.reportText = reportText;
        }

        @SerializedName("reportText")
        @Expose
        private String reportText;


        @SerializedName("isBlocked")
        @Expose
        private Boolean isBlocked;
        @SerializedName("status")
        @Expose
        private Object status;

        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        @SerializedName("userId")
        @Expose
        private String userId;

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("clearChat")
        @Expose
        private Boolean clearChat;
        @SerializedName("chatId")
        @Expose
        private Object chatId;
        @SerializedName("exportChat")
        @Expose
        private Boolean exportChat;
        @SerializedName("isGroupChatDelete")
        @Expose
        private Boolean isGroupChatDelete;
        @SerializedName("isChatHeadDeleted")
        @Expose
        private Boolean isChatHeadDeleted;
        @SerializedName("snoozeChat")
        @Expose
        private Boolean snoozeChat;
        @SerializedName("archiveChat")
        @Expose
        private Boolean archiveChat;
        @SerializedName("mediaAutodownload")
        @Expose
        private Boolean mediaAutodownload;

        @SerializedName("deletedAt")
        @Expose
        private Object deletedAt;

        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("restrict")
        @Expose
        private boolean restrict;

        public boolean isRestrict() {
            return restrict;
        }

        public void setRestrict(boolean restrict) {
            this.restrict = restrict;
        }

        public String getChatHeadId() {
            return chatHeadId;
        }

        public void setChatHeadId(String chatHeadId) {
            this.chatHeadId = chatHeadId;
        }

        public String getSettingType() {
            return settingType;
        }

        public void setSettingType(String settingType) {
            this.settingType = settingType;
        }

        public Boolean getMarkPriority() {
            return markPriority;
        }

        public void setMarkPriority(Boolean markPriority) {
            this.markPriority = markPriority;
        }

        public Boolean getIsNotificationMute() {
            return isNotificationMute;
        }

        public void setIsNotificationMute(Boolean isNotificationMute) {
            this.isNotificationMute = isNotificationMute;
        }

        public Boolean getIsReported() {
            return isReported;
        }

        public void setIsReported(Boolean isReported) {
            this.isReported = isReported;
        }

        public Boolean getIsBlocked() {
            return isBlocked;
        }

        public void setIsBlocked(Boolean isBlocked) {
            this.isBlocked = isBlocked;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }


        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Boolean getNotificationMute() {
            return isNotificationMute;
        }

        public void setNotificationMute(Boolean notificationMute) {
            isNotificationMute = notificationMute;
        }

        public Boolean getReported() {
            return isReported;
        }

        public void setReported(Boolean reported) {
            isReported = reported;
        }

        public Boolean getBlocked() {
            return isBlocked;
        }

        public void setBlocked(Boolean blocked) {
            isBlocked = blocked;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getClearChat() {
            return clearChat;
        }

        public void setClearChat(Boolean clearChat) {
            this.clearChat = clearChat;
        }

        public Object getChatId() {
            return chatId;
        }

        public void setChatId(Object chatId) {
            this.chatId = chatId;
        }

        public Boolean getExportChat() {
            return exportChat;
        }

        public void setExportChat(Boolean exportChat) {
            this.exportChat = exportChat;
        }

        public Boolean getGroupChatDelete() {
            return isGroupChatDelete;
        }

        public void setGroupChatDelete(Boolean groupChatDelete) {
            isGroupChatDelete = groupChatDelete;
        }

        public Boolean getChatHeadDeleted() {
            return isChatHeadDeleted;
        }

        public void setChatHeadDeleted(Boolean chatHeadDeleted) {
            isChatHeadDeleted = chatHeadDeleted;
        }

        public Boolean getSnoozeChat() {
            return snoozeChat;
        }

        public void setSnoozeChat(Boolean snoozeChat) {
            this.snoozeChat = snoozeChat;
        }

        public Boolean getArchiveChat() {
            return archiveChat;
        }

        public void setArchiveChat(Boolean archiveChat) {
            this.archiveChat = archiveChat;
        }

        public Boolean getMediaAutodownload() {
            return mediaAutodownload;
        }

        public void setMediaAutodownload(Boolean mediaAutodownload) {
            this.mediaAutodownload = mediaAutodownload;
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
