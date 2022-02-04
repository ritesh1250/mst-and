package com.meest.responses;

import java.util.List;

public class GetPendingUserResponse {
    int code;
    Data data;
    boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public class Data {

        User user;
        Result result;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }

    public class User{
        String displayPicture;
        String id;
        String firstName;
        String lastName;
        int totalFollowers;
        int totalPosts;
        String createdAt;
        List<Friends> friends;

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
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

        public int getTotalFollowers() {
            return totalFollowers;
        }

        public void setTotalFollowers(int totalFollowers) {
            this.totalFollowers = totalFollowers;
        }

        public int getTotalPosts() {
            return totalPosts;
        }

        public void setTotalPosts(int totalPosts) {
            this.totalPosts = totalPosts;
        }

        public List<Friends> getFriends() {
            return friends;
        }

        public void setFriends(List<Friends> friends) {
            this.friends = friends;
        }
    }

    public class Result{
        String id;
        String createdAt;

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }


    public class Friends{
        String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
