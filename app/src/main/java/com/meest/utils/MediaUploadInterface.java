package com.meest.utils;

import com.meest.mediapikcer.utils.PictureFacer;

import java.util.ArrayList;
import java.util.List;

public interface MediaUploadInterface {
    void getUploadedMedia();
    void getUploadedMedia(int requestCode, ArrayList<String> mediaUrlList, boolean isImage,
                          List<PictureFacer> localMedia);
}
