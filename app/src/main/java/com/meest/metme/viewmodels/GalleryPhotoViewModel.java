package com.meest.metme.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.meest.R;
import com.meest.metme.GalleryPhotoActivity;
import com.meest.metme.model.gallery.GalleryPhotoModel;

import java.util.ArrayList;

import timber.log.Timber;

public class GalleryPhotoViewModel {

    public String image;

    public ArrayList<GalleryPhotoModel> selectedGalleryPhotoList;
    public GalleryPhotoModel galleryPhotoModel;
    public GalleryPhotoActivity context;
    public boolean isChecked ;
    public ObservableBoolean isClickable = new ObservableBoolean(true);
    public boolean isVideo;

    public GalleryPhotoViewModel(GalleryPhotoModel galleryPhotoModel, GalleryPhotoActivity context) {

        this.galleryPhotoModel  = galleryPhotoModel;
        this.image = galleryPhotoModel.getPicturePath();
        this.isVideo = galleryPhotoModel.isVideo();
        this.context =context;
        this.isChecked = galleryPhotoModel.isChecked();
    }

    @BindingAdapter("bindGalleryPhoto")
    public static void bindGalleryPhoto(ImageView imageView , String imageUrl){

        if (!imageUrl.isEmpty()){

            Glide.with(imageView.getContext()).load(imageUrl)
                    .into(imageView);
        }

    }

    public void onImageClick(){

        context.onImageClick(galleryPhotoModel, galleryPhotoModel.isChecked());
    /*    Intent returnIntent = new Intent();
        returnIntent.putExtra("result", galleryPhotoModel.getPicturePath());
        context.setResult(Activity.RESULT_OK, returnIntent);
        context.finish();*/

    }




    public void onImageLongClick(){
        Log.d("GalleryPhotoModel", "onLongClick");


    }

    public void onCheckedChange() {
//        Log.d("Z1D1", "onCheckedChange: " + check);
       // button.setChecked(check);
      //  galleryPhotoModel.setChecked(check);

        context.onClick(galleryPhotoModel, galleryPhotoModel.isChecked());

    }



}
