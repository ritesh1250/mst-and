package com.meest.videomvvmmodule.viewmodel;

import android.media.MediaPlayer;
import android.view.View;

import androidx.camera.core.CameraX;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.VideoCaptureConfig;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.model.music.Musics;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivityViewModel extends ViewModel {
    public ObservableBoolean isRecording = new ObservableBoolean(false);
    public ObservableBoolean isFlashOn = new ObservableBoolean(false);
    public ObservableBoolean isFacingFront = new ObservableBoolean(false);
    public ObservableBoolean isEnabled = new ObservableBoolean(false);
    public ObservableBoolean isStartRecording = new ObservableBoolean(false);
    public MutableLiveData<Integer> onItemClick = new MutableLiveData<>();
    public MutableLiveData<Long> onDurationUpdate = new MutableLiveData<>(15000L);
    public CameraX.LensFacing lensFacing = CameraX.LensFacing.BACK;
    public Preview preview;
    public ObservableBoolean is15sSelect = new ObservableBoolean(true);
    public ObservableBoolean is45sSelect = new ObservableBoolean(true);
    public long duration = 15000;
    public ArrayList<String> videoPaths = new ArrayList<>();
    public VideoCapture videoCapture;
    public int count = 0;
//    public MediaPlayer audio;
    private CompositeDisposable disposable = new CompositeDisposable();
    public void setOnItemClick(int type) {
        onItemClick.setValue(type);
    }
    public void onSelectSecond(boolean isSelected15s) {
        is15sSelect.set(isSelected15s);
        onDurationUpdate.setValue(isSelected15s ? 15000L : 30000L);
    }

    public void onSelectSecond2(boolean isSelected45s) {
        is45sSelect.set(isSelected45s);
        onDurationUpdate.setValue(isSelected45s ? 45000L : 60000L);
    }



}
