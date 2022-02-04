package com.meest.metme.adapter;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemImageTextBinding;
import com.meest.metme.ImageTextActivity;
import com.meest.metme.model.gallery.GalleryPhotoModel;
import com.meest.metme.viewmodels.ImageTextViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ImageTextAdapter extends RecyclerView.Adapter<ImageTextAdapter.ViewHolder> {

    public ArrayList<GalleryPhotoModel> galleryPhotoModelList;
    public ImageTextActivity context;
    ItemImageTextBinding imageTextBinding;


    public ImageTextAdapter(ImageTextActivity context, ArrayList<GalleryPhotoModel> galleryPhotoModelList) {

        this.context =context;
        this.galleryPhotoModelList = galleryPhotoModelList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        imageTextBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_image_text, parent, false);
        ViewHolder viewHolder = new ViewHolder(imageTextBinding.getRoot());
        viewHolder.setBinding(imageTextBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageTextAdapter.ViewHolder holder, int position) {

        holder.setImageTextViewModel(galleryPhotoModelList.get(position));

        if(galleryPhotoModelList.get(position).isVideo()) {
            Uri uri = Uri.parse(galleryPhotoModelList.get(position).getPicturePath());
            imageTextBinding.svid.setVideoURI(uri);

            imageTextBinding.svid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            imageTextBinding.svid.start();

            imageTextBinding.svid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageTextBinding.svid.isPlaying()) {
                        imageTextBinding.svid.pause();
                    } else {
                        imageTextBinding.svid.start();
                    }

                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return galleryPhotoModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ItemImageTextBinding imageTextBinding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        public void setBinding(ItemImageTextBinding itemImageTextBinding){

            this.imageTextBinding = itemImageTextBinding;

        }

        public void setImageTextViewModel(GalleryPhotoModel galleryPhotoModel){

            imageTextBinding.setImageTextViewModel(new ImageTextViewModel(galleryPhotoModel,galleryPhotoModelList,context));
        }
    }
}
