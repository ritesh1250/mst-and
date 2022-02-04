package com.meest.metme;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.meest.Activities.base.ApiCallActivity;
import com.meest.R;
import com.meest.databinding.LoutAudioMikeBinding;
import com.meest.meestbhart.utilss.Constant;
import com.meest.utils.Helper;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class RecordingBaseActivity extends ApiCallActivity implements View.OnTouchListener{

    private boolean listenForRecord = true;
    private int commId;
    private MediaRecorder recorder = null;
    private boolean isRecording;

    private static SoundPool soundPool = null;

    static Dialog mBuilder;


    protected String[] permissionsRecord = {Manifest.permission.VIBRATE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};




    public boolean isListenForRecord() {
        return listenForRecord;
    }






    protected boolean permissionsAvailable(String[] permissions) {
        boolean granted = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }
        return granted;
    }


    File file;

    //audio recording tools
    public void startRecording() {



        if (isRecording) {
            stopRecording();
        }
        String fileName = getApplicationContext().getExternalCacheDir().getAbsolutePath() + "/" + commId + "_" + new Date().getTime() + ".amr";
        file = new File(fileName);
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        recorder.setAudioSamplingRate(8000);
        recorder.setAudioChannels(1);
        recorder.setAudioEncodingBitRate(12000);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recorder.start();

        startRecordListener.onStartRecord();

        setIsRecording(true);
    }

    void recordingDialog(Context context) {
        mBuilder =  new Dialog(context);
        mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        if (mBuilder.getWindow() != null) {
            mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mBuilder.setCancelable(true);
        mBuilder.setCanceledOnTouchOutside(true);
        mBuilder.getWindow().setGravity(Gravity.BOTTOM);
        mBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LoutAudioMikeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.lout_audio_mike, null, false);
        mBuilder.setContentView(binding.getRoot());
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(binding.progressBar, "progress", 100, 0);
        progressAnimator.setDuration(120000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
        new CountDownTimer(120000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                binding.tvTimer.setText("" + String.format("0%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                binding.tvTimer.setText("OO:OO");
                stopRecording();
                mBuilder.dismiss();
            }
        }.start();
        mBuilder.show();
    }

    public void stopRecording() {
        if (isRecording) {
            try {

                recorder.stop();
                mBuilder.dismiss();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                recorder.release();
                setIsRecording(false);
                recordingFinishedListener.onRecordingFinished(file);
                recorder = null;
            }

        }
    }

    private void setIsRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }


    public interface RecordingFinishedListener {
        void onRecordingFinished(File file);
    }

    RecordingFinishedListener recordingFinishedListener;

    public void setRecordingFinishedListener(RecordingFinishedListener recordingFinishedListener) {
        this.recordingFinishedListener = recordingFinishedListener;
    }


    public interface StartRecordListener {
        void onStartRecord();
    }

    StartRecordListener startRecordListener;

    public void setOnStartRecord(StartRecordListener startRecordListener) {
        this.startRecordListener = startRecordListener;
    }

    public static void setRecordDialogDismiss(){
        if (mBuilder!=null) {
            mBuilder.dismiss();
        }
    }


    /*@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 999:
                Constant.buildAlertForPermission(this);

                break;
        }
    }*/
}
