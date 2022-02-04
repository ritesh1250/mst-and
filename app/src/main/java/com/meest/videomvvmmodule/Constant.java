package com.meest.videomvvmmodule;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Constant {
    public static String getPath(Context context, Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String picturePath = "";
        if (selectedImage != null) {
            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
            }
        }
        return picturePath;
    }
}
