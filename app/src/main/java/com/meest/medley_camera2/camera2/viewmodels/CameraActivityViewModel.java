package com.meest.medley_camera2.camera2.viewmodels;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.meest.R;
import com.meest.databinding.ActivityCamera2MedlyBinding;
import com.meest.databinding.DailogProgressBinding;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.medley_camera2.camera2.view.fragment.Camera2Fragment;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CameraActivityViewModel extends ViewModel {
    private static final String TAG = "CameraActivityViewModel";
    public ObservableInt soundTextVisibility = new ObservableInt(View.INVISIBLE);
    public MutableLiveData<Long> onDurationUpdate = new MutableLiveData<>(15000L);
    public ObservableBoolean videoClickChk = new ObservableBoolean(false);
    public String parentPath = "";
    public MediaPlayer audio;
    public ObservableBoolean isEnabled = new ObservableBoolean(false);
    public ArrayList<String> videoPaths = new ArrayList<>();
    public long duration = 15000;
    public String soundId = "";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private Activity activity;
    ActivityCamera2MedlyBinding binding;
    @SuppressLint("StaticFieldLeak")
    Camera2Fragment mCamera2Fragment;

    private CustomDialogBuilder customDialogBuilder;

    private Dialog mBuilder;

    private DailogProgressBinding progressBinding;

    public boolean boomActive = false;

    public CameraActivityViewModel(Context context, Activity activity, ActivityCamera2MedlyBinding binding, Camera2Fragment mCamera2Fragment) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;
        this.mCamera2Fragment = mCamera2Fragment;

        customDialogBuilder = new CustomDialogBuilder(activity);
    }

//    public void createAudioForCamera() {
//
//        File file = new File(parentPath.concat("/recordSound.mp3"));
//        if (file.exists()) {
//            audio = new MediaPlayer();
//            try {
//                audio.setDataSource(parentPath.concat("/recordSound.mp3"));
//                audio.prepare();
//                soundTextVisibility.set(View.VISIBLE);
//                onDurationUpdate.setValue((long) audio.getDuration());
//                initSoundViews();
//                Log.e(TAG, "createAudioForCamera : " + audio.getDuration());
//            } catch (IOException e) {
//                Log.e(TAG, "createAudioForCamera: "+e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }

    public void createAudioForCamera() {
        File file = new File(parentPath.concat("/recordSound.mp3"));
        if (file.exists()) {
            audio = new MediaPlayer();
            try {
                audio.setDataSource(parentPath.concat("/recordSound.mp3"));
                audio.prepare();
                soundTextVisibility.set(View.VISIBLE);
                onDurationUpdate.setValue((long) audio.getDuration());
                initSoundViews();
                Log.i("TAG", "====================================================================createAudioForCamera: " + duration);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initSoundViews() {
        binding.videoWithSound.setOnClickListener(v -> {
            if (mCamera2Fragment.isRecordingVideo()) {
                mCamera2Fragment.stopRecordingVideo();
                if (audio != null) {
                    audio.stop();
                    binding.segmentedProgressBar.setVisibility(View.GONE);
                    binding.segmentedProgressBar.pause();
                }
            } else {
                if (audio != null) {
                    audio.start();
                    long Duration = audio.getDuration();
                    initProgressBar(Duration);

                    Log.e(TAG, "Duration 2: " + audio.getDuration());
                }
                mCamera2Fragment.startRecordingVideo();
                //video.setImageResource(R.drawable.ic_record_stop_);
            }

        });
    }

    public void initProgressBar(long Duration) {
        binding.segmentedProgressBar.setListener(mills -> {
            isEnabled.set(mills >= Duration);
            if (mills == Duration) {
                Log.e(TAG, "Progress Finish: ");
                if (mCamera2Fragment.isRecordingVideo()) {
                    if (audio != null) {
                        audio.stop();
                    }
                    mCamera2Fragment.stopRecordingVideo();
                }
            }
        });
        binding.segmentedProgressBar.setShader(new int[]{ContextCompat.getColor(activity, R.color.new_action_color), ContextCompat.getColor(activity, R.color.new_action_color)});
        binding.segmentedProgressBar.enableAutoProgressView(Duration);
        binding.segmentedProgressBar.setVisibility(View.VISIBLE);
        binding.segmentedProgressBar.resume();
    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            filesDir = activity.getExternalFilesDir(null);
        } else {
            // Load another directory, probably local memory
            filesDir = activity.getFilesDir();
        }
        if (filesDir != null) {
            parentPath = filesDir.getPath();
        }
        return filesDir;
    }

    public void downLoadMusic(String endPoint) {
        PRDownloader.download(endPoint, getPath().getPath(), "recordSound.mp3")
//        PRDownloader.download(Const.ITEM_BASE_URL + endPoint, getPath().getPath(), "recordSound.mp3")
                .build()
                .setOnStartOrResumeListener(() -> customDialogBuilder.showLoadingDialog())
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        customDialogBuilder.hideLoadingDialog();
                        Log.e(TAG, "onDownloadComplete: ");
                        createAudioForCamera();
                    }

                    @Override
                    public void onError(Error error) {
                        Log.e(TAG, "onError: " + error.toString());
                        customDialogBuilder.hideLoadingDialog();
                    }
                });
    }

//    public void arCameraCode() {
//        Intent intent = new Intent(activity, MainActivity.class);
//        intent.putExtra("title", "AR Filters");
//        activity.startActivity(intent);
//        activity.finish();
//    }

    public void initProgressDialog() {
        mBuilder = new Dialog(activity);
        mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        if (mBuilder.getWindow() != null) {
            mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        progressBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dailog_progress, null, false);
        progressBinding.progressBar.setShader(new int[]{ContextCompat.getColor(activity, R.color.colorTheme), ContextCompat.getColor(activity, R.color.colorTheme), ContextCompat.getColor(activity, R.color.colorTheme)});
//        Glide.with(this)
//                .load(R.drawable.loader_gif)
//                .into(progressBinding.ivParent);

        mBuilder.setContentView(progressBinding.getRoot());
    }

    public void disableOptions() {
        if (CameraUtil.bitmap != null) {
            CameraUtil.bitmap.recycle();
            CameraUtil.bitmap = null;
        }
        CameraUtil.currentCamera = "Photo";

        CameraUtil.twoGridValue = 0;
        CameraUtil.threeGridValue = 0;
        CameraUtil.fourGridValue = 0;
        CameraUtil.sixGridValue = 0;
        CameraUtil.twoGridValueHorizontal = 0;

        CameraUtil.previewComplete = false;
        CameraUtil.gridLayoutActive = false;

        CameraUtil.gridLayout2Active = false;
        CameraUtil.gridLayout3Active = false;
        CameraUtil.gridLayout4Active = false;
        CameraUtil.gridLayout6Active = false;
        CameraUtil.gridLayout2ActiveHorizontal = false;

        CameraUtil.gridLayoutHVideoActive = false;

        CameraUtil.gridCamera = false;
        CameraUtil.superZoomEnable = false;
        CameraUtil.handsFreeEnable = false;
        CameraUtil.multiSnapEnable = false;
        CameraUtil.timerEnable = false;
        CameraUtil.isRunningTimer = false;
        CameraUtil.speedEnable = false;
        CameraUtil.aspectRatioEnable = false;


        CameraUtil.focusEnable = false;
        CameraUtil.speed = "Normal";
        CameraUtil.imageUri = null;

    }

    public int absVal(int value) {
        if (value < 0)
            value = (-1 * value);
        return value;
    }

    public void hideGridLayout() {
        mCamera2Fragment.hideTheGridLayout();
        clearGridValues();
    }

    public void hideLayouts() {
        binding.switchCamera.setVisibility(View.GONE);
        CameraUtil.progressTimer = false;
        binding.progressTimerParent.setVisibility(View.GONE);
        binding.segmentedProgressBar.setVisibility(View.GONE);

        if (binding.llGalleryLayout.getVisibility() == View.VISIBLE) {
            binding.llGalleryLayout.setVisibility(View.GONE);
//            binding.cardViewParentLayout.setVisibility(View.GONE);
        }
        binding.musicParentLayout.setVisibility(View.GONE);
        if (binding.video.getVisibility() == View.VISIBLE)
            binding.video.setVisibility(View.GONE);

        if (!CameraUtil.gridLayoutActive)
            binding.gidParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.superZoomEnable)
            binding.superZoomParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.gridCamera)
            binding.gridLineParent.setVisibility(View.GONE);

        if (!CameraUtil.handsFreeEnable)
            binding.handsFreeParentLayout.setVisibility(View.GONE);


        if (!CameraUtil.timerEnable)
            binding.timerParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.focusEnable)
            binding.focusParentLayout.setVisibility(View.GONE);

        //if (!CameraUtil.aspectRatioEnable)

        binding.aspectRatioParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.speedEnable)
            binding.speedParentLayout.setVisibility(View.GONE);

        if (!boomActive)
            binding.boomParentLayout.setVisibility(View.GONE);
//        binding.arCameraParentLayout.setVisibility(View.GONE);

    }

    public void showLayouts() {
        binding.llGalleryLayout.setVisibility(View.VISIBLE);
        binding.switchCamera.setVisibility(View.VISIBLE);
        binding.segmentedProgressBar.publishProgress(0);
        binding.video.setImageResource(R.drawable.ic_record_start);
        binding.cardViewParentLayout.setVisibility(View.VISIBLE);
//        binding.gallery.setVisibility(View.VISIBLE);
        binding.gridLineParent.setVisibility(View.VISIBLE);

        binding.timerParentLayout.setVisibility(View.VISIBLE);
        binding.video.setVisibility(View.VISIBLE);
        binding.gridLineParent.setVisibility(View.VISIBLE);
        binding.gidParentLayout.setVisibility(View.VISIBLE);
        binding.aspectRatioParentLayout.setVisibility(View.VISIBLE);
        binding.superZoomParentLayout.setVisibility(View.VISIBLE);
        binding.handsFreeParentLayout.setVisibility(View.VISIBLE);
        binding.speedParentLayout.setVisibility(View.VISIBLE);
        binding.focusParentLayout.setVisibility(View.VISIBLE);
        CameraUtil.progressTimer = true;
        binding.progressTimerParent.setVisibility(View.VISIBLE);
        binding.segmentedProgressBar.setVisibility(View.VISIBLE);
        binding.boomParentLayout.setVisibility(View.VISIBLE);

        binding.musicParentLayout.setVisibility(View.VISIBLE);
//        binding.arCameraParentLayout.setVisibility(View.VISIBLE);
    }

    private void clearGridValues() {
        //Grid 2 Layout
        CameraUtil.twoGridValue = 0;
        CameraUtil.gridLayout2Active = false;

        //Grid 3 Layout
        CameraUtil.threeGridValue = 0;
        CameraUtil.gridLayout3Active = false;

        //Grid 6 Layout
        CameraUtil.fourGridValue = 0;
        CameraUtil.gridLayout4Active = false;

        //Grid 6 Layout
        CameraUtil.sixGridValue = 0;
        CameraUtil.gridLayout6Active = false;

        //Grid 2 Layout Horizontal
        CameraUtil.twoGridValueHorizontal = 0;
        CameraUtil.gridLayout2ActiveHorizontal = false;


        //Grid 2 Video Layout Horizontal
        CameraUtil.gridLayoutHVideoActive = false;


        binding.checkImage.setImageResource(0);
        binding.checkImage.setImageResource(R.drawable.ic_outline_add_24);

        binding.progressBar.setProgress(0);

        mCamera2Fragment.fullscreenTexture();
    }

}
