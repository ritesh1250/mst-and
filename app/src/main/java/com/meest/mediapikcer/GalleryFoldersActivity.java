package com.meest.mediapikcer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;

import com.meest.mediapikcer.utils.imageFolder;
import com.meest.mediapikcer.utils.pictureFolderAdapter;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

public class GalleryFoldersActivity extends AppCompatActivity {
    int LAUNCH_SECOND_ACTIVITY = 1231;

    private static final String TAG = "MainActivity";
    RecyclerView folderRecycler;
    TextView empty,titleHead;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private String filter;
    private String call_type;
    private ArrayList<imageFolder> folds;

    /**
     * Request the user for permission to access media files and read Video on the device
     * this will be useful as from api 21 and above, if this check is not done the Activity will crash
     * <p>
     * Setting up the RecyclerView and getting all folders that contain pictures from the device
     * the getPicturePaths() returns an ArrayList of imageFolder objects that is then used to
     * create a RecyclerView Adapter that is set to the RecyclerView
     *
     * @param savedInstanceState saving the activity state
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galleryfolders);

        if (getSupportActionBar()!=null)
        getSupportActionBar().hide();

        if (getIntent()!=null && getIntent().hasExtra("filter"))
        {
            filter=getIntent().getStringExtra("filter");
        }
        if (getIntent()!=null && getIntent().hasExtra("call_type"))
        {
            call_type=getIntent().getStringExtra("call_type");
        }

        if (ContextCompat.checkSelfPermission(GalleryFoldersActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(GalleryFoldersActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        //____________________________________________________________________________________
        empty = findViewById(R.id.empty);
        titleHead = findViewById(R.id.titleHead);
        folderRecycler = findViewById(R.id.folderRecycler);
        folderRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        if (filter!=null && filter.equalsIgnoreCase("Image")){
        folds = getPicturePaths();
        }else{
         folds = getVideoPaths();
        }

        if (folds.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
        } else {
            RecyclerView.Adapter folderAdapter = new pictureFolderAdapter(folds, GalleryFoldersActivity.this,filter,call_type);
            folderRecycler.setAdapter(folderAdapter);
        }

//        changeStatusBarColor();
        findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 1
     *
     * @return gets all folders with pictures on the device and loads each of them in a custom object imageFolder
     * the returns an ArrayList of these custom objects
     */
    private ArrayList<imageFolder> getPicturePaths() {
        ArrayList<imageFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();

        titleHead.setText(getString(R.string.Images));
        Uri allImagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection_image = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor_image = this.getContentResolver().query(allImagesuri, projection_image, null, null, null);
        try {
            if (cursor_image != null) {
                cursor_image.moveToFirst();
            }
            do {
                imageFolder folds = new imageFolder();
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

/*        if (filter.equalsIgnoreCase("Video"))
        {
            titleHead.setText("Video");
            Uri allVideouri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            Log.d(TAG, "getPicturePaths: " + allVideouri.getPath());


            Log.d(TAG, "getPicturePaths: LAD" + new Gson().toJson(new File(allVideouri.getPath())));


            String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.BUCKET_ID};
            Cursor cursor = this.getContentResolver().query(allVideouri, projection, null, null, null);
            try {
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                do {
                    imageFolder folds = new imageFolder();
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

        }
        else
        {
            titleHead.setText("Image");
            Uri allImagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection_image = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
            Cursor cursor_image = this.getContentResolver().query(allImagesuri, projection_image, null, null, null);
            try {
                if (cursor_image != null) {
                    cursor_image.moveToFirst();
                }
                do {
                    imageFolder folds = new imageFolder();
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
                        folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
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

        }*/


        for (int i = 0; i < picFolders.size(); i++) {
            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPics());
        }



        return picFolders;
    }



    private ArrayList<imageFolder> getVideoPaths() {
        ArrayList<imageFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();



        if (filter!=null && filter.equalsIgnoreCase("Video"))
        {
            titleHead.setText(getString(R.string.video));
            Uri allVideouri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            Log.d(TAG, "getPicturePaths: " + allVideouri.getPath());


            Log.d(TAG, "getPicturePaths: LAD" + new Gson().toJson(new File(allVideouri.getPath())));


            String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.BUCKET_ID};
            Cursor cursor = this.getContentResolver().query(allVideouri, projection, null, null, null);
            try {
                if (cursor != null) {
                    cursor.moveToFirst();
                }
                do {
                    imageFolder folds = new imageFolder();
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                    String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                    //String folderpaths =  datapath.replace(name,"");

                    if (datapath.lastIndexOf(folder + "/") != -1) {
                        String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                        folderpaths = folderpaths + folder + "/";
                        if (!picPaths.contains(folderpaths)) {
                            picPaths.add(folderpaths);

                            folds.setPath(folderpaths);
                            folds.setFolderName(folder);
                            folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
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
                    }
                } while (cursor.moveToNext());
                cursor.close();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        for (int i = 0; i < picFolders.size(); i++) {
            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPics());
        }



        return picFolders;
    }


    /**
     * Default status bar height 24dp,with code API level 24
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeStatusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                String call_type_result = data.getStringExtra("call_type");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", result);
                returnIntent.putExtra("call_type",   call_type_result);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

}
