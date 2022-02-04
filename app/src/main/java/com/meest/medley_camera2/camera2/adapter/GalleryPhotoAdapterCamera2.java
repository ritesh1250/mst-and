package com.meest.medley_camera2.camera2.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemGalleryPhotoCamera2Binding;
import com.meest.databinding.ItemGalleryPhotoCamera2MedlyBinding;
import com.meest.medley_camera2.camera2.viewmodels.GalleryPhotoViewModelCamera2;
import com.meest.medley_camera2.model.gallery.GalleryPhotoModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GalleryPhotoAdapterCamera2 extends RecyclerView.Adapter<GalleryPhotoAdapterCamera2.ViewHolder> {

    ItemGalleryPhotoCamera2MedlyBinding itemGalleryPhotoBinding;
    List<GalleryPhotoModel> galleryPhotoModelList;
    public Activity context;


    public GalleryPhotoAdapterCamera2(Activity context, List<GalleryPhotoModel> galleryPhotoModelList) {
        this.context = context;
        this.galleryPhotoModelList = galleryPhotoModelList;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        itemGalleryPhotoBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_gallery_photo_camera2_medly, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemGalleryPhotoBinding.getRoot());
        viewHolder.setItemGalleryPhotoCamera2Binding(itemGalleryPhotoBinding);

        return viewHolder;
    }

    private static final String TAG = "GalleryPhotoAdapterCame";
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.setPhotoItemViewModel(galleryPhotoModelList.get(position));
        //Log.e(TAG, "galleryPhotoModelList: "+galleryPhotoModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return galleryPhotoModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ItemGalleryPhotoCamera2MedlyBinding itemGalleryPhotoBinding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        public void setItemGalleryPhotoCamera2Binding(ItemGalleryPhotoCamera2MedlyBinding itemGalleryPhotoBinding){

            this.itemGalleryPhotoBinding = itemGalleryPhotoBinding;
        }

        public void setPhotoItemViewModel(GalleryPhotoModel galleryPhotoModel){

            itemGalleryPhotoBinding.setGalleryPhotoViewModel(new GalleryPhotoViewModelCamera2(galleryPhotoModel,context));


        }
    }
}
