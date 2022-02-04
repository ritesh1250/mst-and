package com.meest.metme.camera2.gallery.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.meest.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "GalleryAdapter";
    private final Context context;
    private final List<String> images;
    protected PhotoListener photoListener;

    public GalleryAdapter(Context context, List<String> images, PhotoListener photoListener) {
        this.context = context;
        this.images = images;
        this.photoListener = photoListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.medley_gallery_item, parent, false)

        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String image = images.get(position);
        Glide.with(context).asBitmap().load(image).into(holder.gallery_image);

        holder.itemView.setOnClickListener(view -> {
            photoListener.onPhotoClick(image);
//            Toast.makeText(context, "Item Clicked", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public void onClick(View view) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gallery_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gallery_image = itemView.findViewById(R.id.gallery_image);

        }
    }

    public interface PhotoListener{
        void onPhotoClick(String path);
    }
}
