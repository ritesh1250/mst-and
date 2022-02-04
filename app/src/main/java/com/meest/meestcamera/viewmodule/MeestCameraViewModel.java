package com.meest.meestcamera.viewmodule;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.VideoCaptureConfig;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MeestCameraViewModel  extends ViewModel {


    public ObservableBoolean isRecording = new ObservableBoolean(false);
    public ObservableBoolean isCapturing = new ObservableBoolean(false);
//    public ObservableBoolean isPhoto = new ObservableBoolean(false);
    public ObservableBoolean isFlashOn = new ObservableBoolean(false);
    public ObservableBoolean isFacingFront = new ObservableBoolean(false);
    public ObservableBoolean isEnabled = new ObservableBoolean(false);
    public ObservableBoolean isStartRecording = new ObservableBoolean(false);
    public ObservableBoolean is15sSelect = new ObservableBoolean(true);
    public ObservableInt soundTextVisibility = new ObservableInt(View.INVISIBLE);
    public MutableLiveData<Long> onDurationUpdate = new MutableLiveData<>(60000L);
    public MutableLiveData<Integer> onItemClick = new MutableLiveData<>();
//    public MutableLiveData<Musics.SoundList> onSoundSelect = new MutableLiveData<>();

    public CameraX.LensFacing lensFacing = CameraX.LensFacing.BACK;
    public Preview preview;
    public VideoCapture videoCapture;
    public ImageCapture imageCapture;
    public ImageCaptureConfig imageCaptureConfig;
    public ImageCaptureConfig.Builder builder2;
    public VideoCaptureConfig videoCaptureConfig;
    public PreviewConfig previewConfig;
    public VideoCaptureConfig.Builder builder1;
    public PreviewConfig.Builder builder;
    public ArrayList<String> videoPaths = new ArrayList<>();
    public ArrayList<String> photoPaths = new ArrayList<>();
    public int count = 0;
    public String parentPath = "";
    public long duration = 5000;
    public String soundId = "";
    public MediaPlayer audio;
    public boolean isBack = false;


    public void onClickFlash() {
        isFlashOn.set(!isFlashOn.get());
        preview.enableTorch(isFlashOn.get());
    }



    public void setOnItemClick(int type) {
        onItemClick.setValue(type);
    }

    public void onSelectSecond(boolean isSelected15s) {
        is15sSelect.set(isSelected15s);
        onDurationUpdate.setValue(isSelected15s ? 5000L : 60000L);
    }

    public void createAudioForCamera() {
        File file = new File(parentPath.concat("/recordSound.aac"));
        if (file.exists()) {
            audio = new MediaPlayer();
            try {
                audio.setDataSource(parentPath.concat("/recordSound.aac"));
                audio.prepare();
                soundTextVisibility.set(View.VISIBLE);
                onDurationUpdate.setValue((long) audio.getDuration());
                Log.i("TAG", "createAudioForCamera: " + duration);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();

    }

}
