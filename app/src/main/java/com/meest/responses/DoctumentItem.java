package com.meest.responses;

public class DoctumentItem {

    private String name;
    private String Date;
    private String Size;

    public DoctumentItem(String name, String date, String size) {
        this.name = name;
        Date = date;
        Size = size;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return Date;
    }

    public String getSize() {
        return Size;
    }
}
