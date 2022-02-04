package com.meest.videomvvmmodule.model.videos;

public class VideoData {
    String svs;
    String userId;
    int pageNumber;
    int pageCount;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getSvs() {
        return svs;
    }

    public void setSvs(String svs) {
        this.svs = svs;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
