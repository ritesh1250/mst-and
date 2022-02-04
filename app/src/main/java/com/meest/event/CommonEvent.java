package com.meest.event;

public class CommonEvent {
    String id;
    int position;

    public CommonEvent() {

    }

    public CommonEvent(int position) {
        this.position = position;
    }

    public CommonEvent(int position, String id) {
        this.position = position;
        this.id = id;
    }
}
