package com.meest.model;

public class AllNotification {
    String name;
    String name2;
    String comment_type;
    String time;

    public AllNotification(String name, String name2, String comment_type, String time) {
        this.name = name;
        this.name2 = name2;
        this.comment_type = comment_type;
        this.time = time;
    }


    public String getName() {
        return name;
    }

    public String getName2() {
        return name2;
    }

    public String getComment_type() {
        return comment_type;
    }

    public String getTime() {
        return time;
    }
}