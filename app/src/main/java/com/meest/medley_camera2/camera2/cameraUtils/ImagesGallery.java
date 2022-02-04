package com.meest.medley_camera2.camera2.cameraUtils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.meest.medley_camera2.camera2.model.GalleryModel;

import java.util.ArrayList;


public class ImagesGallery {

    private static final String TAG = "ImagesGallery";

    public static ArrayList<String> listOfImages(Context context) {



//        Uri uri;
//        Cursor cursor;
        int column_index_data, column_index_folder_name, thum;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        String absolutePath;
//        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
//        String[] projection = {MediaStore.MediaColumns.DATA,
//                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
//
//        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
//        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
//        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//
//        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//
//        while (cursor.moveToNext()){
//            absolutePath = cursor.getString(column_index_data);
//
//            listOfAllImages.add(absolutePath);
//        }

        // Get relevant columns for use later.
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE
        };

// Return only video and image metadata.
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Uri queryUri = MediaStore.Files.getContentUri("external");


        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        try {
            Cursor cursor = context.getContentResolver().query(queryUri, projection, selection, null, orderBy + " DESC");
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

//        CursorLoader cursorLoader = new CursorLoader(
//                this,
//                queryUri,
//                projection,
//                selection,
//                null, // Selection args (none).
//                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
//        );

//        Cursor cursor = cursorLoader.loadInBackground();

            while (cursor.moveToNext()) {
                absolutePath = cursor.getString(column_index_data);

                GalleryModel galleryModel = new GalleryModel();
                galleryModel.setBoolean_selected(false);
                galleryModel.setStr_path(absolutePath);

                galleryModel.setStr_thumb(cursor.getString(thum));
//
                listOfAllImages.add(absolutePath);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        return listOfAllImages;
    }
}
