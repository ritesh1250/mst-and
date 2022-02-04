package com.meest.model;

public class AdMediaData {

    String heading, subHeading, websiteUrl, buttonType, fileType, fileUrl;

    public String getHeading() {
        return heading == null ? "" : heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubHeading() {
        return subHeading == null ? "" : subHeading;
    }

    public void setSubHeading(String subHeading) {
        this.subHeading = subHeading;
    }

    public String getWebsiteUrl() {
        return websiteUrl == null ?"" : websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getButtonType() {
        return buttonType == null ?"" : buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public String getFileType() {
        return fileType == null ? "" : fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl ==null ?"" : fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
