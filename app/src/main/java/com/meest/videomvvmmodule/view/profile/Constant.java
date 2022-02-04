package com.meest.videomvvmmodule.view.profile;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.multidex.BuildConfig;

import com.arthenica.mobileffmpeg.FFmpeg;
import com.meest.videomvvmmodule.view.recordvideo.CompressMedia;

import java.io.File;
import java.util.ArrayList;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;


public class Constant {
    public static final String APP_DIRECTORY = "Meest";
    public static final String ARGEAR_API_KEY = "1459fb305e25ecec51021e4d";
    public static final String ARGEAR_API_URL = "https://apis.argear.io";
    public static final String USER_PREF_NAME = BuildConfig.APPLICATION_ID + ".Preference";
    public static final String USER_PREF_NAME_FILTER = BuildConfig.APPLICATION_ID + ".ARGearFilter.Preference";
    public static final String USER_PREF_NAME_STICKER = BuildConfig.APPLICATION_ID + ".ARGearItem.Preference";
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
        app_folder_path += System.currentTimeMillis() + extension;
        return app_folder_path;
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

                    StringBuilder sb = new StringBuilder();

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
    public static void compressVideo(String input, String output, CompressMedia compressMedia, int requestCode) {
        try {

            new Thread() {
                public void run() {
                    ArrayList<String> cmdList = new ArrayList<>();

                    cmdList.add("-i");
                    cmdList.add(input);
                    cmdList.add("-vcodec");
                    cmdList.add("h264");
                    cmdList.add("-acodec");
                    cmdList.add("aac");
                    cmdList.add(output);

                    StringBuilder sb = new StringBuilder();

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
}
