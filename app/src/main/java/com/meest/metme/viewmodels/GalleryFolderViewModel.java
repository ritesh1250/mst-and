package com.meest.metme.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.metme.GalleryPhotoActivity;
import com.meest.metme.model.gallery.GalleryFolderModel;

public class GalleryFolderViewModel {

    public String image;
    public String name;
    public GalleryFolderModel galleryFolderModel;
    public Activity context;
    public String chatHeadId;
    public String secondColor;
    public String mediType, whereFrom;

    int LAUNCH_SECOND_ACTIVITY = 1231;

    public GalleryFolderViewModel(GalleryFolderModel galleryFolderModel, Activity context, String chatHeadId, String secondColor,  String mediType, String whereFrom) {
        this.galleryFolderModel = galleryFolderModel;
        this.context = context;
        this.image = galleryFolderModel.getFirstPic();
        this.name = galleryFolderModel.getFolderName()+"("+galleryFolderModel.getNumberOfPic()+")";
        this.chatHeadId=chatHeadId;
        this.secondColor=secondColor;
        this.mediType=mediType;
        this.whereFrom=whereFrom;
    }


    public void onGalleryFolderClick(){


        Intent intent = new Intent(context, GalleryPhotoActivity.class);
        intent.putExtra("whereFrom", whereFrom);
        intent.putExtra("folderPath",galleryFolderModel.getPath());
        intent.putExtra("chatHeadId",chatHeadId);
        intent.putExtra("secondColor",secondColor);
        intent.putExtra("mediatype",mediType);
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
