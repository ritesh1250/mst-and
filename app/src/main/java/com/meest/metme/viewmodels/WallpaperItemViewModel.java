package com.meest.metme.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.metme.model.WallpaperItemModel;

public class WallpaperItemViewModel {

    public WallpaperItemModel wallpaperItemModel;
    public String image;
    public static Activity context;


    public WallpaperItemViewModel(WallpaperItemModel wallpaperItemModel,Activity context) {
        this.wallpaperItemModel = wallpaperItemModel;
        this.context =context;
        this.image = wallpaperItemModel.getImageWallpaper();
    }

    @BindingAdapter("bindWallpaper")
    public static void bindWallpaperImage(ImageView imageView , String imageUrl){


        if (!imageUrl.isEmpty()){

            Glide.with(imageView.getContext()).load(imageUrl)
                    .into(imageView);
        }
        else{
            if(context!=null)
            imageView.setImageDrawable(context.getDrawable(R.drawable.image_placeholder));
        }
    }
}
