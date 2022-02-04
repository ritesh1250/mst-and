package com.meest.medley_camera2.camera2.viewmodels;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.meest.R;
import com.meest.databinding.ActivityRecordAudioMedlyBinding;
import com.meest.medley_camera2.camera2.view.activity.RecordAudioActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class RecordAudioViewModel extends ViewModel {


    RecordAudioActivity activity;
    ActivityRecordAudioMedlyBinding binding;

    public String videoURL;

    public String pathkya;
    public String folderName = "files";

    public String newVideoPath,newAudioPath;


    public RecordAudioViewModel(RecordAudioActivity recordAudioActivity, ActivityRecordAudioMedlyBinding binding) {
        this.activity = recordAudioActivity;
        this.binding = binding;
    }

    public void getData()
    {
        if (activity.getIntent().getStringExtra("videoPath") != null) {
            videoURL = activity.getIntent().getStringExtra("videoPath");

            binding.recordLayout.setVisibility(View.VISIBLE);
            Toast toast = Toast.makeText(activity,"Hold The Button", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            playVideo(videoURL);
        }
        else
        {
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }

    private void playVideo(String outputVideo) {

        binding.videoView.setVideoPath(outputVideo);
        MediaController mediaController = new MediaController(activity);
        mediaController.setAnchorView(binding.videoView);
        binding.videoView.setMediaController(mediaController);
        binding.videoView.seekTo(1);
    }


    public String getFilename(){

        String format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
        sb.append("/");
        sb.append("Music");
        sb.append("/");
        sb.append("medley");
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
        sb2.append("/");
        sb2.append("Music");
        sb2.append("/");
        sb2.append("medley");
        sb2.append("/Medley"+System.currentTimeMillis());
        sb2.append(format);
        sb2.append(".aac");
        return sb2.toString();
    }

    //Remove Audio From Video

    public void removeAudio() {
        String outputVideo = getVideoFilePath();
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(activity.getResources().getString(R.string.please_wait));

        progressDialog.show();

        Config.enableLogCallback(message -> Log.e(Config.TAG, message.getText()));
        Config.enableStatisticsCallback(newStatistics -> progressDialog.setMessage("progress : video "));

        String s = "volume=" + String.format(Locale.US, "%.1f", 0.0);
        String[] command = {"-i", videoURL, "-vcodec", "copy", "-af", s, "-preset", "ultrafast", outputVideo};
        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                progressDialog.dismiss();

                //output is outputVideo
                newVideoPath = outputVideo;
                mixAudio();
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (progressDialog != null)
                    progressDialog.dismiss();
            } else {
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }

    //Mix The Audio with Video .
    private void mixAudio() {
        String outputVideo = getVideoFilePath2();
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(activity.getResources().getString(R.string.please_wait));

        progressDialog.show();

        Config.enableLogCallback(message -> Log.e(Config.TAG, message.getText()));
        Config.enableStatisticsCallback(newStatistics -> progressDialog.setMessage("progress : video "));


        String[] command  = {"-i", newVideoPath, "-i", newAudioPath, "-c:v", "copy", "-c:a", "aac", "-map", "0:v:0", "-map", "1:a:0", "-shortest", outputVideo};
        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                progressDialog.dismiss();
                Toast.makeText(activity, "Video Mix Done.", Toast.LENGTH_LONG).show();
                //output is outputVideo
                videoURL =  outputVideo;
                playVideo(outputVideo);
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (progressDialog != null)
                    progressDialog.dismiss();
            } else {
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }

    protected String getVideoFilePath() {
        String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "without_audio.mp4";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File appSpecificExternalDir = new File(activity.getExternalCacheDir(), fname);
            pathkya = appSpecificExternalDir.getAbsolutePath();
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/";
            File dir = new File(path);

            boolean isDirectoryCreated = dir.exists();
            if (!isDirectoryCreated) {
                dir.mkdir();
            }
            pathkya = path + fname;
        }
        return pathkya;
    }

    protected String getVideoFilePath2() {
        String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "mix.mp4";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File appSpecificExternalDir = new File(activity.getExternalCacheDir(), fname);
            pathkya = appSpecificExternalDir.getAbsolutePath();
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/";
            File dir = new File(path);

            boolean isDirectoryCreated = dir.exists();
            if (!isDirectoryCreated) {
                dir.mkdir();
            }
            pathkya = path + fname;
        }
        return pathkya;
    }

    public String getAudioFilePath() {
        String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + ".aac";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File appSpecificExternalDir = new File(activity.getExternalCacheDir(), fname);
            pathkya = appSpecificExternalDir.getAbsolutePath();
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/";
            File dir = new File(path);

            boolean isDirectoryCreated = dir.exists();
            if (!isDirectoryCreated) {
                dir.mkdir();
            }
            pathkya = path + fname;
        }
        return pathkya;
    }

}
