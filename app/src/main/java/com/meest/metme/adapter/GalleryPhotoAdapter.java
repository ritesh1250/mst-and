package com.meest.metme.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemGalleryPhotoBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.GalleryPhotoActivity;
import com.meest.metme.model.gallery.GalleryPhotoModel;
import com.meest.metme.viewmodels.GalleryPhotoViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GalleryPhotoAdapter extends RecyclerView.Adapter<GalleryPhotoAdapter.ViewHolder> {

    ItemGalleryPhotoBinding itemGalleryPhotoBinding;
    List<GalleryPhotoModel> galleryPhotoModelList;
    public GalleryPhotoActivity context;
    public String fontName;
    public String chatHeadId;

    public GalleryPhotoAdapter(GalleryPhotoActivity context, List<GalleryPhotoModel> galleryPhotoModelList, String chatHeadId) {
        this.context = context;
        this.galleryPhotoModelList = galleryPhotoModelList;
        this.chatHeadId = chatHeadId;
        fontName= SharedPrefreances.getSharedPreferenceString(context,chatHeadId);
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        itemGalleryPhotoBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_gallery_photo, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemGalleryPhotoBinding.getRoot());
        viewHolder.setItemGalleryPhotoBinding(itemGalleryPhotoBinding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GalleryPhotoAdapter.ViewHolder holder, int position) {

        holder.setPhotoItemViewModel(galleryPhotoModelList.get(position));

       /* holder.itemGalleryPhotoBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                galleryPhotoModelList.get(position).setChecked(isChecked);
             //   context.onClick(galleryPhotoModelList.get(position),isChecked);
            }
        });*/

        if (galleryPhotoModelList.get(position).isChecked()){
            holder.itemGalleryPhotoBinding.checkbox.setImageDrawable(context.getDrawable(R.drawable.ic_chat_request_select));
            holder.itemGalleryPhotoBinding.checkbox.setColorFilter(ContextCompat.getColor(context, R.color.yellow), PorterDuff.Mode.SRC_IN);
        }else {
            holder.itemGalleryPhotoBinding.checkbox.setImageDrawable(context.getDrawable(R.drawable.ic_chat_request_unselect));
            holder.itemGalleryPhotoBinding.checkbox.setColorFilter(ContextCompat.getColor(context, R.color.edit_line_gray_search), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemCount() {
        return galleryPhotoModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemGalleryPhotoBinding itemGalleryPhotoBinding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        public void setItemGalleryPhotoBinding(ItemGalleryPhotoBinding itemGalleryPhotoBinding){

            this.itemGalleryPhotoBinding = itemGalleryPhotoBinding;
        }

        public void setPhotoItemViewModel(GalleryPhotoModel galleryPhotoModel){

            itemGalleryPhotoBinding.setGalleryPhotoViewModel(new GalleryPhotoViewModel(galleryPhotoModel,context));
        }

        public GalleryPhotoViewModel getGalleryPhotoModel(){

          return   itemGalleryPhotoBinding.getGalleryPhotoViewModel();


        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
