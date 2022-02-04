package com.meest.metme.camera2.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.apache.commons.io.FilenameUtils;

import java.util.List;

import com.meest.R;
import com.meest.metme.camera2.CameraActivity;
import com.meest.metme.camera2.gallery.adpater.GalleryAdapter;
import com.meest.metme.camera2.preview.PreviewVideoActivity;


public class GalleryBottomSheet extends BottomSheetDialogFragment {
    private static final String TAG = "galleryBottomSheet";

    RecyclerView recyclerView;
    GalleryAdapter galleryAdapter;
    List<String> images;
    TextView gallery_number;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gallery_bottom_sheet,
                container, false);

        gallery_number = view.findViewById(R.id.gallery_number);
        recyclerView = view.findViewById(R.id.recyclerview_gallery_images);

//        if(ContextCompat.checkSelfPermission(galleryBottomSheet.this))
        loadImages();
//        view.findViewById(R.id.closeBottomSheet).setOnClickListener(v1 -> CameraActivity.bottomSheet.dismiss());

        return view;
    }

    private void loadImages() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        images = ImagesGallery.listOfImages(getContext());
        galleryAdapter = new GalleryAdapter(getContext(), images, path -> {
            Log.e(TAG, "loadImages: "+path);
            String ext = FilenameUtils.getExtension(path);
            Log.e(TAG, "loadImages: "+ext);
            //Toast.makeText(getContext(), ext, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
            if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpeg")) {
                intent.putExtra("image", path);
                Toast.makeText(getContext(), "Image Loading", Toast.LENGTH_SHORT).show();
            }
            else {
                intent.putExtra("videoPath", path);
                Toast.makeText(getContext(), "Video Loading", Toast.LENGTH_SHORT).show();
            }
            startActivity(intent);
//                Intent intent = new Intent(getContext(), PreviewActivity.class);
//                intent.putExtra("videoPath", path);
//                startActivity(intent);

            //Do something with photo

        });
        recyclerView.setAdapter(galleryAdapter);
        gallery_number.setText("Select Media");

    }
}