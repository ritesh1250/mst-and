package com.meest.medley_camera2.viewmodels;

import android.text.Editable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.medley_camera2.ImageTextActivity;
import com.meest.medley_camera2.model.gallery.GalleryPhotoModel;

import java.util.ArrayList;


public class ImageTextViewModel {

    public GalleryPhotoModel galleryPhotoModel;

    ArrayList<GalleryPhotoModel> galleryPhotoModelList;
    public static ImageTextActivity context;
    public String image;
    public boolean isVideo;

    public ImageTextViewModel(GalleryPhotoModel galleryPhotoModel, ArrayList<GalleryPhotoModel> galleryPhotoModelList, ImageTextActivity context) {

        this.galleryPhotoModel = galleryPhotoModel;
        this.galleryPhotoModelList = galleryPhotoModelList;
        this.context = context;
        this.image = galleryPhotoModel.getPicturePath();
        this.isVideo = galleryPhotoModel.isVideo();

    }


    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        Timber.tag("tag").w("onTextChanged " + s);


    }


    public void afterTextChanged(Editable s){

        galleryPhotoModel.setImageText(s.toString());

//        Timber.tag("tag").w("afterTextChanges " + s);


    }



    public void onSendButtonClick(){


        context.onClick(galleryPhotoModelList);




    }

    @BindingAdapter("bindImageText")
    public static void bindImageText(ImageView imageView , String imageUrl){


        if (!imageUrl.isEmpty()){

            Glide.with(imageView.getContext()).load(imageUrl)
                    .into(imageView);
        }
        else{
            if(context!=null)
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
        }
    }
}
