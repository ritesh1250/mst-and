package com.meest.medley_camera2.camera2.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import com.meest.R;
import com.meest.databinding.ActivityRecordAudioMedlyBinding;
import com.meest.medley_camera2.camera2.viewmodels.RecordAudioViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class RecordAudioActivity extends AppCompatActivity {

    private MediaRecorder recorder = null;
    private final int[] output_formats = {MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP};

    RecordAudioViewModel viewModel;
    ActivityRecordAudioMedlyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_record_audio_medly);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new RecordAudioViewModel(this, binding))).get(RecordAudioViewModel.class);
        binding.setViewModel(viewModel);

        viewModel.getData();

        handsFreeRecordMethod();

        clickEvent();

    }

    public void clickEvent() {
        binding.checkIcon.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("song", viewModel.videoURL);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        binding.closeIcon.setOnClickListener(v -> finish());
    }

    boolean goneFlag = false;
    float initialTouchX;

    final Handler handlerLongPress = new Handler();
    Runnable mLongPressed = () -> {
        goneFlag = true;
        binding.startRecord.setImageResource(R.drawable.ic_record_start);
        startRecording();
    };

    @SuppressLint("ClickableViewAccessibility")
    public void handsFreeRecordMethod() {
        binding.startRecord.setOnTouchListener((v, event) -> {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    initialTouchX = event.getRawX();
                    handlerLongPress.postDelayed(mLongPressed, 300);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    handlerLongPress.removeCallbacks(mLongPressed);
                    if (Math.abs(event.getRawX() - initialTouchX) <= 2 && !goneFlag) {
                        //Code for single click
                        Toast toast = Toast.makeText(this, "Hold The Button", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return false;
                    } else {
                        stopRecording();
                        findViewById(R.id.recordLayout).setVisibility(View.GONE);
                        viewModel.removeAudio();
                    }

                    return true;
            }
            return false;
        });
    }


    //Start Audio Recording
    private void startRecording() {

        if (!binding.animationView.isAnimating()) {
            binding.animationView.setVisibility(View.VISIBLE);
            binding.animationView.playAnimation();
        }
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        int currentFormat = 0;
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        viewModel.newAudioPath = viewModel.getAudioFilePath();
        recorder.setOutputFile(viewModel.newAudioPath);
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MediaRecorder.OnErrorListener errorListener = (mr, what, extra) -> Log.e("TAG", "onError: " + what + ", " + extra);


    private MediaRecorder.OnInfoListener infoListener = (mr, what, extra) -> Log.e("TAG", "Warning: " + what + ", " + extra);

    //Stop Audio Recording
    private void stopRecording() {

        if (null != recorder) {
            if (binding.animationView.isAnimating()) {
                binding.animationView.cancelAnimation();
                binding.animationView.setVisibility(View.GONE);
            }
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}