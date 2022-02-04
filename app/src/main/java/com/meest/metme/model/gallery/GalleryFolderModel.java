package com.meest.metme.model.gallery;

public class GalleryFolderModel {

    private String path;
    private String FolderName;
    private int numberOfPic;
    private String setType;

    public GalleryFolderModel() {

    }

    public String getPath() {
        return path;
    }

    public GalleryFolderModel(String path, String folderName, int numberOfPic, String firstPic) {
        this.path = path;
        FolderName = folderName;
        this.numberOfPic = numberOfPic;
        this.firstPic = firstPic;
    }

    public String getSetType() {
        return setType;
    }

    public void setSetType(String setType) {
        this.setType = setType;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFolderName() {
        return FolderName;
    }

    public void setFolderName(String folderName) {
        FolderName = folderName;
    }

    public int getNumberOfPic() {
        return numberOfPic;
    }

    public void setNumberOfPic(int numberOfPic) {
        this.numberOfPic = numberOfPic;
    }

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    private String firstPic;

    public void addpics(){
        this.numberOfPic++;
    }


}
