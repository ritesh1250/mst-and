package com.meest.metme.camera2.gallery2.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.meest.metme.GalleryPhotoActivity;
import com.meest.metme.camera2.gallery2.Camera2GalleryPhotoActivity;
import com.meest.metme.model.gallery.GalleryFolderModel;

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

    private static final String TAG = "Camera2GalleryFolderVie";
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
