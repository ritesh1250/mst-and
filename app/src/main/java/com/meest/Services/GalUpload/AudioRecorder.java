package com.meest.Services.GalUpload;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;



public class AudioRecorder {

Context context;

    final MediaRecorder recorder = new MediaRecorder();
    final String path;
    String recorderState;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public AudioRecorder(String path,Context context) {
        this.context = context;
        this.path = path;
        sharedPreferences=context.getSharedPreferences("DATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void start() throws IOException {
        Log.e("Recording ","Start");
        Log.e("path ",path+"");
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED))  {
            throw new IOException("SD Card is not mounted.  It is " + state + ".");
        }

        // make sure the directory we plan to store the recording in exists
        File directory = new File(path).getParentFile();
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created.");
        }
        boolean isMicFree = true;
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(path);
        recorder.prepare();
        try {
            recorder.start();
            recorderState="0";
            editor.putBoolean("isMicFree",false);
            editor.commit();

        } catch (IllegalStateException e) {
            Log.e("MediaRecorder", "start() failed: MIC is busy");
            recorderState="1";
            editor.putBoolean("isMicFree",true);
            editor.commit();
        }
    }

    /**
     * Stops a recording that has been previously started.
     */


    public void stop() throws IOException {
        Log.e("Recording ","Stop");
        recorder.stop();
        recorder.release();
        editor.putBoolean("isMicFree",true);
        editor.commit();
    }

}