package com.meest.metme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.meest.databinding.ActivityMetMeGalleryBinding;
import com.meest.mediapikcer.GalleryFoldersActivity;
import com.meest.mediapikcer.utils.imageFolder;
import com.meest.mediapikcer.utils.pictureFolderAdapter;
import com.meest.metme.adapter.PictureFolderAdapter;
import com.meest.metme.model.gallery.GalleryFolderModel;
import com.meest.metme.model.gallery.GalleryPhotoModel;
import com.meest.metme.viewmodels.TextAppearanceViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MetMeGallery extends AppCompatActivity {

    ActivityMetMeGalleryBinding metMeGalleryBinding;
    PictureFolderAdapter pictureFolderAdapter;

    ArrayList<GalleryFolderModel> galleryFolderModelList = new ArrayList<>();

    int LAUNCH_SECOND_ACTIVITY = 1231;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private String secondColor;
    private String chatHeadId;
    private String mediaType, whereFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        metMeGalleryBinding = DataBindingUtil.setContentView(this, R.layout.activity_met_me_gallery);
        metMeGalleryBinding.setLifecycleOwner(this);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        if (getIntent() != null) {
            whereFrom = getIntent().getStringExtra("whereFrom");
            mediaType = getIntent().getStringExtra("Mediatype");
        } else {
            whereFrom = "";
            mediaType = "";
        }

        if (whereFrom.equalsIgnoreCase("camera") && mediaType != null && !mediaType.equalsIgnoreCase("")) {
            if (mediaType.equalsIgnoreCase("Photo")) {
                metMeGalleryBinding.titleHead.setText("Images");
                galleryFolderModelList = (getPicturePaths());
            } else {
                metMeGalleryBinding.titleHead.setText(getString(R.string.video));
                galleryFolderModelList = (getVideoPaths());
            }
        } else {
            metMeGalleryBinding.titleHead.setText(getString(R.string.gallery));
            galleryFolderModelList.addAll(getVideoPaths());
            galleryFolderModelList.addAll(getPicturePaths());
        }

        if (getIntent() != null) {
            secondColor = getIntent().getStringExtra("secondColor");
            chatHeadId = getIntent().getStringExtra("chatHeadId");

        } else {
            chatHeadId = "";
            secondColor = "#143988";//Assign default string
        }

        metMeGalleryBinding.folderRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        pictureFolderAdapter = new PictureFolderAdapter(MetMeGallery.this, galleryFolderModelList, chatHeadId, secondColor, mediaType, whereFrom);
        metMeGalleryBinding.setGalleryFolderAdapter(pictureFolderAdapter);
        metMeGalleryBinding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        changeHeaderColor(secondColor);
    }

    private void changeHeaderColor(String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(0f);
            metMeGalleryBinding.mainContainer.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getColor(R.color.first_color), getColor(R.color.first_color)});
            gd.setCornerRadius(0f);
            metMeGalleryBinding.mainContainer.setBackground(gd);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<GalleryPhotoModel> result = data.getParcelableArrayListExtra("result");
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

    private ArrayList<GalleryFolderModel> getPicturePaths() {
        ArrayList<GalleryFolderModel> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
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

                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                folderpaths = folderpaths + folder + "/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in item view
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

        for (int i = 0; i < picFolders.size(); i++) {
            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPic());
        }
        return picFolders;
    }

    private ArrayList<GalleryFolderModel> getVideoPaths() {
        ArrayList<GalleryFolderModel> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
//        if (mediaType != null && mediaType.equalsIgnoreCase("Video")) {
//            Uri allVideouri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//            String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
//                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.BUCKET_ID};
//            Cursor cursor = this.getContentResolver().query(allVideouri, projection, null, null, null);
//            try {
//                if (cursor != null) {
//                    cursor.moveToFirst();
//                }do {
//                    GalleryFolderModel folds = new GalleryFolderModel();
//                    String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
//                    String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
//                    String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
//                    //String folderpaths =  datapath.replace(name,"");
//                    if (datapath.lastIndexOf(folder + "/") != -1) {
//                        String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
//                        folderpaths = folderpaths + folder + "/";
//                        if (!picPaths.contains(folderpaths)) {
//                            picPaths.add(folderpaths);
//                            folds.setPath(folderpaths);
//                            folds.setFolderName(folder);
//                            folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
//                            folds.addpics();
//                            picFolders.add(folds);
//                        } else {
//                            for (int i = 0; i < picFolders.size(); i++) {
//                                if (picFolders.get(i).getPath().equals(folderpaths)) {
//                                    picFolders.get(i).setFirstPic(datapath);
//                                    picFolders.get(i).addpics();
//                                }
//                            }
//                        }
//                    }
//
//                } while (cursor.moveToNext());
//                cursor.close();
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
////        }
//        for (int i = 0; i < picFolders.size(); i++) {
//            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPic());
//        }


//
//        // metMeGalleryBinding.titleHead.setText(getString(R.string.video));
        Uri allVideouri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Log.d("TAG", "getPicturePaths: " + allVideouri.getPath());


        Log.d("TAG", "getPicturePaths: LAD" + new Gson().toJson(new File(allVideouri.getPath())));


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

                //String folderpaths =  datapath.replace(name,"");
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


        for (int i = 0; i < picFolders.size(); i++) {
            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPic());
        }


        return picFolders;
    }

}