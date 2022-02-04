package com.meest.model;

public class HastagItem {
    public HastagItem(String hashtag_name, String videos) {
        this.hashtag_name = hashtag_name;
        this.videos = videos;
    }

    private String hashtag_name;
    private String videos;

    public String getHashtag_name() {
        return hashtag_name;
    }

    public String getVideos() {
        return videos;
    }

}
