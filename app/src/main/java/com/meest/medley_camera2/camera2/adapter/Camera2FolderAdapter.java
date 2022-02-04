package com.meest.medley_camera2.camera2.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.GalleryFolderItemCamera2Binding;
import com.meest.databinding.GalleryFolderItemCamera2MedlyBinding;
import com.meest.medley_camera2.camera2.viewmodels.Camera2GalleryFolderViewModel;
import com.meest.medley_camera2.model.gallery.GalleryFolderModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Camera2FolderAdapter extends RecyclerView.Adapter<Camera2FolderAdapter.ViewHolder> {

    public Activity context;
    public ArrayList<GalleryFolderModel> galleryFolderModelList;

    public GalleryFolderItemCamera2MedlyBinding galleryFolderItemMetmeBinding;

    public Camera2FolderAdapter(Activity context, ArrayList<GalleryFolderModel> galleryFolderModelList) {

        this.context = context;
        this.galleryFolderModelList = galleryFolderModelList;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        galleryFolderItemMetmeBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.gallery_folder_item_camera2_medly, parent, false);
        ViewHolder viewHolder = new ViewHolder(galleryFolderItemMetmeBinding.getRoot());
        viewHolder.setGalleryFolderItemCamera2Binding(galleryFolderItemMetmeBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.setGalleryFolderViewModel(galleryFolderModelList.get(position),context);

    }

    @Override
    public int getItemCount() {
        return galleryFolderModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        GalleryFolderItemCamera2MedlyBinding galleryFolderItemMetmeBinding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        public void setGalleryFolderItemCamera2Binding(GalleryFolderItemCamera2MedlyBinding galleryFolderItemMetmeBinding){

            this.galleryFolderItemMetmeBinding = galleryFolderItemMetmeBinding;
        }

        public void setGalleryFolderViewModel(GalleryFolderModel galleryFolderModel,Activity context){

            galleryFolderItemMetmeBinding.setGalleryFolderViewModel(new Camera2GalleryFolderViewModel(galleryFolderModel,context));
        }
    }
}
