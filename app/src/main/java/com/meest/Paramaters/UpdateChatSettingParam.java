package com.meest.Paramaters;

public class UpdateChatSettingParam {

    private String chatHeadId;
    private String settingType;
    private Boolean isNotificationMute;
    private Boolean isReported;
    private String reportText;
    private Boolean markPriority;
    private Boolean isBlocked;
    private Boolean clearChat;
    private Boolean restrict;

    public Boolean getRestrict() {
        return restrict;
    }

    public void setRestrict(Boolean restrict) {
        this.restrict = restrict;
    }


    private String isChatHeadDeleted;

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

    public Boolean getClearChat() {
        return clearChat;
    }

    public void setClearChat(Boolean clearChat) {
        this.clearChat = clearChat;
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

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public Boolean getMarkPriority() {
        return markPriority;
    }

    public void setMarkPriority(Boolean markPriority) {
        this.markPriority = markPriority;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getIsChatHeadDeleted() {
        return isChatHeadDeleted;
    }

    public void setIsChatHeadDeleted(String isChatHeadDeleted) {
        this.isChatHeadDeleted = isChatHeadDeleted;
    }
}
