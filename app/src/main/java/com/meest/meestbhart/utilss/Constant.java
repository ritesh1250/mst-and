package com.meest.meestbhart.utilss;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import androidx.multidex.BuildConfig;

import com.meest.Interfaces.CompressMedia;
import com.meest.R;
import com.meest.responses.FeelingResponse;
import com.meest.responses.UserDataResponse;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.meest.responses.UserSelectedTopics;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicsResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;


public class Constant {
    public static ArrayList<String> array_list_ = new ArrayList<>();
    public static ArrayList<String> array_list_2 = new ArrayList<>();
    public static ArrayList<String> selectedTopics = new ArrayList<>();
    public static ArrayList<String> allTopics = new ArrayList<>();
    public static ArrayList<String> selectedTopicIDs = new ArrayList<>();
    public static List<TopicsResponse.Row> mData = new ArrayList<>();
    public static List<UserSelectedTopics.Row> rowList = new ArrayList<>();
    public static FeelingResponse allFeelings, allActivities;
    public static String selectedFeelingID = "", selectedFeelingTitle = "", selectedActivityID = "", selectedActivityTitle = "";
    public static int row_index_feeling = -1, row_index_activity = -1;

    public static List<UserSelectedTopics> allTopicsResponse = new ArrayList<>();
    public static List<UserSelectedTopics> selectedTopicsResponse = new ArrayList<>();

    public static int LAST_SEL_TAB = 0;
    public static final String STORY_TYPE_CHAT = "chat";
    public static final String STORY_TYPE_SOCIAL = "feed";


    public static final String APP_DIRECTORY = "Meest";
    public static final String APP_VIDEO_DIRECTORY = "Video";
    public static final String APP_TEMP_DIRECTORY = "temp";
    public static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.meest";

    // for fcm
    public static String CALL_RESPONSE_ACTION_KEY = "CALL_RESPONSE_ACTION_KEY";
    public static String FCM_DATA_KEY = "FCM_DATA_KEY";
    public static String CALL_RECEIVE_ACTION = "CALL_RECEIVE_ACTION";
    public static String CALL_CANCEL_ACTION = "CALL_CANCEL_ACTION";

    // static ARGear screen sizes
    public static int mDeviceWidth = 0;
    public static int mDeviceHeight = 0;
    public static int mGLViewWidth = 0;
    public static int mGLViewHeight = 0;


    public static int REQUEST_FOR_FEELING = 125;

    // user data constant
    public static UserDataResponse userData;

    public static final String ARGEAR_API_URL = "https://apis.argear.io";
    //    public static final String ARGEAR_API_KEY = "fe49fe8fe8c757e5a4f8d48d";
    public static final String ARGEAR_API_KEY = "1459fb305e25ecec51021e4d";
    //    public static final String ARGEAR_SECRET_KEY = "5c3b1c9540b4d76134596fd3e9acb2a1aef55245a9980fdd26064ea6b8c5c48c";
    public static final String ARGEAR_SECRET_KEY = "bac29e79bd399af25e7fb66bdf1757e6a35b36a431675e0723a2de8ba6731288";
    //    public static final String ARGEAR_AUTH_KEY = "U2FsdGVkX1+k8JjhmPLPROT1F7wdSAiW08DhJymQnqpYdU/VaaSR5BwYgLzfiRsS2LXFf1YXCjfryqzIo4/M0g==";
    public static final String ARGEAR_AUTH_KEY = "U2FsdGVkX1+U/MtJuJg/QVSig8V+VzvQF+zzRms00lRucY4/ro4vxGBi3/+PdriYda87BkFyGh8hZ29tBu1o7Q==";

    // preference
    public static final String USER_PREF_NAME = BuildConfig.APPLICATION_ID + ".Preference";
    public static final String USER_PREF_NAME_FILTER = BuildConfig.APPLICATION_ID + ".ARGearFilter.Preference";
    public static final String USER_PREF_NAME_STICKER = BuildConfig.APPLICATION_ID + ".ARGearItem.Preference";
    public static boolean isFlipping = false;

    public static Map<String, String> token(Context context) {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        return map;
    }

    public static String userName(Context context){
        return SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.USERNAME);
    }

    public static String displayPitcher(Context context){
        return SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.PROFILE);
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int pxFromDp(final Context context, final int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

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

    public static void compressVideo(String input, String output, CompressMedia compressMedia, int requestCode) {
        try {

            File chkFileSize=new File(input);
            long fileSizeInBytes = chkFileSize.length();
            long fileSizeInKB = fileSizeInBytes / 1024;
            long fileSizeInMB = fileSizeInKB / 1024;
            Log.e("input path size",fileSizeInMB+"");
//
//            if(fileSizeInMB<3){
//                compressMedia.compressDone(true, requestCode);
//            }
            new Thread() {
                public void run() {
                    ArrayList<String> cmdList = new ArrayList<>();

                    cmdList.add("-y");
                    cmdList.add("-i");
                    cmdList.add(input);
                    cmdList.add("-s");
                    cmdList.add("640*480");
                    cmdList.add("-r");
                    cmdList.add("25");
                    cmdList.add("-vcodec");
                    cmdList.add("mpeg4");
                    cmdList.add("-b:v");
                    cmdList.add("1000k");
                    cmdList.add("-b:a");
                    cmdList.add("48000");
                    cmdList.add("-ac");
                    cmdList.add("2");
                    cmdList.add("-ar");
                    cmdList.add("22050");
                    cmdList.add(output);
                    Log.e("OutputPath",output);

//                    cmdList.add("-i");
//                    cmdList.add(input);
//                    cmdList.add("-c:v");
//                    cmdList.add("mpeg4");
//                    cmdList.add(output);
//                    cmdList.add("-acodec");
//                    cmdList.add("aac");
//                    cmdList.add(output);


                    StringBuilder sb = new java.lang.StringBuilder();

                    for (String str : cmdList) {
                        sb.append(str).append(" ");
                    }

                    String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                    int rc = FFmpeg.execute(cmd);
                    if (rc == RETURN_CODE_SUCCESS) {
                        Log.e("Video compressMedia", "Command Done");
                        compressMedia.compressDone(true, requestCode);
                    } else if (rc == RETURN_CODE_CANCEL) {
                        compressMedia.compressDone(false, requestCode);
                        Log.e("Video compressMedia", "Command execution cancelled by user.");
                    } else {
                        compressMedia.compressDone(false, requestCode);
                        Log.e("Video compressMedia", String.format("Command execution failed with rc=%d and the output below.", rc));
                    }

                }

            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createThumbnail(String input, String output, CompressMedia compressMedia, int requestCode) {
        try {

            new Thread() {
                public void run() {
                    ArrayList<String> cmdList = new ArrayList<>();
                    cmdList.add("-i");
                    cmdList.add(input);
                    cmdList.add("-ss");
                    cmdList.add("00:00:02");
                    cmdList.add("-vframes");
                    cmdList.add("1");
                    cmdList.add(output);

                    StringBuilder sb = new java.lang.StringBuilder();

                    for (String str : cmdList) {
                        sb.append(str).append(" ");
                    }

                    String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                    int rc = FFmpeg.execute(cmd);
                    if (rc == RETURN_CODE_SUCCESS) {
                        compressMedia.compressDone(true, requestCode);
                    } else if (rc == RETURN_CODE_CANCEL) {
                        compressMedia.compressDone(false, requestCode);
                        Log.i("alhaj", "Command execution cancelled by user.");
                    } else {
                        compressMedia.compressDone(false, requestCode);
                        Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                    }

                }

            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scalePhoto(String input, String output, CompressMedia compressMedia, int requestCode) {
        try {

            new Thread() {
                public void run() {
                    ArrayList<String> cmdList = new ArrayList<>();

                    cmdList.add("-i");
                    cmdList.add(input);
                    cmdList.add("-vf");
                    cmdList.add("scale=300:-1");
                    cmdList.add(output);

                    StringBuilder sb = new java.lang.StringBuilder();

                    for (String str : cmdList) {
                        sb.append(str).append(" ");
                    }

                    String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                    int rc = FFmpeg.execute(cmd);
                    if (rc == RETURN_CODE_SUCCESS) {
                        compressMedia.compressDone(true, requestCode);
                    } else if (rc == RETURN_CODE_CANCEL) {
                        Log.i("alhaj", "Command execution cancelled by user.");
                        compressMedia.compressDone(false, requestCode);
                    } else {
                        Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                        compressMedia.compressDone(false, requestCode);
                    }

                }

            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String createOutputPath(Context context, String extension) {
        String app_folder_path;
        if (Build.VERSION.SDK_INT >= 29) {
            app_folder_path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        } else {
            app_folder_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString() + "/Meest/";
        }
        File videoFolder = new File(app_folder_path);

        if (!videoFolder.exists()) {
            videoFolder.mkdir();
        }
        app_folder_path += "Meest_"+System.currentTimeMillis() + extension;
        return app_folder_path;
    }

    public static String createOutputPath(Context context) {
        String app_folder_path;
        if (Build.VERSION.SDK_INT >= 29) {
            app_folder_path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        } else {
            app_folder_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString() + "/Meest/";
        }
        File videoFolder = new File(app_folder_path);

        if (!videoFolder.exists()) {
            videoFolder.mkdir();
        }
        // app_folder_path += "Meest_";
        return app_folder_path;
    }

    public static String compressPhotoFromBitmap(Context inContext, Bitmap inImage)  {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage,
                "demo",
                null);
        return path;
    }

    public static String compressPhoto(Context inContext, Uri uri)  {
        Bitmap inImage = null;
        try {
            inImage = MediaStore.Images.Media.getBitmap(inContext.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return uri.getPath();
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 10, bytes);
        return MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage,
                "demo",
                null);
    }





    public static String compressImage(Context inContext,String imageUri) {
        String filePath = getRealPathFromURI(imageUri,inContext);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }
//        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = createOutputPath(inContext,".jpeg");
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }
    public static String compressImageThumb(Context inContext,String imageUri) {
        String filePath = getRealPathFromURI(imageUri,inContext);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 100.0f;
        float maxWidth = 100.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }
//        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = createOutputPath(inContext,".jpeg");
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }
    private static String getRealPathFromURI(String contentURI, Context inContext) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = inContext.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }



    public static String getPathFromUriAllVersion(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }



    public static void buildAlertForPermission(Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.Permission_Denied_Goto_settings_and_allow_permission)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
