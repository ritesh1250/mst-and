package com.meest.videomvvmmodule.model.user;

/**
 * Created by sanket kumar.
 */
public class VideoSearch_People {

    String key ;
    int pageNumber ;
    int pageCount ;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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
}
