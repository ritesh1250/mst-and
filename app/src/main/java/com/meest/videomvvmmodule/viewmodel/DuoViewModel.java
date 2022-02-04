package com.meest.videomvvmmodule.viewmodel;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
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

import com.downloader.OnDownloadListener;
import com.meest.videomvvmmodule.model.music.Musics;
import com.meest.videomvvmmodule.model.videos.UploadVideoResponse;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.ProgressRequestBody;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.recordvideo.DuoActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class DuoViewModel extends ViewModel {
    public ObservableBoolean isRecording = new ObservableBoolean(false);
    public ObservableBoolean isFlashOn = new ObservableBoolean(false);
    public ObservableBoolean isFacingFront = new ObservableBoolean(false);
    public ObservableBoolean isEnabled = new ObservableBoolean(false);
    public ObservableBoolean isStartRecording = new ObservableBoolean(false);
    public ObservableBoolean is15sSelect = new ObservableBoolean(true);
    public ObservableBoolean is45sSelect = new ObservableBoolean(true);
    public ObservableInt soundTextVisibility = new ObservableInt(View.INVISIBLE);
    public ObservableBoolean isFirstTime = new ObservableBoolean(true);

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

    public void createAudioForCamera(SessionManager sessionManager, DuoActivity duoActivity) {
        String parentPath = new File(duoActivity.getExternalCacheDir().getPath()).getPath();
        File file = new File(parentPath.concat("/Medley_" + sessionManager.getUser().getData().getUserName() + "_" + soundId + ".mp3"));
        Log.e("path=======1", file.getPath());
        if (file.exists()) {
            Log.e("path=======2", file.getPath());
            audio = new MediaPlayer();
            try {
                audio.setDataSource(parentPath.concat("/Medley_" + sessionManager.getUser().getData().getUserName() + "_" + soundId + ".mp3"));
                audio.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("path=======:", "null");
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

