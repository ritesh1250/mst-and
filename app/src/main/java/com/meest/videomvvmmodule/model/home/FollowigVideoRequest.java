package com.meest.videomvvmmodule.model.home;

public class FollowigVideoRequest {
    private boolean svs;
    private int pageNumber;
    private boolean forYou;
    private int pageSize;
    private String userId;

    public boolean isSvs() {
        return svs;
    }

    public void setSvs(boolean svs) {
        this.svs = svs;
    }

    public boolean isForYou() {
        return forYou;
    }

    public void setForYou(boolean forYou) {
        this.forYou = forYou;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
