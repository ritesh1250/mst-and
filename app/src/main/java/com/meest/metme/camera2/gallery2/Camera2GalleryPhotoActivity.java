package com.meest.metme.camera2.gallery2;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.meest.R;
import com.meest.databinding.ActivityGalleryPhotoCamera2Binding;
import com.meest.metme.ImageTextActivity;
import com.meest.metme.camera2.gallery2.adapter.GalleryPhotoAdapterCamera2;
import com.meest.metme.model.gallery.GalleryPhotoModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Camera2GalleryPhotoActivity extends AppCompatActivity {

    ActivityGalleryPhotoCamera2Binding activityGalleryPhotoBinding;
    ArrayList<GalleryPhotoModel> galleryPhotoModelList= new ArrayList<>();
    ArrayList<GalleryPhotoModel> selectedGalleryPhotoModelList = new ArrayList<>();

    GalleryPhotoAdapterCamera2 galleryPhotoAdapter;

    public String folderPath;
    int LAUNCH_SECOND_ACTIVITY = 1231;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGalleryPhotoBinding = DataBindingUtil.setContentView(this, R.layout.activity_gallery_photo_camera2);

        folderPath = getIntent().getStringExtra("folderPath");


        galleryPhotoModelList.addAll(getAllImagesByFolder(folderPath));
        galleryPhotoModelList.addAll(getAllVideoByFolder(folderPath));

        activityGalleryPhotoBinding.recycler.setLayoutManager(new GridLayoutManager(this, 3));
        galleryPhotoAdapter = new GalleryPhotoAdapterCamera2(Camera2GalleryPhotoActivity.this, galleryPhotoModelList);
        activityGalleryPhotoBinding.setGalleryPhotoAdapter(galleryPhotoAdapter);

        activityGalleryPhotoBinding.okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent(getApplicationContext(), ImageTextActivity.class);
                returnIntent.putParcelableArrayListExtra("result", selectedGalleryPhotoModelList);
                startActivityForResult(returnIntent, LAUNCH_SECOND_ACTIVITY);

            }

        });

        activityGalleryPhotoBinding.backButton.setOnClickListener(v -> finish());

    }


    public void onClick(GalleryPhotoModel galleryPhotoModel, boolean ischecked) {

        if (ischecked)
            selectedGalleryPhotoModelList.add(galleryPhotoModel);
        else
            selectedGalleryPhotoModelList.remove(galleryPhotoModel);

        Log.e("selectedList", selectedGalleryPhotoModelList.toString());

        if (selectedGalleryPhotoModelList.size() > 0)
            activityGalleryPhotoBinding.okbutton.setVisibility(View.VISIBLE);
        else
            activityGalleryPhotoBinding.okbutton.setVisibility(View.GONE);

    }

    public ArrayList<GalleryPhotoModel> getAllVideoByFolder(String path){
        ArrayList<GalleryPhotoModel> images = new ArrayList<>();
        Uri allVideosuri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection_video = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE};
        Cursor cursor_video = this.getContentResolver().query(allVideosuri, projection_video, MediaStore.Video.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
        try {
            cursor_video.moveToFirst();
            do {
                GalleryPhotoModel pic = new GalleryPhotoModel();

                pic.setPicturName(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));

                pic.setPictureSize(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));

                File fileObject = new File(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                Long fileModified = fileObject.lastModified();
                pic.setDateTime(new Date(fileModified));
                Log.d("TAG", "getAllImagesByFolder: " + fileModified);
                pic.setVideo(true);
                images.add(pic);
            } while (cursor_video.moveToNext());
            cursor_video.close();
            ArrayList<GalleryPhotoModel> reSelection = new ArrayList<>();
            for (int i = images.size() - 1; i > -1; i--) {
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return images;
    }


    public ArrayList<GalleryPhotoModel> getAllImagesByFolder(String path) {
        ArrayList<GalleryPhotoModel> images = new ArrayList<>();
        Uri allimagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = this.getContentResolver().query(allimagesuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
        try {
            cursor.moveToFirst();
            do {
                GalleryPhotoModel pic = new GalleryPhotoModel();

                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
//                Log.e("Image path",cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
                File fileObject = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                Long fileModified = fileObject.lastModified();
                pic.setDateTime(new Date(fileModified));
                pic.setVideo(false);
                images.add(pic);
            } while (cursor.moveToNext());
            cursor.close();
            ArrayList<GalleryPhotoModel> reSelection = new ArrayList<>();
            for (int i = images.size() - 1; i > -1; i--) {
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return images;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<GalleryPhotoModel> result = data.getParcelableArrayListExtra(
                        "result");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", result);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

    }

}