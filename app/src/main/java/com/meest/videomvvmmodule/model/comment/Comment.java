
package com.meest.videomvvmmodule.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Comment {

    @Expose
    private List<Data> data;
    @Expose
    private String message;
    @Expose
    private Boolean status;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static class Data {

        @Expose
        private String comment;
        @SerializedName("comments_id")
        private String commentsId;
        @SerializedName("created_date")
        private String createdDate;
        @SerializedName("full_name")
        private String fullName;
        @SerializedName("is_verify")
        private String isVerify;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("first_name")
        private String firstName;
        @SerializedName("comment_like")
        private String commentLike;


        public boolean getCommentLike() {
            return commentLike.equals("1");
        }

        public void setCommentLike(String commentLike) {
            this.commentLike = commentLike;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        @SerializedName("user_profile")
        private String userProfile;
        @Expose
        private List<CommentsReplyData> comments_reply;

        public List<CommentsReplyData> getComments_reply() {
            return comments_reply;
        }

        public void setComments_reply(List<CommentsReplyData> comments_reply) {
            this.comments_reply = comments_reply;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getCommentsId() {
            return commentsId;
        }

        public void setCommentsId(String commentsId) {
            this.commentsId = commentsId;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public boolean getIsVerify() {
            return isVerify.equals("1");
        }

        public void setIsVerify(String isVerify) {
            this.isVerify = isVerify;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserProfile() {
            return userProfile;
        }

        public void setUserProfile(String userProfile) {
            this.userProfile = userProfile;
        }

        public static class CommentsReplyData{

            @SerializedName("comment_comments_id")
            private String commentreplyId;
            @SerializedName("comment_id")
            private String commentId;
            @SerializedName("user_id")
            private String userId;
            @SerializedName("comment")
            private String comment;
            @SerializedName("status")
            private String status;
            @SerializedName("first_name")
            private String firstName;
            @SerializedName("user_profile")
            private String userProfile;

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getUserProfile() {
                return userProfile;
            }

            public void setUserProfile(String userProfile) {
                this.userProfile = userProfile;
            }

            public String getCommentreplyId() {
                return commentreplyId;
            }

            public void setCommentreplyId(String commentreplyId) {
                this.commentreplyId = commentreplyId;
            }

            public String getCommentId() {
                return commentId;
            }

            public void setCommentId(String commentId) {
                this.commentId = commentId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }

        @Override
        public String toString() {
            return "Data{" +
                    "comment='" + comment + '\'' +
                    ", commentsId='" + commentsId + '\'' +
                    ", createdDate='" + createdDate + '\'' +
                    ", fullName='" + fullName + '\'' +
                    ", isVerify='" + isVerify + '\'' +
                    ", userId='" + userId + '\'' +
                    ", userName='" + userName + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", commentLike='" + commentLike + '\'' +
                    ", userProfile='" + userProfile + '\'' +
                    ", comments_reply=" + comments_reply +
                    '}';
        }
    }

}
