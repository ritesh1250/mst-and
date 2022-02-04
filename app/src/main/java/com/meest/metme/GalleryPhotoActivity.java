package com.meest.metme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.meest.R;
import com.meest.databinding.ActivityGalleryPhotoBinding;
import com.meest.mediapikcer.GalleryItemDisplayActivity;
import com.meest.mediapikcer.utils.PictureFacer;
import com.meest.metme.adapter.GalleryPhotoAdapter;
import com.meest.metme.adapter.PictureFolderAdapter;
import com.meest.metme.model.gallery.GalleryFolderModel;
import com.meest.metme.model.gallery.GalleryPhotoModel;
import com.meest.videomvvmmodule.gallery.GalleryItemDisplayActivityMedley;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GalleryPhotoActivity extends AppCompatActivity {

    public String folderPath;
    ActivityGalleryPhotoBinding activityGalleryPhotoBinding;
    ArrayList<GalleryPhotoModel> galleryPhotoModelList = new ArrayList<>();
    ArrayList<GalleryPhotoModel> selectedGalleryPhotoModelList = new ArrayList<>();
    GalleryPhotoAdapter galleryPhotoAdapter;
    int LAUNCH_SECOND_ACTIVITY = 1231;
    private String secondColor;
    private String chatHeadId;
    private String mediatype, whereFrom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGalleryPhotoBinding = DataBindingUtil.setContentView(this, R.layout.activity_gallery_photo);

        folderPath = getIntent().getStringExtra("folderPath");
        if (getIntent() != null) {
            secondColor = getIntent().getStringExtra("secondColor");
            chatHeadId = getIntent().getStringExtra("chatHeadId");
            whereFrom = getIntent().getStringExtra("whereFrom");
            mediatype = getIntent().getStringExtra("mediatype");
        } else {
            chatHeadId = "";
            secondColor = "#143988";//Assign default string
        }
//
        if (whereFrom.equalsIgnoreCase("camera") && mediatype!=null && !mediatype.equalsIgnoreCase("")){
            if (mediatype.equalsIgnoreCase("Photo")) {
                galleryPhotoModelList.addAll(getAllImagesByFolder(folderPath));
            }else {
                galleryPhotoModelList.addAll(getAllVideoByFolder(folderPath));
            }
        }else {
            galleryPhotoModelList.addAll(getAllImagesByFolder(folderPath));
            galleryPhotoModelList.addAll(getAllVideoByFolder(folderPath));
        }


        activityGalleryPhotoBinding.recycler.setLayoutManager(new GridLayoutManager(this, 3));
        galleryPhotoAdapter = new GalleryPhotoAdapter(GalleryPhotoActivity.this, galleryPhotoModelList, chatHeadId);
        activityGalleryPhotoBinding.setGalleryPhotoAdapter(galleryPhotoAdapter);

        activityGalleryPhotoBinding.okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent(getApplicationContext(), ImageTextActivity.class);
                returnIntent.putParcelableArrayListExtra("result", selectedGalleryPhotoModelList);
                startActivityForResult(returnIntent, LAUNCH_SECOND_ACTIVITY);

            }
        });
        activityGalleryPhotoBinding.backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        changeHeaderColor(secondColor);
        changeButton(secondColor);
    }

    private void changeHeaderColor(String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(0f);
            activityGalleryPhotoBinding.mainContainer.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getColor(R.color.first_color), getColor(R.color.first_color)});
            gd.setCornerRadius(0f);
            activityGalleryPhotoBinding.mainContainer.setBackground(gd);
        }
    }

    private void changeButton(String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(8f);
            activityGalleryPhotoBinding.okbutton.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getColor(R.color.first_color), getColor(R.color.first_color)});
            gd.setCornerRadius(8f);
            activityGalleryPhotoBinding.okbutton.setBackground(gd);
        }
    }

    public void onClick(GalleryPhotoModel galleryPhotoModel, boolean ischecked) {

        if (ischecked) {
            galleryPhotoModel.setChecked(false);
            selectedGalleryPhotoModelList.add(galleryPhotoModel);
        } else {
            galleryPhotoModel.setChecked(true);
            selectedGalleryPhotoModelList.remove(galleryPhotoModel);
        }

        Log.e("selectedList", selectedGalleryPhotoModelList.toString());

        if (selectedGalleryPhotoModelList.size() > 0)
            activityGalleryPhotoBinding.okbutton.setVisibility(View.VISIBLE);
        else
            activityGalleryPhotoBinding.okbutton.setVisibility(View.GONE);

    }

    public void onImageClick(GalleryPhotoModel galleryPhotoModel, boolean ischecked) {

        if (ischecked) {
            galleryPhotoModel.setChecked(false);
            selectedGalleryPhotoModelList.remove(galleryPhotoModel);
        } else {
            galleryPhotoModel.setChecked(true);
            selectedGalleryPhotoModelList.add(galleryPhotoModel);

        }

        Log.e("selectedList", selectedGalleryPhotoModelList.toString());

        if (selectedGalleryPhotoModelList.size() > 0)
            activityGalleryPhotoBinding.okbutton.setVisibility(View.VISIBLE);
        else
            activityGalleryPhotoBinding.okbutton.setVisibility(View.GONE);

        galleryPhotoAdapter.notifyDataSetChanged();

    }

    public ArrayList<GalleryPhotoModel> getAllVideoByFolder(String path) {
        ArrayList<GalleryPhotoModel> images = new ArrayList<>();
        Uri allVideosuri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection_video = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE};
        Cursor cursor_video = this.getContentResolver().query(allVideosuri, projection_video, MediaStore.Video.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
//        try {
//            cursor_video.moveToFirst();
//            do {
//                GalleryPhotoModel pic = new GalleryPhotoModel();
//
//                pic.setPicturName(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
//
//                pic.setPicturePath(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
//
//                pic.setPictureSize(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
//
//                File fileObject = new File(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
//                Long fileModified = fileObject.lastModified();
//                pic.setDateTime(new Date(fileModified));
////                Log.d("TAG", "getAllImagesByFolder: " + fileModified);
//                pic.setType("Video");
//                pic.setVideo(true);
//                int i = pic.getPicturName().lastIndexOf('.');
//                String fileExtention = "";
//                if (i > 0) {
//                    fileExtention = pic.getPicturName().substring(i + 1);
//                }
//                if (fileExtention.equalsIgnoreCase("mp4") || fileExtention.equalsIgnoreCase("3gp") || fileExtention.equalsIgnoreCase("mkv") || fileExtention.equalsIgnoreCase("mov") || fileExtention.equalsIgnoreCase("webm"))
//                    images.add(pic);
//            } while (cursor_video.moveToNext());
//            cursor_video.close();
//            ArrayList<GalleryPhotoModel> reSelection = new ArrayList<>();
//            for (int i = images.size() - 1; i > -1; i--) {
//                reSelection.add(images.get(i));
//            }
//            images = reSelection;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

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
        Cursor cursor = GalleryPhotoActivity.this.getContentResolver().query(allimagesuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
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
                pic.setType("Image");
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
                returnIntent.putExtra("whereFrom", getIntent().getStringExtra("whereFrom"));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

    }

}