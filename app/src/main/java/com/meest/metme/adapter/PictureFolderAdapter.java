package com.meest.metme.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.GalleryFolderItemMetmeBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.MetMeGallery;
import com.meest.metme.model.gallery.GalleryFolderModel;
import com.meest.metme.viewmodels.GalleryFolderViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PictureFolderAdapter extends RecyclerView.Adapter<PictureFolderAdapter.ViewHolder> {

    public Activity context;
    public ArrayList<GalleryFolderModel> galleryFolderModelList;
    public GalleryFolderItemMetmeBinding galleryFolderItemMetmeBinding;
    public static String chatHeadId;
    public static String secondColor;
    public static String mediaType, whereFrom;
    public String fontName;


    public PictureFolderAdapter(Activity context, ArrayList<GalleryFolderModel> galleryFolderModelList,String chatHeadId,String secondColor,  String mediaType, String whereFrom) {
        this.context = context;
        this.galleryFolderModelList = galleryFolderModelList;
        this.chatHeadId = chatHeadId;
        this.secondColor = secondColor;
        this.mediaType=mediaType;
        this.whereFrom=whereFrom;
        fontName=SharedPrefreances.getSharedPreferenceString(context,chatHeadId);
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        galleryFolderItemMetmeBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.gallery_folder_item_metme, parent, false);
        ViewHolder viewHolder = new ViewHolder(galleryFolderItemMetmeBinding.getRoot());
        viewHolder.setGalleryFolderItemMetmeBinding(galleryFolderItemMetmeBinding);

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

        GalleryFolderItemMetmeBinding galleryFolderItemMetmeBinding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        public void setGalleryFolderItemMetmeBinding(GalleryFolderItemMetmeBinding galleryFolderItemMetmeBinding){

            this.galleryFolderItemMetmeBinding = galleryFolderItemMetmeBinding;
        }

        public void setGalleryFolderViewModel(GalleryFolderModel galleryFolderModel,Activity context){

            galleryFolderItemMetmeBinding.setGalleryFolderViewModel(new GalleryFolderViewModel(galleryFolderModel,context,chatHeadId,secondColor, mediaType, whereFrom));
        }
    }
}
