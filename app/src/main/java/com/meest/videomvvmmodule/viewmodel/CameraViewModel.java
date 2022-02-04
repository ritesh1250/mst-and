package com.meest.videomvvmmodule.viewmodel;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.camera.core.CameraX;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.VideoCaptureConfig;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.videomvvmmodule.model.music.Musics;
import com.meest.videomvvmmodule.model.videos.UploadVideoResponse;
import com.meest.videomvvmmodule.model.videos.UploadVideoThumbnailResponse;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.ProgressRequestBody;
import com.meest.videomvvmmodule.utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class CameraViewModel extends ViewModel {
    public ObservableBoolean isRecording = new ObservableBoolean(false);
    public ObservableBoolean isFlashOn = new ObservableBoolean(false);
    public ObservableBoolean isFacingFront = new ObservableBoolean(false);
    public ObservableBoolean isEnabled = new ObservableBoolean(false);
    public ObservableBoolean isStartRecording = new ObservableBoolean(false);
    public ObservableBoolean is15sSelect = new ObservableBoolean(true);
    public ObservableBoolean is45sSelect = new ObservableBoolean(true);
    public ObservableInt soundTextVisibility = new ObservableInt(View.INVISIBLE);

    public ObservableBoolean hideArrowPermanently = new ObservableBoolean(false);

    public ObservableBoolean isExpanded = new ObservableBoolean(false);
    public ObservableBoolean isAspectRationExpanded = new ObservableBoolean(false);
    public ObservableBoolean isTimerExpanded = new ObservableBoolean(false);

    public ObservableBoolean showGrid = new ObservableBoolean(false);

    public ObservableInt countdownTimer = new ObservableInt(0);
    public ObservableInt aspectRatio = new ObservableInt(0);


    public MutableLiveData<Long> onDurationUpdate = new MutableLiveData<>(15000L);
    public MutableLiveData<Integer> onItemClick = new MutableLiveData<>();
    public MutableLiveData<Musics.SoundList> onSoundSelect = new MutableLiveData<>();

    public CameraX.LensFacing lensFacing = CameraX.LensFacing.BACK;
    public Preview preview;
    public VideoCapture videoCapture;
    public VideoCaptureConfig videoCaptureConfig;
    public PreviewConfig previewConfig;
    public VideoCaptureConfig.Builder builder1;
    public PreviewConfig.Builder builder;
    public ArrayList<String> videoPaths = new ArrayList<>();
    public int count = 0;
    public String parentPath = "";
    public long duration = 15000;
    public String soundId = "";
    public MediaPlayer audio;
    public boolean isBack = false;


    private CompositeDisposable disposable = new CompositeDisposable();


    public void onClickFlash() {
        isFlashOn.set(!isFlashOn.get());
        preview.enableTorch(isFlashOn.get());
    }

    public void setOnItemClick(int type) {
        onItemClick.setValue(type);
    }

    public void updateCountDownTime(int i) {
        countdownTimer.set(i);
        isTimerExpanded.set(false);
    }

    public void expandOrCollapseView() {
        isExpanded.set(!isExpanded.get());
        isTimerExpanded.set(false);
        isAspectRationExpanded.set(false);
    }

    public void expandOrCollapseAspectView() {
        isAspectRationExpanded.set(!isAspectRationExpanded.get());
        isTimerExpanded.set(false);
    }

    public void hideOrShowGrid() {
        showGrid.set(!showGrid.get());
    }


    public void expandOrCollapseTimerView() {
        isTimerExpanded.set(!isTimerExpanded.get());
        isAspectRationExpanded.set(false);
    }

    public void onSelectSecond(boolean isSelected15s) {
        is15sSelect.set(isSelected15s);
        onDurationUpdate.setValue(isSelected15s ? 15000L : 30000L);
    }

    public void onSelectSecond2(boolean isSelected45s) {
        is45sSelect.set(isSelected45s);
        onDurationUpdate.setValue(isSelected45s ? 45000L : 60000L);
    }

    public void createAudioForCamera() {
        File file = new File(parentPath.concat("/recordSound.mp3"));
        if (file.exists()) {
            audio = new MediaPlayer();
            try {
                audio.setDataSource(parentPath.concat("/recordSound.mp3"));
                audio.prepare();
                soundTextVisibility.set(View.VISIBLE);
                onDurationUpdate.setValue((long) audio.getDuration());
                Log.i("TAG", "====================================================================createAudioForCamera: " + duration);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void fetchVideoUrl(String videoPath) {
//        video multipart
        MultipartBody.Part body;
        File file = new File(videoPath);
        ProgressRequestBody requestBody = new ProgressRequestBody(file, percentage -> {

        });
        body = MultipartBody.Part.createFormData("video", file.getName(), requestBody);

        disposable.add(Global.initRetrofit().getVideoPostUrl(Global.apikey, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((UploadVideoResponse updateUser, Throwable throwable) -> {
                    if (updateUser.getStatus() != null) {
//                        videoUrl=updateUser.getData().getVideo_url();
                        Log.e("video_url", "===============================================" + updateUser.getData());

                    }
                }));
    }

}

