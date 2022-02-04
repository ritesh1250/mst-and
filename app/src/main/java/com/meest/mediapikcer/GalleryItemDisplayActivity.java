package com.meest.mediapikcer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.mediapikcer.utils.MarginDecoration;
import com.meest.mediapikcer.utils.PictureFacer;
import com.meest.mediapikcer.utils.picture_Adapter;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class GalleryItemDisplayActivity extends AppCompatActivity {

    RecyclerView imageRecycler;
    ArrayList<PictureFacer> allpictures;
    ArrayList<PictureFacer> selectedItem = new ArrayList<>();
    ProgressBar load;
    String foldePath,call_type,filter;
    Button okbutton;
    TextView folderName;
    int LAUNCH_SECOND_ACTIVITY = 1231;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_item_display);

        okbutton = findViewById(R.id.okbutton);
        folderName = findViewById(R.id.foldername);


        folderName.setText(getIntent().getStringExtra("folderName"));

        foldePath = getIntent().getStringExtra("folderPath");

        call_type = getIntent().getStringExtra("call_type");
        filter = getIntent().getStringExtra("filter");


        allpictures = new ArrayList<>();
        imageRecycler = findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(this));
        imageRecycler.hasFixedSize();
        load = findViewById(R.id.loader);



        if (allpictures.isEmpty()) {
            load.setVisibility(View.VISIBLE);

            if (filter.equalsIgnoreCase("Image")){
                allpictures = getAllImagesByFolder(foldePath);
            }else {
                allpictures = getAllVideoByFolder(foldePath);
            }


            Collections.sort(allpictures);
            Collections.reverse(allpictures);

            imageRecycler.setAdapter(new picture_Adapter(allpictures, GalleryItemDisplayActivity.this,filter,call_type));
            load.setVisibility(View.GONE);
        } else {

        }
        findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.okbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", new Gson().toJson(selectedItem));
                returnIntent.putExtra("call_type", call_type);
                returnIntent.putExtra("filter", filter);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        findViewById(R.id.filterimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //creating a popup menu
//                PopupMenu popup = new PopupMenu(GalleryItemDisplayActivity.this, view);
//                //inflating menu from xml resource
//                popup.inflate(R.menu.gallery_filter_menu);
//
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        switch (menuItem.getItemId()) {
//                            case R.id.all: {
//                                filterdata("all");
//
//                                break;
//                            }
//                            case R.id.videos: {
//                                filterdata("Video");
//
//                                break;
//                            }
//                            case R.id.images: {
//                                filterdata("Image");
//                                break;
//                            }
//
//
//                        }
//                        return false;
//                    }
//                });
//
//                popup.show();
            }
        });


        filterdata(filter);
    }


    public ArrayList<PictureFacer> getAllImagesByFolder(String path) {
        ArrayList<PictureFacer> images = new ArrayList<>();
        Uri allimagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = GalleryItemDisplayActivity.this.getContentResolver().query(allimagesuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
        try {
            cursor.moveToFirst();
            do {
                PictureFacer pic = new PictureFacer();

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
            ArrayList<PictureFacer> reSelection = new ArrayList<>();
            for (int i = images.size() - 1; i > -1; i--) {
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }



        return images;
    }


    public ArrayList<PictureFacer> getAllVideoByFolder(String path){
        ArrayList<PictureFacer> images = new ArrayList<>();
        Uri allVideosuri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection_video = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE};
        Cursor cursor_video = GalleryItemDisplayActivity.this.getContentResolver().query(allVideosuri, projection_video, MediaStore.Video.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
        try {
            cursor_video.moveToFirst();
            do {
                PictureFacer pic = new PictureFacer();

                pic.setPicturName(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));

                pic.setPictureSize(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));

                File fileObject = new File(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                Long fileModified = fileObject.lastModified();
                pic.setDateTime(new Date(fileModified));
                Log.d("TAG", "getAllImagesByFolder: " + fileModified);
                pic.setType("Video");
                images.add(pic);
            } while (cursor_video.moveToNext());
            cursor_video.close();
            ArrayList<PictureFacer> reSelection = new ArrayList<>();
            for (int i = images.size() - 1; i > -1; i--) {
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return images;
    }


    public void onClick(PictureFacer file, boolean isselect) {

        if (isselect) {
            selectedItem.add(file);
        } else {
            selectedItem.remove(file);
        }
        if (selectedItem.size() > 0) {
            okbutton.setVisibility(View.VISIBLE);
        } else {
            okbutton.setVisibility(View.GONE);
        }

    }


    void filterdata(String itemtype) {
        selectedItem.clear();
        okbutton.setVisibility(View.GONE);
        ArrayList<PictureFacer> fileModellist_filter = new ArrayList<>();
        for (PictureFacer item : allpictures) {


            if (itemtype.equals("Image")) {
                if (item.getType().equals("Image"))
                    fileModellist_filter.add(item);
            } else if (itemtype.equals("Video")) {
                if (item.getType().equals("Video"))
                    fileModellist_filter.add(item);
            }
            else {
                fileModellist_filter.add(item);
            }



        }
        imageRecycler.setAdapter(new picture_Adapter(allpictures, GalleryItemDisplayActivity.this,filter,call_type));
    }


}
