package com.meest.model;

public class AdsListingItem {

    private int profile_img;
    private String Name;
    private String Startdate;
    private String Enddate;
    private String AmountPaid;

    public int getProfile_img() {
        return profile_img;
    }

    public String getName() {
        return Name;
    }

    public String getStartdate() {
        return Startdate;
    }

    public String getEnddate() {
        return Enddate;
    }

    public String getAmountPaid() {
        return AmountPaid;
    }

    public AdsListingItem(int profile_img, String name, String startdate, String enddate, String amountPaid) {
        this.profile_img = profile_img;
        Name = name;
        Startdate = startdate;
        Enddate = enddate;
        AmountPaid = amountPaid;
    }
}
