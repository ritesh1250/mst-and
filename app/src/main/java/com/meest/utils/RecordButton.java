package com.meest.utils;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;


import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.meest.R;
import com.meest.databinding.LoutAudioMikeBinding;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RecordButton extends AppCompatImageView implements View.OnTouchListener {


    private ScaleAnim scaleAnim;
    private boolean listenForRecord = true;
    private int commId;
    private MediaRecorder recorder = null;
    private boolean isRecording;

    private static SoundPool soundPool = null;
    private static int soundStart, soundEnd;

    static Dialog mBuilder;


    public RecordButton(Context context, int id) {
        super(context);
        init(context, null);
        commId = id;
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordButton);

            int imageResource = typedArray.getResourceId(R.styleable.RecordButton_mic_icon, -1);

            if (imageResource != -1) {
                setTheImageResource(imageResource);
            }

            typedArray.recycle();
        }

        if (soundPool == null) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .build();
            //these are just two wav files with short intro and exit sounds
            soundStart = soundPool.load(getContext(), R.raw.sound, 0);
            soundEnd = soundPool.load(getContext(), R.raw.sound, 0);
        }

        scaleAnim = new ScaleAnim(this);
        this.setOnTouchListener(this);
    }

    private void setTheImageResource(int imageResource) {
        Drawable image = AppCompatResources.getDrawable(getContext(), imageResource);
        setImageDrawable(image);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setClip(this);
    }

    public void setClip(View v) {
        if (v.getParent() == null) {
            return;
        }

        if (v instanceof ViewGroup) {
            ((ViewGroup) v).setClipChildren(false);
            ((ViewGroup) v).setClipToPadding(false);
        }

        if (v.getParent() instanceof View) {
            setClip((View) v.getParent());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (isListenForRecord()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ((RecordButton) v).startScale();

                    // soundPool.play(soundStart,1,1,0,0,1);
                    if (permissionsAvailable(permissionsRecord)) {

                        startRecording();
                        recordingDialog(v.getContext());
                    } else {
                        ActivityCompat.requestPermissions((Activity) getContext(), permissionsRecord, 999);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    stopRecording();
                    //  soundPool.play(soundEnd,1,1,0,0,1);
                    ((RecordButton) v).stopScale();
                    break;
            }

        }
        return isListenForRecord();
    }

    protected String[] permissionsRecord = {Manifest.permission.VIBRATE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    protected boolean permissionsAvailable(String[] permissions) {
        boolean granted = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }
        return granted;
    }


    File file;

    //audio recording tools
    private void startRecording() {

        if (isRecording) {
            stopRecording();
        }
        String fileName = getContext().getExternalCacheDir().getAbsolutePath() + "/" + commId + "_" + new Date().getTime() + ".amr";
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
/*    try {
        mRecorder.stop();
    }
     catch(RuntimeException e) {
    }
      finally {
        mRecorder.release();
        mRecorder = null;
    }*/

    private void stopRecording() {
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


    protected void startScale() {
        scaleAnim.start();
    }

    protected void stopScale() {
        scaleAnim.stop();
    }

    public void setListenForRecord(boolean listenForRecord) {
        this.listenForRecord = listenForRecord;

    }

    public boolean isListenForRecord() {
        return listenForRecord;
    }

    public interface StartRecordListener {
        void onStartRecord();
    }

    StartRecordListener startRecordListener;

    public void setOnStartRecord(StartRecordListener startRecordListener) {
        this.startRecordListener = startRecordListener;
    }

    //callback for when a recording has been made
    public interface RecordingFinishedListener {
        void onRecordingFinished(File file);
    }

    RecordingFinishedListener recordingFinishedListener;

    public void setRecordingFinishedListener(RecordingFinishedListener recordingFinishedListener) {
        this.recordingFinishedListener = recordingFinishedListener;
    }


    public class ScaleAnim {
        private View view;

        public ScaleAnim(View view) {
            this.view = view;
        }

        void start() {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.3f);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.3f);
            set.setDuration(150);
            set.setInterpolator(new AccelerateDecelerateInterpolator());
            set.playTogether(scaleY, scaleX);
            set.start();
        }

        void stop() {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f);
            //        scaleY.setDuration(250);
            //        scaleY.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f);
            //        scaleX.setDuration(250);
            //        scaleX.setInterpolator(new DecelerateInterpolator());

            set.setDuration(150);
            set.setInterpolator(new AccelerateDecelerateInterpolator());
            set.playTogether(scaleY, scaleX);
            set.start();
        }
    }


    public static void setRecordDialogDismiss(){
        if (mBuilder!=null)
            mBuilder.dismiss();
    }

}
