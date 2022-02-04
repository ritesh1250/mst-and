package com.meest.medley_camera2.camera2.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.meest.medley_camera2.camera2.view.activity.Camera2GalleryPhotoActivity;
import com.meest.medley_camera2.model.gallery.GalleryFolderModel;

public class Camera2GalleryFolderViewModel {

    public String image;
    public String name;
    public GalleryFolderModel galleryFolderModel;
    public Activity context;

    int LAUNCH_SECOND_ACTIVITY = 1231;
    public Camera2GalleryFolderViewModel(GalleryFolderModel galleryFolderModel, Activity context) {
        this.galleryFolderModel = galleryFolderModel;
        this.context = context;
        this.image = galleryFolderModel.getFirstPic();
        this.name = galleryFolderModel.getFolderName()+"("+galleryFolderModel.getNumberOfPic()+")";
    }
    public void onGalleryFolderClick(){
        Intent intent = new Intent(context, Camera2GalleryPhotoActivity.class);
        intent.putExtra("folderPath",galleryFolderModel.getPath());
        context.startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    }
    @BindingAdapter("bindGalleryFolder")
    public static void bindGalleryFolder(ImageView imageView , String imageUrl){

        if (!imageUrl.isEmpty()){

            Glide.with(imageView.getContext()).load(imageUrl)
                    .into(imageView);
        }

    }
}
