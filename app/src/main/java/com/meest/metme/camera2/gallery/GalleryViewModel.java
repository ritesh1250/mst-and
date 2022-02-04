package com.meest.metme.camera2.gallery;

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

public class GalleryViewModel {

    public String image;

    public ArrayList<GalleryPhotoModel> selectedGalleryPhotoList;
    public GalleryPhotoModel galleryPhotoModel;
    public GalleryPhotoActivity context;
    public ObservableBoolean isChecked = new ObservableBoolean(false);
    public ObservableBoolean isClickable = new ObservableBoolean(true);
    public boolean isVideo;

    public GalleryViewModel(GalleryPhotoModel galleryPhotoModel, GalleryPhotoActivity context) {

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

    public void onImageClick(){

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", galleryPhotoModel.getPicturePath());
        context.setResult(Activity.RESULT_OK, returnIntent);
        context.finish();


    }




    public void onImageLongClick(){
        Log.d("GalleryPhotoModel", "onLongClick");


    }

    public void onCheckedChange(CompoundButton button, boolean check) {
        Log.d("Z1D1", "onCheckedChange: " + check);


        if(check) {
            isChecked.set(true);
            context.onClick(galleryPhotoModel, check);
        }else{
            isChecked.set(false);
            context.onClick(galleryPhotoModel,check);
        }

    }



}
