package com.meest.videomvvmmodule.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FollowingVideoResponse {
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
            @SerializedName("isFABOpen")
            @Expose
            boolean isFABOpen;

            public boolean isFABOpen() {
                return isFABOpen;
            }

            public void setFABOpen(boolean FABOpen) {
                isFABOpen = FABOpen;
            }

            @SerializedName("audio_file")
            @Expose
            private String audioFile;
            @SerializedName("hashtags")
            @Expose
            private List<String> hashtags = null;
            @SerializedName("thumbnail")
            @Expose
            private String thumbnail;
            @SerializedName("thumbnail_image")
            @Expose
            private String thumbnailImage;
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("userId")
            @Expose
            private String userId;
            @SerializedName("videoURL")
            @Expose
            private String videoURL;

            public User getUser() {
                return user;
            }

            public void setUser(User user) {
                this.user = user;
            }

            public AudioData getAudioDataModel() {
                return audioDataModel;
            }

            public void setAudioDataModel(AudioData audioDataModel) {
                this.audioDataModel = audioDataModel;
            }

            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("likes")
            @Expose
            private int likes;
            @SerializedName("comments")
            @Expose
            private int comments;
            @SerializedName("isFriend")
            @Expose
            private String isFriend;
            @SerializedName("views")
            @Expose
            private String views;
            @SerializedName("isLiked")
            @Expose
            private String isLiked;
            @SerializedName("user")
            @Expose
            private User user;
            @SerializedName("isPrivate")
            @Expose
            private String isPrivate ;
            @SerializedName("isAllowComment")
            @Expose
            private boolean isAllowComment;
            @SerializedName("isAllowDownload")
            @Expose
            private Boolean isAllowDownload;
            @SerializedName("isAllowDuet")
            @Expose
            private Boolean isAllowDuet;
            @SerializedName("isAllowTrio")
            @Expose

            private Boolean isAllowTrio;

            public int getLikes() {
                return likes;
            }

            public void setLikes(int likes) {
                this.likes = likes;
            }

            @SerializedName("audio")
            @Expose
            private AudioData audioDataModel;

            public String getAudioFile() {
                return audioFile;
            }

            public void setAudioFile(String audioFile) {
                this.audioFile = audioFile;
            }

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

            public String getThumbnailImage() {
                return thumbnailImage;
            }

            public void setThumbnailImage(String thumbnailImage) {
                this.thumbnailImage = thumbnailImage;
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

            public Integer getComments() {
                return comments;
            }

            public void setComments(Integer comments) {
                this.comments = comments;
            }


            public String getViews() {
                return views;
            }

            public void setViews(String views) {
                this.views = views;
            }

            public boolean isFriend() {
                return isFriend.equals("1");
            }

            public void setIsFriend(String isFriend) {
                this.isFriend = isFriend;
            }

            public boolean isLiked() {
                return isLiked.equals("1");
            }

            public void setIsLiked(String isLiked) {
                this.isLiked = isLiked;
            }

            public boolean isPrivate() {
                return isPrivate.equals("true");
            }

            public void setIsPrivate(String isPrivate) {
                this.isPrivate = isPrivate;
            }

            public boolean isAllowComment() {
                return isAllowComment;
            }

            public void setAllowComment(boolean allowComment) {
                isAllowComment = allowComment;
            }

            public Boolean getAllowDownload() {
                return isAllowDownload == null ? true : isAllowDownload;
            }

            public void setAllowDownload(Boolean allowDownload) {
                isAllowDownload = allowDownload;
            }

            public Boolean getAllowDuet() {
                return isAllowDuet == null ? true : isAllowDuet;
            }

            public void setAllowDuet(Boolean allowDuet) {
                isAllowDuet = allowDuet;
            }

            public Boolean getAllowTrio() {
                return isAllowTrio == null ? true : isAllowTrio;
            }

            public void setAllowTrio(Boolean allowTrio) {
                isAllowTrio = allowTrio;
            }

            public class User {

                @SerializedName("displayPicture")
                @Expose
                private String displayPicture;
                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("username")
                @Expose
                private String username;

                @SerializedName("firstName")
                @Expose
                private String firstName;

                public String getFirstName() {
                    return firstName;
                }

                public void setFirstName(String firstName) {
                    this.firstName = firstName;
                }

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

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }
            }

            public class AudioData{
                @SerializedName("audioURL")
                @Expose
                private String audioURL;
                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("catagoryId")
                @Expose
                private String catagoryId;
                @SerializedName("description")
                @Expose
                private String description;
                @SerializedName("approvalStatus")
                @Expose
                private String approvalStatus;
                @SerializedName("status")
                @Expose
                private Boolean status;

                public String getAudioURL() {
                    return audioURL;
                }

                public void setAudioURL(String audioURL) {
                    this.audioURL = audioURL;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getCatagoryId() {
                    return catagoryId;
                }

                public void setCatagoryId(String catagoryId) {
                    this.catagoryId = catagoryId;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getApprovalStatus() {
                    return approvalStatus;
                }

                public void setApprovalStatus(String approvalStatus) {
                    this.approvalStatus = approvalStatus;
                }

                public Boolean getStatus() {
                    return status;
                }

                public void setStatus(Boolean status) {
                    this.status = status;
                }
            }
        }

    }
}
