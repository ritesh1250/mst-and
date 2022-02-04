package com.meest.metme.camera2.videoEditing;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.meest.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class RecordAudioActivity extends AppCompatActivity {

    private static final String TAG = "RecordAudioActivity";

    ImageView startRecord;
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FILE_EXT_AAC = ".aac";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private  MediaRecorder recorder = null;
    private final int currentFormat = 0;
    private final int[] output_formats = { MediaRecorder.OutputFormat.MPEG_4,             MediaRecorder.OutputFormat.THREE_GPP };
    private final String[] file_exts = {AUDIO_RECORDER_FILE_EXT_AAC, AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP };
    private String videoURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);

        startRecord = findViewById(R.id.startRecord);


        if (getIntent().getStringExtra("videoPath") != null) {
            videoURL = getIntent().getStringExtra("videoPath");
            Log.e(TAG, "videoPath: " + videoURL);
           playVideo(videoURL);

        }

        findViewById(R.id.recordLayout).setVisibility(View.VISIBLE);
        Toast.makeText(this, "Hold the record button", Toast.LENGTH_LONG).show();
        handsFreeRecordMethod();



        findViewById(R.id.checkIcon).setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("videoPath", videoURL);
            setResult(RESULT_OK,resultIntent);
            finish();
        });

        findViewById(R.id.closeIcon).setOnClickListener(v -> finish());


    }

    @SuppressLint("ClickableViewAccessibility")
    public void handsFreeRecordMethod() {
        startRecord.setOnTouchListener((v, event) -> {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    startRecord.setImageResource(R.drawable.record_start_mic);
                    startRecording();
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:

                    stopRecording();
                    //startRecord.setVisibility(View.GONE);
                    findViewById(R.id.recordLayout).setVisibility(View.GONE);
                    removeAudio();

                    return true;
            }

            return false;

        });
    }

    private String getFilename(){

//        String filepath = Environment.getExternalStorageDirectory().getPath();
//        File file = new File(filepath,AUDIO_RECORDER_FOLDER);
//
//        if(!file.exists()){
//            file.mkdirs();
//        }
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


    protected String getAudioFilePath() {
        String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + ".aac";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File appSpecificExternalDir = new File(getExternalCacheDir(), fname);
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


    LottieAnimationView animationView;
    //Start Audio Recording
    private void startRecording(){
        animationView = findViewById(R.id.animationView);
        if (!animationView.isAnimating()) {
            animationView.setVisibility(View.VISIBLE);
            animationView.playAnimation();
        }
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        newAudioPath = getAudioFilePath();
        recorder.setOutputFile(newAudioPath);
        Log.e(TAG, "Record Audio OutputFile: "+getFilename());
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            Log.e(TAG, "Exception: "+e.getMessage());
            e.printStackTrace();
        }
    }

    private MediaRecorder.OnErrorListener errorListener = (mr, what, extra) -> Log.e(TAG, "onError: "+what + ", " + extra );

    private MediaRecorder.OnInfoListener infoListener = (mr, what, extra) -> Log.e(TAG, "Warning: "+what + ", " + extra );

    //Stop Audio Recording
    private void stopRecording(){
        if(null != recorder){

            if (animationView.isAnimating()) {
                animationView.cancelAnimation();
                animationView.setVisibility(View.GONE);

            }

            recorder.stop();
            recorder.reset();
            recorder.release();

            recorder = null;


        }
    }

    //Remove Audio From Video
    String newVideoPath,newAudioPath;
    private void removeAudio() {
        String outputVideo = getVideoFilePath();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

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
                Log.e(TAG, "Audio Remove outFile: " + outputVideo);
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
    String pathkya;
    String folderName = "files";

    protected String getVideoFilePath() {
        String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "without_audio.mp4";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File appSpecificExternalDir = new File(getExternalCacheDir(), fname);
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
            File appSpecificExternalDir = new File(getExternalCacheDir(), fname);
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

    private void playVideo(String outputVideo) {
       VideoView video_view = findViewById(R.id.video_view);
        video_view.setVideoPath(outputVideo);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video_view);
        video_view.setMediaController(mediaController);
        video_view.seekTo(1);
    }

    //Mix The Audio with Video .
    private void mixAudio() {
        String outputVideo = getVideoFilePath2();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        progressDialog.show();

        Config.enableLogCallback(message -> Log.e(Config.TAG, message.getText()));
        Config.enableStatisticsCallback(newStatistics -> progressDialog.setMessage("progress : video "));

        Log.e(TAG, "mixAudio: newVideoPath "+newVideoPath);
        Log.e(TAG, "mixAudio: newAudioPath "+newAudioPath);
        String[] command  = {"-i", newVideoPath, "-i", newAudioPath, "-c:v", "copy", "-c:a", "aac", "-map", "0:v:0", "-map", "1:a:0", "-shortest", outputVideo};
        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                progressDialog.dismiss();
                Toast.makeText(this, "Video Mix Done.", Toast.LENGTH_LONG).show();
                //output is outputVideo
                Log.e(TAG, "MixAudio outFile: " + outputVideo);
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
}