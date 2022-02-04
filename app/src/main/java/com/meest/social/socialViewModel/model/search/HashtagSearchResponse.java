package com.meest.social.socialViewModel.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HashtagSearchResponse {

    @SerializedName("code")
    @Expose
    private Integer code;

    public Datum getData() {
        return data;
    }

    public void setData(Datum data) {
        this.data = data;
    }

    @SerializedName("data")
    @Expose
    private Datum data = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }



    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


    public class Datum {

        @SerializedName("count")
        @Expose
        private Integer count;

        @SerializedName("rows")
        @Expose
        private List<Rows> rows;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<Rows> getRows() {
            return rows;
        }

        public void setRows(List<Rows> rows) {
            this.rows = rows;
        }

        public class Rows{
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("displayPicture")
            @Expose
            private String displayPicture;
            @SerializedName("dislayType")
            @Expose
            private String dislayType;

            @SerializedName("hashtag")
            @Expose
            private String hashtag;
            @SerializedName("count")
            @Expose
            private Integer count;
            @SerializedName("addedBy")
            @Expose
            private String addedBy;
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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDisplayPicture() {
                return displayPicture;
            }

            public void setDisplayPicture(String displayPicture) {
                this.displayPicture = displayPicture;
            }

            public String getDislayType() {
                return dislayType;
            }

            public void setDislayType(String dislayType) {
                this.dislayType = dislayType;
            }

            public String getHashtag() {
                return hashtag;
            }

            public void setHashtag(String hashtag) {
                this.hashtag = hashtag;
            }

            public Integer getCount() {
                return count;
            }

            public void setCount(Integer count) {
                this.count = count;
            }

            public String getAddedBy() {
                return addedBy;
            }

            public void setAddedBy(String addedBy) {
                this.addedBy = addedBy;
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
        }
    }





}
