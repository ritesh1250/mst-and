package com.meest.responses;

import android.os.Parcel;

import com.meest.model.AdMediaData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CampaignDraftedResponse {

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


    public class Row{

        @SerializedName("fileURL")
        @Expose
        private String fileURL;
        @SerializedName("campaignTitle")
        @Expose
        private String campaignTitle;
        @SerializedName("campaignText")
        @Expose
        private String campaignText;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobileNumber")
        @Expose
        private String mobileNumber;
        @SerializedName("websiteUrl")
        @Expose
        private String websiteUrl;
        @SerializedName("fileType")
        @Expose
        private String fileType;
        @SerializedName("callToAction")
        @Expose
        private String callToAction;
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
        private List<AdMediaData> otherImageVideos;
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
        @SerializedName("accountOverview")
        @Expose
        private String accountOverview;
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
        @SerializedName("paymentStatus")
        @Expose
        private String paymentStatus;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("isPreview")
        @Expose
        private Boolean isPreview;
        @SerializedName("duplicate")
        @Expose
        private Boolean duplicate;
        @SerializedName("isAdvertise")
        @Expose
        private Boolean isAdvertise;
        @SerializedName("postShares")
        @Expose
        private Integer postShares;
        @SerializedName("reachPeopleWithAddress")
        @Expose
        private Boolean reachPeopleWithAddress;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("deletedAt")
        @Expose
        private Object deletedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;


        @SerializedName("adminBlocked")
        @Expose
        private Boolean adminBlocked;

        @SerializedName("isPaused")
        @Expose
        private Boolean isPaused;

        @SerializedName("isApproved")
        @Expose
        private Boolean isApproved;

        protected Row(Parcel in) {
            fileURL = in.readString();
            campaignTitle = in.readString();
            campaignText = in.readString();
            name = in.readString();
            email = in.readString();
            mobileNumber = in.readString();
            websiteUrl = in.readString();
            fileType = in.readString();
            callToAction = in.readString();
            gender = in.createStringArrayList();
            startAge = in.readString();
            endAge = in.readString();
            location = in.createStringArrayList();
            campaignType = in.readString();
            startDate = in.readString();
            endDate = in.readString();
            dailyBudget = in.readString();
            totalAmmount = in.readString();
            addedBy = in.readString();
            userId = in.readString();
            campaignStatus = in.readString();
            accountOverview = in.readString();
            if (in.readByte() == 0) {
                viewerCount = null;
            } else {
                viewerCount = in.readInt();
            }
            if (in.readByte() == 0) {
                photoViews = null;
            } else {
                photoViews = in.readInt();
            }
            if (in.readByte() == 0) {
                postReactions = null;
            } else {
                postReactions = in.readInt();
            }
            if (in.readByte() == 0) {
                postComments = null;
            } else {
                postComments = in.readInt();
            }
            if (in.readByte() == 0) {
                messageConversations = null;
            } else {
                messageConversations = in.readInt();
            }
            paymentStatus = in.readString();
            id = in.readString();
            byte tmpIsPreview = in.readByte();
            isPreview = tmpIsPreview == 0 ? null : tmpIsPreview == 1;
            byte tmpDuplicate = in.readByte();
            duplicate = tmpDuplicate == 0 ? null : tmpDuplicate == 1;
            byte tmpIsAdvertise = in.readByte();
            isAdvertise = tmpIsAdvertise == 0 ? null : tmpIsAdvertise == 1;
            if (in.readByte() == 0) {
                postShares = null;
            } else {
                postShares = in.readInt();
            }
            byte tmpReachPeopleWithAddress = in.readByte();
            reachPeopleWithAddress = tmpReachPeopleWithAddress == 0 ? null : tmpReachPeopleWithAddress == 1;
            byte tmpStatus = in.readByte();
            status = tmpStatus == 0 ? null : tmpStatus == 1;
            createdAt = in.readString();
            updatedAt = in.readString();
        }

        public Boolean getPaused() {
            return isPaused;
        }

        public void setPaused(Boolean paused) {
            isPaused = paused;
        }

        public Boolean getApproved() {
            return isApproved;
        }

        public void setApproved(Boolean approved) {
            isApproved = approved;
        }

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

        public List<AdMediaData> getOtherImageVideos() {
            return otherImageVideos;
        }

        public void setOtherImageVideos(List<AdMediaData> otherImageVideos) {
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

        public String getAccountOverview() {
            return accountOverview;
        }

        public void setAccountOverview(String accountOverview) {
            this.accountOverview = accountOverview;
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

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public Boolean getIsAdvertise() {
            return isAdvertise;
        }

        public void setIsAdvertise(Boolean isAdvertise) {
            this.isAdvertise = isAdvertise;
        }

        public Integer getPostShares() {
            return postShares;
        }

        public void setPostShares(Integer postShares) {
            this.postShares = postShares;
        }

        public Boolean getReachPeopleWithAddress() {
            return reachPeopleWithAddress;
        }

        public void setReachPeopleWithAddress(Boolean reachPeopleWithAddress) {
            this.reachPeopleWithAddress = reachPeopleWithAddress;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
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

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public Boolean getPreview() {
            return isPreview;
        }

        public void setPreview(Boolean preview) {
            isPreview = preview;
        }

        public Boolean getAdvertise() {
            return isAdvertise;
        }

        public void setAdvertise(Boolean advertise) {
            isAdvertise = advertise;
        }

        public Boolean getAdminBlocked() {
            return adminBlocked;
        }

        public void setAdminBlocked(Boolean adminBlocked) {
            this.adminBlocked = adminBlocked;
        }

        public Boolean getIsPaused() {
            return isPaused;
        }

        public void setIsPaused(Boolean isPaused) {
            this.isPaused = isPaused;
        }
    }

    public class Data {

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("rows")
        @Expose
        private List<Row> rows = null;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }

    }
}
