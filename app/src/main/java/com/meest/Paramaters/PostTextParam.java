
package com.meest.Paramaters;

import java.util.List;

public class PostTextParam {
    private List<PostDatum> postData = null;

    public List<PostDatum> getPostData() {
        return postData;
    }

    public void setPostData(List<PostDatum> postData) {
        this.postData = postData;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getViewPost() {
        return viewPost;
    }

    public void setViewPost(String viewPost) {
        this.viewPost = viewPost;
    }

    public Boolean getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Boolean allowComment) {
        this.allowComment = allowComment;
    }

    private String postType;
    private String viewPost;



    private Boolean allowComment;
    public static class PostDatum {
        private String post;
        private Integer image;

        public PostDatum(String post, Integer image) {
            this.post = post;
            this.image = image;
        }


    }


}
