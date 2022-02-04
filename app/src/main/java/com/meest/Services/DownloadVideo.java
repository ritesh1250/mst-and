package com.meest.Services;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.meest.Activities.VideoPost.CameraActivity;
import com.meest.Interfaces.DownloadMedia;
import com.meest.R;
import com.meest.meestbhart.utilss.Constant;
import com.meest.svs.activities.MusicVideosActivity;
import com.meest.svs.models.AudioDataModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadVideo extends AsyncTask<String, String, String> {

    private ProgressDialog progressDialog;
    private String videoFile = "";
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private AudioDataModel audioDataModel;
    private String fileExtension;
    private String type, trioLink1;
    private DownloadMedia downloadMedia;

    public DownloadVideo(Context context, String type, DownloadMedia downloadMedia) {
        this.type = type;
        this.context = context;
        this.downloadMedia = downloadMedia;

        fileExtension = ".mp3";
    }

    public DownloadVideo(Context context, String type, AudioDataModel audioDataModel) {
        this.type = type;
        this.context = context;
        this.audioDataModel = audioDataModel;

        fileExtension = ".mp4";
    }

    public DownloadVideo(Context context, String type, String trioLink1, AudioDataModel audioDataModel) {
        this.type = type;
        this.trioLink1 = trioLink1;
        this.context = context;
        this.audioDataModel = audioDataModel;

        fileExtension = ".mp4";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!type.equalsIgnoreCase("audioDownload")) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage(context.getString(R.string.Downloading));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        OutputStream output = null;
        InputStream input = null;

        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length
            int lengthOfFile = connection.getContentLength();

            // input stream to read file - with 8k buffer
            input = new BufferedInputStream(url.openStream(), 8192);

            //Extract file name from URL
            String fileName = "Video_" + System.currentTimeMillis() + fileExtension;

            //External directory path to save file
            String folder = Environment.getExternalStorageDirectory() + File.separator + Constant.APP_DIRECTORY + "/";

            //Create androiddeft folder if it does not exist
            File directory = new File(folder);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            videoFile = folder + fileName;

            // Output stream to write file
            output = new FileOutputStream(videoFile);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lengthOfFile));
                // Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                // writing data to file
                output.write(data, 0, count);

            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
            return "Downloaded at: " + videoFile;

        } catch (Exception e) {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            Log.e("Error: ", e.getMessage());
        }
        return "Video not Found";
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        if (progressDialog != null) {
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }
    }

    @Override
    protected void onPostExecute(String message) {
        // dismiss the dialog after the file was downloaded
        if (progressDialog != null) {
            this.progressDialog.dismiss();
        }
        String audioId, audioName;

        if (audioDataModel != null) {
            audioId = audioDataModel.getId();
            audioName = audioDataModel.getDescription();
        } else {
            audioId = "";
            audioName = "";
        }
        if (type.equalsIgnoreCase("trio")) {
//            Intent shareIntent = new Intent(context, TrioActivity.class);
//            shareIntent.putExtra("videoFile", videoFile);
//            shareIntent.putExtra("thumbnail", model.getThumbnail_image());
//            context.startActivity(shareIntent);
            Intent intent = new Intent(context, MusicVideosActivity.class);
            intent.putExtra("audioId", audioId);
            intent.putExtra("audioName", audioName);
            intent.putExtra("trioVideo1", videoFile);
            intent.putExtra("type", "trio");
            context.startActivity(intent);
        } else if (type.equalsIgnoreCase("duo")) {
            Intent intent = new Intent(context, CameraActivity.class);
            intent.putExtra("duoVideo", videoFile);
            intent.putExtra("audioId", audioId);
            intent.putExtra("type", "duo");
            context.startActivity(intent);
        } else if (type.equalsIgnoreCase("trioEnd")) {
            Log.d("Tag", message);

            // Display File path after downloading
            Intent intent = new Intent(context, CameraActivity.class);
            intent.putExtra("type", "trio");
            intent.putExtra("audioId", audioId);
            intent.putExtra("trioVideo1", trioLink1);
            intent.putExtra("trioVideo2", videoFile);
            context.startActivity(intent);
        } else if (type.equalsIgnoreCase("audioDownload")) {
            Log.d("Tag", message);
            // Display File path after downloading
            downloadMedia.downloadDone(true, videoFile);
        } else {
            Log.d("Tag", message);
            // Display File path after downloading

            Log.v("testing", "videoFile == " + videoFile);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("video/*");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "sub");
            shareIntent.putExtra(Intent.EXTRA_TITLE, "title");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(videoFile));
            context.startActivity(Intent.createChooser(shareIntent,
                    "share video"));
        }
    }
}