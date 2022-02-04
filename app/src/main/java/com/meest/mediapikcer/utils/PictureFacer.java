package com.meest.mediapikcer.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


public class PictureFacer implements Comparable<PictureFacer> , Parcelable {

    private String picturName;
    private String picturePath;
    private String pictureSize;
    private String imageUri;
    private String type;
    private String thumbnail;


    private Date dateTime;

    protected PictureFacer(Parcel in) {
        picturName = in.readString();
        picturePath = in.readString();
        pictureSize = in.readString();
        imageUri = in.readString();
        type = in.readString();
        thumbnail = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(picturName);
        dest.writeString(picturePath);
        dest.writeString(pictureSize);
        dest.writeString(imageUri);
        dest.writeString(type);
        dest.writeString(thumbnail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PictureFacer> CREATOR = new Creator<PictureFacer>() {
        @Override
        public PictureFacer createFromParcel(Parcel in) {
            return new PictureFacer(in);
        }

        @Override
        public PictureFacer[] newArray(int size) {
            return new PictureFacer[size];
        }
    };

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date datetime) {
        this.dateTime = datetime;
    }

    @Override
    public int compareTo(PictureFacer obj) {
        return getDateTime().compareTo(obj.getDateTime());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PictureFacer() {

    }

    public PictureFacer(String picturName, String picturePath, String pictureSize, String imageUri, String type,String thumbnail) {
        this.picturName = picturName;
        this.picturePath = picturePath;
        this.pictureSize = pictureSize;
        this.imageUri = imageUri;
        this.type = type;
        this.thumbnail = thumbnail;
    }


    public String getPicturName() {
        return picturName;
    }

    public void setPicturName(String picturName) {
        this.picturName = picturName;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getPictureSize() {
        return pictureSize;
    }

    public void setPictureSize(String pictureSize) {
        this.pictureSize = pictureSize;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }




}
