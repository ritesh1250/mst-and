package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CampaignViewResponse {

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

    public static class OtherImageVideo {

        @SerializedName("fileUrl")
        @Expose
        private String fileUrl;
        @SerializedName("heading")
        @Expose
        private String heading;
        @SerializedName("fileType")
        @Expose
        private String fileType;
        @SerializedName("subHeading")
        @Expose
        private String subHeading;
        @SerializedName("websiteUrl")
        @Expose
        private String websiteUrl;

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getHeading() {
            return heading;
        }

        public void setHeading(String heading) {
            this.heading = heading;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getSubHeading() {
            return subHeading;
        }

        public void setSubHeading(String subHeading) {
            this.subHeading = subHeading;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

    }

    public class Data {

        @SerializedName("fileURL")
        @Expose
        private String fileURL;
        @SerializedName("campaignTitle")
        @Expose
        private String campaignTitle;
        @SerializedName("campaignText")
        @Expose
        private String campaignText;
        @SerializedName("websiteUrl")
        @Expose
        private String websiteUrl;
        @SerializedName("gender")
        @Expose
        private List<String> gender = null;
        @SerializedName("startAge")
        @Expose
        private String startAge;
        @SerializedName("endAge")
        @Expose
        private String endAge;
        @SerializedName("location")
        @Expose
        private List<String> location = null;
        @SerializedName("campaignType")
        @Expose
        private String campaignType;
        @SerializedName("otherImageVideos")
        @Expose
        private List<OtherImageVideo> otherImageVideos = null;
        @SerializedName("startDate")
        @Expose
        private String startDate;
        @SerializedName("endDate")
        @Expose
        private String endDate;
        @SerializedName("dailyBudget")
        @Expose
        private String dailyBudget;
        @SerializedName("totalAmmount")
        @Expose
        private String totalAmmount;
        @SerializedName("campaignStatus")
        @Expose
        private String campaignStatus;
        @SerializedName("viewerCount")
        @Expose
        private Integer viewerCount;
        @SerializedName("photoViews")
        @Expose
        private Integer photoViews;
        @SerializedName("postReactions")
        @Expose
        private Integer postReactions;
        @SerializedName("postComments")
        @Expose
        private Integer postComments;
        @SerializedName("messageConversations")
        @Expose
        private Integer messageConversations;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("postShares")
        @Expose
        private Integer postShares;
        @SerializedName("totalUsers")
        @Expose
        private Integer totalUsers;
        @SerializedName("totalWeeks")
        @Expose
        private Integer totalWeeks;
        @SerializedName("totalWeeksDay")
        @Expose
        private Integer totalWeeksDay;
        @SerializedName("totalPostEngagement")
        @Expose
        private Integer totalPostEngagement;
        @SerializedName("weekPostEngagement")
        @Expose
        private Integer weekPostEngagement;
        @SerializedName("oneDayPostEngagement")
        @Expose
        private Integer oneDayPostEngagement;
        @SerializedName("maleCount")
        @Expose
        private Integer maleCount;
        @SerializedName("malePostEngagement")
        @Expose
        private Integer malePostEngagement;
        @SerializedName("maleCostPerEngagement")
        @Expose
        private Integer maleCostPerEngagement;
        @SerializedName("femaleCount")
        @Expose
        private Integer femaleCount;
        @SerializedName("totalMaleReached")
        @Expose
        private Integer totalMaleReached;
        @SerializedName("totalFemaleReached")
        @Expose
        private Integer totalFemaleReached;
        @SerializedName("femalePostEngagement")
        @Expose
        private Integer femalePostEngagement;
        @SerializedName("femaleCostPerEngagement")
        @Expose
        private Integer femaleCostPerEngagement;
        @SerializedName("ageZeroToThirty")
        @Expose
        private Integer ageZeroToThirty;
        @SerializedName("ZeroToThirtyPostEngagement")
        @Expose
        private Integer zeroToThirtyPostEngagement;
        @SerializedName("ZeroToThirtyCostPerEngagement")
        @Expose
        private Integer zeroToThirtyCostPerEngagement;
        @SerializedName("ageThirtyToSixty")
        @Expose
        private Integer ageThirtyToSixty;
        @SerializedName("ThirtyToSixtyePostEngagement")
        @Expose
        private Integer thirtyToSixtyePostEngagement;
        @SerializedName("totalCountZtoThirtyS")
        @Expose
        private Integer totalCountZtoThirtyS;
        @SerializedName("totalCountTtoS")
        @Expose
        private Integer totalCountTtoS;
        @SerializedName("ThirtyToSixtyCostPerEngagement")
        @Expose
        private Integer thirtyToSixtyCostPerEngagement;

        public String getFileURL() {
            return fileURL;
        }

        public void setFileURL(String fileURL) {
            this.fileURL = fileURL;
        }

        public String getCampaignTitle() {
            return campaignTitle;
        }

        public void setCampaignTitle(String campaignTitle) {
            this.campaignTitle = campaignTitle;
        }

        public String getCampaignText() {
            return campaignText;
        }

        public void setCampaignText(String campaignText) {
            this.campaignText = campaignText;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

        public List<String> getGender() {
            return gender;
        }

        public void setGender(List<String> gender) {
            this.gender = gender;
        }

        public String getStartAge() {
            return startAge;
        }

        public void setStartAge(String startAge) {
            this.startAge = startAge;
        }

        public String getEndAge() {
            return endAge;
        }

        public void setEndAge(String endAge) {
            this.endAge = endAge;
        }

        public List<String> getLocation() {
            return location;
        }

        public void setLocation(List<String> location) {
            this.location = location;
        }

        public String getCampaignType() {
            return campaignType;
        }

        public void setCampaignType(String campaignType) {
            this.campaignType = campaignType;
        }

        public List<OtherImageVideo> getOtherImageVideos() {
            return otherImageVideos;
        }

        public void setOtherImageVideos(List<OtherImageVideo> otherImageVideos) {
            this.otherImageVideos = otherImageVideos;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getDailyBudget() {
            return dailyBudget;
        }

        public void setDailyBudget(String dailyBudget) {
            this.dailyBudget = dailyBudget;
        }

        public String getTotalAmmount() {
            return totalAmmount;
        }

        public void setTotalAmmount(String totalAmmount) {
            this.totalAmmount = totalAmmount;
        }

        public String getCampaignStatus() {
            return campaignStatus;
        }

        public void setCampaignStatus(String campaignStatus) {
            this.campaignStatus = campaignStatus;
        }

        public Integer getViewerCount() {
            return viewerCount;
        }

        public void setViewerCount(Integer viewerCount) {
            this.viewerCount = viewerCount;
        }

        public Integer getPhotoViews() {
            return photoViews;
        }

        public void setPhotoViews(Integer photoViews) {
            this.photoViews = photoViews;
        }

        public Integer getPostReactions() {
            return postReactions;
        }

        public void setPostReactions(Integer postReactions) {
            this.postReactions = postReactions;
        }

        public Integer getPostComments() {
            return postComments;
        }

        public void setPostComments(Integer postComments) {
            this.postComments = postComments;
        }

        public Integer getMessageConversations() {
            return messageConversations;
        }

        public void setMessageConversations(Integer messageConversations) {
            this.messageConversations = messageConversations;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getPostShares() {
            return postShares;
        }

        public void setPostShares(Integer postShares) {
            this.postShares = postShares;
        }

        public Integer getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(Integer totalUsers) {
            this.totalUsers = totalUsers;
        }

        public Integer getTotalWeeks() {
            return totalWeeks;
        }

        public void setTotalWeeks(Integer totalWeeks) {
            this.totalWeeks = totalWeeks;
        }

        public Integer getTotalWeeksDay() {
            return totalWeeksDay;
        }

        public void setTotalWeeksDay(Integer totalWeeksDay) {
            this.totalWeeksDay = totalWeeksDay;
        }

        public Integer getTotalPostEngagement() {
            return totalPostEngagement;
        }

        public void setTotalPostEngagement(Integer totalPostEngagement) {
            this.totalPostEngagement = totalPostEngagement;
        }

        public Integer getWeekPostEngagement() {
            return weekPostEngagement;
        }

        public void setWeekPostEngagement(Integer weekPostEngagement) {
            this.weekPostEngagement = weekPostEngagement;
        }

        public Integer getOneDayPostEngagement() {
            return oneDayPostEngagement;
        }

        public void setOneDayPostEngagement(Integer oneDayPostEngagement) {
            this.oneDayPostEngagement = oneDayPostEngagement;
        }

        public Integer getMaleCount() {
            return maleCount;
        }

        public void setMaleCount(Integer maleCount) {
            this.maleCount = maleCount;
        }

        public Integer getMalePostEngagement() {
            return malePostEngagement;
        }

        public void setMalePostEngagement(Integer malePostEngagement) {
            this.malePostEngagement = malePostEngagement;
        }

        public Integer getMaleCostPerEngagement() {
            return maleCostPerEngagement;
        }

        public void setMaleCostPerEngagement(Integer maleCostPerEngagement) {
            this.maleCostPerEngagement = maleCostPerEngagement;
        }

        public Integer getFemaleCount() {
            return femaleCount;
        }

        public void setFemaleCount(Integer femaleCount) {
            this.femaleCount = femaleCount;
        }

        public Integer getTotalMaleReached() {
            return totalMaleReached;
        }

        public void setTotalMaleReached(Integer totalMaleReached) {
            this.totalMaleReached = totalMaleReached;
        }

        public Integer getTotalFemaleReached() {
            return totalFemaleReached;
        }

        public void setTotalFemaleReached(Integer totalFemaleReached) {
            this.totalFemaleReached = totalFemaleReached;
        }

        public Integer getFemalePostEngagement() {
            return femalePostEngagement;
        }

        public void setFemalePostEngagement(Integer femalePostEngagement) {
            this.femalePostEngagement = femalePostEngagement;
        }

        public Integer getFemaleCostPerEngagement() {
            return femaleCostPerEngagement;
        }

        public void setFemaleCostPerEngagement(Integer femaleCostPerEngagement) {
            this.femaleCostPerEngagement = femaleCostPerEngagement;
        }

        public Integer getAgeZeroToThirty() {
            return ageZeroToThirty;
        }

        public void setAgeZeroToThirty(Integer ageZeroToThirty) {
            this.ageZeroToThirty = ageZeroToThirty;
        }

        public Integer getZeroToThirtyPostEngagement() {
            return zeroToThirtyPostEngagement;
        }

        public void setZeroToThirtyPostEngagement(Integer zeroToThirtyPostEngagement) {
            this.zeroToThirtyPostEngagement = zeroToThirtyPostEngagement;
        }

        public Integer getZeroToThirtyCostPerEngagement() {
            return zeroToThirtyCostPerEngagement;
        }

        public void setZeroToThirtyCostPerEngagement(Integer zeroToThirtyCostPerEngagement) {
            this.zeroToThirtyCostPerEngagement = zeroToThirtyCostPerEngagement;
        }

        public Integer getAgeThirtyToSixty() {
            return ageThirtyToSixty;
        }

        public void setAgeThirtyToSixty(Integer ageThirtyToSixty) {
            this.ageThirtyToSixty = ageThirtyToSixty;
        }

        public Integer getThirtyToSixtyePostEngagement() {
            return thirtyToSixtyePostEngagement;
        }

        public void setThirtyToSixtyePostEngagement(Integer thirtyToSixtyePostEngagement) {
            this.thirtyToSixtyePostEngagement = thirtyToSixtyePostEngagement;
        }

        public Integer getTotalCountZtoThirtyS() {
            return totalCountZtoThirtyS;
        }

        public void setTotalCountZtoThirtyS(Integer totalCountZtoThirtyS) {
            this.totalCountZtoThirtyS = totalCountZtoThirtyS;
        }

        public Integer getTotalCountTtoS() {
            return totalCountTtoS;
        }

        public void setTotalCountTtoS(Integer totalCountTtoS) {
            this.totalCountTtoS = totalCountTtoS;
        }

        public Integer getThirtyToSixtyCostPerEngagement() {
            return thirtyToSixtyCostPerEngagement;
        }

        public void setThirtyToSixtyCostPerEngagement(Integer thirtyToSixtyCostPerEngagement) {
            this.thirtyToSixtyCostPerEngagement = thirtyToSixtyCostPerEngagement;
        }

    }
}
