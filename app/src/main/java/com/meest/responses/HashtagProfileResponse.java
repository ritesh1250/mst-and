
package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HashtagProfileResponse {


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

        @SerializedName("videos")
        @Expose
        private Videos videos;
        @SerializedName("postedBy")
        @Expose
        private PostedBy postedBy;

        public Videos getVideos() {
            return videos;
        }

        public void setVideos(Videos videos) {
            this.videos = videos;
        }

        public PostedBy getPostedBy() {
            return postedBy;
        }

        public void setPostedBy(PostedBy postedBy) {
            this.postedBy = postedBy;
        }




        public class Videos {

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



            public class Row {

                @SerializedName("hashtags")
                @Expose
                private List<String> hashtags = null;
                @SerializedName("thumbnail")
                @Expose
                private String thumbnail;
                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("userId")
                @Expose
                private String userId;
                @SerializedName("videoURL")
                @Expose
                private String videoURL;
                @SerializedName("description")
                @Expose
                private String description;
                @SerializedName("title")
                @Expose
                private String title;
                @SerializedName("approvalStatus")
                @Expose
                private String approvalStatus;
                @SerializedName("isPrivate")
                @Expose
                private Boolean isPrivate;
                @SerializedName("status")
                @Expose
                private Boolean status;


                @SerializedName("user")
                @Expose
                private User user;

                public List<String> getHashtags() {
                    return hashtags;
                }

                public void setHashtags(List<String> hashtags) {
                    this.hashtags = hashtags;
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

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getVideoURL() {
                    return videoURL;
                }

                public void setVideoURL(String videoURL) {
                    this.videoURL = videoURL;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getApprovalStatus() {
                    return approvalStatus;
                }

                public void setApprovalStatus(String approvalStatus) {
                    this.approvalStatus = approvalStatus;
                }

                public Boolean getIsPrivate() {
                    return isPrivate;
                }

                public void setIsPrivate(Boolean isPrivate) {
                    this.isPrivate = isPrivate;
                }

                public Boolean getStatus() {
                    return status;
                }

                public void setStatus(Boolean status) {
                    this.status = status;
                }



                public User getUser() {
                    return user;
                }

                public void setUser(User user) {
                    this.user = user;
                }


                public class User {

                    @SerializedName("displayPicture")
                    @Expose
                    private String displayPicture;
                    @SerializedName("id")
                    @Expose
                    private String id;
                    @SerializedName("firstName")
                    @Expose
                    private String firstName;
                    @SerializedName("lastName")
                    @Expose
                    private String lastName;

                    public String getDisplayPicture() {
                        return displayPicture;
                    }

                    public void setDisplayPicture(String displayPicture) {
                        this.displayPicture = displayPicture;
                    }

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
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

                }


            }




        }



        public class PostedBy {

            @SerializedName("id")
            @Expose
            private String id;
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


            @SerializedName("isFriend")
            @Expose
            private Integer isFriend;
            @SerializedName("user")
            @Expose
            private User_ user;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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



            public Integer getIsFriend() {
                return isFriend;
            }

            public void setIsFriend(Integer isFriend) {
                this.isFriend = isFriend;
            }

            public User_ getUser() {
                return user;
            }

            public void setUser(User_ user) {
                this.user = user;
            }



            public class User_ {

                @SerializedName("displayPicture")
                @Expose
                private String displayPicture;
                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("firstName")
                @Expose
                private String firstName;
                @SerializedName("lastName")
                @Expose
                private String lastName;

                public String getDisplayPicture() {
                    return displayPicture;
                }

                public void setDisplayPicture(String displayPicture) {
                    this.displayPicture = displayPicture;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
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

            }

        }

    }

}
