package com.meest.metme.camera2.gallery2.viewmodels;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;

import com.bumptech.glide.Glide;
import com.meest.metme.GalleryPhotoActivity;
import com.meest.metme.model.gallery.GalleryPhotoModel;

import java.util.ArrayList;

public class GalleryPhotoViewModelCamera2 {

    public String image;

    public ArrayList<GalleryPhotoModel> selectedGalleryPhotoList;
    public GalleryPhotoModel galleryPhotoModel;
    public Activity context;
    public ObservableBoolean isChecked = new ObservableBoolean(false);
    public ObservableBoolean isClickable = new ObservableBoolean(true);
    public boolean isVideo;

    public GalleryPhotoViewModelCamera2(GalleryPhotoModel galleryPhotoModel, Activity context) {

        this.galleryPhotoModel  = galleryPhotoModel;
        this.image = galleryPhotoModel.getPicturePath();
        this.isVideo = galleryPhotoModel.isVideo();
        this.context =context;
    }

    @BindingAdapter("bindGalleryPhoto")
    public static void bindGalleryPhoto(ImageView imageView , String imageUrl){

        if (!imageUrl.isEmpty()){

            Glide.with(imageView.getContext()).load(imageUrl)
                    .into(imageView);
        }

    }

    private static final String TAG = "GalleryPhotoViewModelCa";
    public void onImageClick(){

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", galleryPhotoModel.getPicturePath());
        context.setResult(Activity.RESULT_OK, returnIntent);
        context.finish();


    }




    public void onImageLongClick(){
        Log.d("GalleryPhotoModel", "onLongClick");


    }




}
