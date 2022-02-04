package com.meest.medley_camera2.model.gallery;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class GalleryPhotoModel implements Parcelable {

    private String picturName;
    private String picturePath;
    private String pictureSize;
    private String imageText;
    private boolean isVideo;


    public GalleryPhotoModel() {

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

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public static Creator<GalleryPhotoModel> getCREATOR() {
        return CREATOR;
    }

    private String type;
    private String thumbnail;
    private Date dateTime;


    protected GalleryPhotoModel(Parcel in) {
        picturName = in.readString();
        picturePath = in.readString();
        pictureSize = in.readString();
        imageText = in.readString();
        isVideo = in.readByte() != 0;
        type = in.readString();
        thumbnail = in.readString();
    }

    public static final Creator<GalleryPhotoModel> CREATOR = new Creator<GalleryPhotoModel>() {
        @Override
        public GalleryPhotoModel createFromParcel(Parcel in) {
            return new GalleryPhotoModel(in);
        }

        @Override
        public GalleryPhotoModel[] newArray(int size) {
            return new GalleryPhotoModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(picturName);
        dest.writeString(picturePath);
        dest.writeString(pictureSize);
        dest.writeString(imageText);
        dest.writeByte((byte) (isVideo ? 1 : 0));
        dest.writeString(type);
        dest.writeString(thumbnail);
    }
}
