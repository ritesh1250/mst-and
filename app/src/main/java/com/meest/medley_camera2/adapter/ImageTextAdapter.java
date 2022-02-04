package com.meest.medley_camera2.adapter;

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
import com.meest.databinding.ItemImageTextMedlyBinding;
import com.meest.medley_camera2.ImageTextActivity;
import com.meest.medley_camera2.model.gallery.GalleryPhotoModel;
import com.meest.medley_camera2.viewmodels.ImageTextViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ImageTextAdapter extends RecyclerView.Adapter<ImageTextAdapter.ViewHolder> {

    public ArrayList<GalleryPhotoModel> galleryPhotoModelList;
    public ImageTextActivity context;
    ItemImageTextMedlyBinding binding;


    public ImageTextAdapter(ImageTextActivity context, ArrayList<GalleryPhotoModel> galleryPhotoModelList) {

        this.context =context;
        this.galleryPhotoModelList = galleryPhotoModelList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_image_text_medly, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());
        viewHolder.setBinding(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.setImageTextViewModel(galleryPhotoModelList.get(position));

        if(galleryPhotoModelList.get(position).isVideo()) {
            Uri uri = Uri.parse(galleryPhotoModelList.get(position).getPicturePath());
            binding.svid.setVideoURI(uri);

            binding.svid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            binding.svid.start();

            binding.svid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (binding.svid.isPlaying()) {
                        binding.svid.pause();
                    } else {
                        binding.svid.start();
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

        ItemImageTextMedlyBinding imageTextBinding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        public void setBinding(ItemImageTextMedlyBinding itemImageTextBinding){

            this.imageTextBinding = itemImageTextBinding;

        }

        public void setImageTextViewModel(GalleryPhotoModel galleryPhotoModel){

            imageTextBinding.setImageTextViewModel(new ImageTextViewModel(galleryPhotoModel,galleryPhotoModelList,context));
        }
    }
}
