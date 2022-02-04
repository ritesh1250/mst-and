package com.meest.medley_camera2.camera2.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.meest.R;
import com.meest.databinding.Camera2GalleryBinding;
import com.meest.databinding.Camera2GalleryMedleyBinding;
import com.meest.medley_camera2.camera2.adapter.Camera2FolderAdapter;
import com.meest.medley_camera2.model.gallery.GalleryFolderModel;

import java.io.File;
import java.util.ArrayList;

public class Camera2Gallery extends AppCompatActivity {

    Camera2GalleryMedleyBinding metMeGalleryBinding;
    Camera2FolderAdapter pictureFolderAdapter;

    ArrayList<GalleryFolderModel> galleryFolderModelList = new ArrayList<>();

    int LAUNCH_SECOND_ACTIVITY = 1231;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        metMeGalleryBinding = DataBindingUtil.setContentView(this, R.layout.camera2_gallery_medley);
        metMeGalleryBinding.setLifecycleOwner(this);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


        if (getIntent().getStringExtra("type") != null) {
            if (getIntent().getStringExtra("type").equalsIgnoreCase("Video"))
                galleryFolderModelList.addAll(getVideoPaths());
            else
                galleryFolderModelList.addAll(getPicturePaths());
        }


        metMeGalleryBinding.folderRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        pictureFolderAdapter = new Camera2FolderAdapter(this, galleryFolderModelList);
        metMeGalleryBinding.setGalleryFolderAdapter(pictureFolderAdapter);

        metMeGalleryBinding.backButton.setOnClickListener(v -> finish());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

                assert data != null;
                String result = data.getStringExtra("result");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", result);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }

    }

    private ArrayList<GalleryFolderModel> getPicturePaths() {
        ArrayList<GalleryFolderModel> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();

        metMeGalleryBinding.titleHead.setText(getString(R.string.gallery));
        Uri allImagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection_image = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor_image = this.getContentResolver().query(allImagesuri, projection_image, null, null, null);
        try {
            if (cursor_image != null) {
                cursor_image.moveToFirst();
            }
            do {
                GalleryFolderModel folds = new GalleryFolderModel();
                String name = cursor_image.getString(cursor_image.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor_image.getString(cursor_image.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor_image.getString(cursor_image.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));


                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                folderpaths = folderpaths + folder + "/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);
                    folds.setSetType("Image");//if the folder has only one picture this line helps to set it as first so as to avoid blank image in item view
                    folds.addpics();
                    picFolders.add(folds);
                } else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getPath().equals(folderpaths)) {
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            } while (cursor_image.moveToNext());
            cursor_image.close();


        } catch (Exception e) {
            e.printStackTrace();
        }



        return picFolders;
    }

    private ArrayList<GalleryFolderModel> getVideoPaths() {
        ArrayList<GalleryFolderModel> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();

        Uri allVideouri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.BUCKET_ID};
        Cursor cursor = this.getContentResolver().query(allVideouri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                GalleryFolderModel folds = new GalleryFolderModel();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                folderpaths = folderpaths + folder + "/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.setSetType("Video");
                    folds.addpics();
                    picFolders.add(folds);
                } else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getPath().equals(folderpaths)) {
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();


        } catch (Exception e) {
            e.printStackTrace();
        }



        return picFolders;
    }

}