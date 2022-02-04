package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateNewCampaignResponse {


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

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("isAdvertise")
        @Expose
        private Boolean isAdvertise;
        @SerializedName("reachPeopleWithAddress")
        @Expose
        private Boolean reachPeopleWithAddress;
        @SerializedName("viewerCount")
        @Expose
        private Integer viewerCount;
        @SerializedName("photoViews")
        @Expose
        private Integer photoViews;
        @SerializedName("postReactions")
        @Expose
        private Integer postReactions;
        @SerializedName("postShares")
        @Expose
        private Integer postShares;
        @SerializedName("postComments")
        @Expose
        private Integer postComments;
        @SerializedName("messageConversations")
        @Expose
        private Integer messageConversations;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("campaignTitle")
        @Expose
        private String campaignTitle;
        @SerializedName("campaignText")
        @Expose
        private String campaignText;
        @SerializedName("websiteUrl")
        @Expose
        private String websiteUrl;
        @SerializedName("callToAction")
        @Expose
        private String callToAction;
        @SerializedName("gendder")
//        @SerializedName("gender")
        @Expose
        private String gender;
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
        @SerializedName("addedBy")
        @Expose
        private String addedBy;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("campaignStatus")
        @Expose
        private String campaignStatus;
        @SerializedName("paymentStatus")
        @Expose
        private String paymentStatus;
        @SerializedName("isPreview")
        @Expose
        private Boolean isPreview;
        @SerializedName("duplicate")
        @Expose
        private Boolean duplicate;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobileNumber")
        @Expose
        private String mobileNumber;

        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getIsAdvertise() {
            return isAdvertise;
        }

        public void setIsAdvertise(Boolean isAdvertise) {
            this.isAdvertise = isAdvertise;
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

        public Integer getPostShares() {
            return postShares;
        }

        public void setPostShares(Integer postShares) {
            this.postShares = postShares;
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

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
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

        public String getCallToAction() {
            return callToAction;
        }

        public void setCallToAction(String callToAction) {
            this.callToAction = callToAction;
        }

        public String getGender() {
            return gender==null ? "all" : gender;
        }

        public void setGender(String gender) {
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

        public String getAddedBy() {
            return addedBy;
        }

        public void setAddedBy(String addedBy) {
            this.addedBy = addedBy;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCampaignStatus() {
            return campaignStatus;
        }

        public void setCampaignStatus(String campaignStatus) {
            this.campaignStatus = campaignStatus;
        }

        public Boolean getIsPreview() {
            return isPreview;
        }

        public void setIsPreview(Boolean isPreview) {
            this.isPreview = isPreview;
        }

        public Boolean getDuplicate() {
            return duplicate;
        }

        public void setDuplicate(Boolean duplicate) {
            this.duplicate = duplicate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }


        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public Boolean getReachPeopleWithAddress() {
            return reachPeopleWithAddress;
        }

        public void setReachPeopleWithAddress(Boolean reachPeopleWithAddress) {
            this.reachPeopleWithAddress = reachPeopleWithAddress;
        }
    }
}