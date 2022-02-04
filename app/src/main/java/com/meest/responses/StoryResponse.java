package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoryResponse {

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
        @SerializedName("seen")
        @Expose
        private Integer seen;
        @SerializedName("unseen")
        @Expose
        private Integer unseen;

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

        public Integer getSeen() {
            return seen;
        }

        public void setSeen(Integer seen) {
            this.seen = seen;
        }

        public Integer getUnseen() {
            return unseen;
        }

        public void setUnseen(Integer unseen) {
            this.unseen = unseen;
        }

    }

    public class Row {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("story")
        @Expose
        private String story;
        @SerializedName("caption")
        @Expose
        private String caption;
        @SerializedName("hashTags")
        @Expose
        private List<String> hashTags = null;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("image")
        @Expose
        private Boolean image;
        @SerializedName("viewed")
        @Expose
        private Integer viewed;
        @SerializedName("user")
        @Expose
        private User user;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStory() {
            return story;
        }

        public void setStory(String story) {
            this.story = story;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public List<String> getHashTags() {
            return hashTags;
        }

        public void setHashTags(List<String> hashTags) {
            this.hashTags = hashTags;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Boolean getImage() {
            return image;
        }

        public void setImage(Boolean image) {
            this.image = image;
        }

        public Integer getViewed() {
            return viewed == null ? 0 : viewed;
        }

        public void setViewed(Integer viewed) {
            this.viewed = viewed;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

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
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("email")
        @Expose
        private String email;

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

}
