package com.meest.Paramaters;

public class InsightGraphParameter {
    String postId;
    String countOf;
    String type;//'days' 'months', 'years'

    public InsightGraphParameter(String postId, String countOf, String type) {
        this.postId = postId;
        this.countOf = countOf;
        this.type = type;
    }
}
