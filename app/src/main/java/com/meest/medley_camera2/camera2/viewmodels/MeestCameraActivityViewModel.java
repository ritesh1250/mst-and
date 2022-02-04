package com.meest.medley_camera2.camera2.viewmodels;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.databinding.DailogProgressBinding;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.medley_camera2.camera2.view.fragment.MeestCamera2Fragment;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;

import java.io.File;
import java.io.IOException;


public class MeestCameraActivityViewModel extends ViewModel {
    private static final String TAG = "CameraActivityViewModel";
    public ObservableInt soundTextVisibility = new ObservableInt(View.INVISIBLE);
    public MutableLiveData<Long> onDurationUpdate = new MutableLiveData<>(15000L);
    public String parentPath = "";
    public MediaPlayer audio;
    public ObservableBoolean isEnabled = new ObservableBoolean(false);
    public long duration = 15000;

    public String soundId = "";

    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private Activity activity;
    com.meest.databinding.ActivityCamera2MetmeBinding binding;
    @SuppressLint("StaticFieldLeak")
    MeestCamera2Fragment mMeestCamera2Fragment;

    private CustomDialogBuilder customDialogBuilder;


    private Dialog mBuilder;

    private DailogProgressBinding progressBinding;

    private boolean boomActive = false;
    public static int screenHeight;
    public static int screenWidth;


    public MeestCameraActivityViewModel(Context context, Activity activity, com.meest.databinding.ActivityCamera2MetmeBinding binding, MeestCamera2Fragment mMeestCamera2Fragment) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;
        this.mMeestCamera2Fragment = mMeestCamera2Fragment;
        customDialogBuilder = new CustomDialogBuilder(activity);

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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void clearGridValues() {
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
        mMeestCamera2Fragment.fullScreenTexture();

    }


    public void videoProgressbar() {

        binding.timer30.setOnClickListener(v -> {
            CameraUtil.timer = 30000;
            binding.timer30.setBackgroundResource(R.drawable.bg_black_corner_5);
            binding.timer60.setBackgroundResource(R.drawable.bg_white_corner_5);

            binding.timer30.setTextColor(context.getResources().getColor(R.color.white));
            binding.timer60.setTextColor(context.getResources().getColor(R.color.black));


        });
        binding.timer60.setOnClickListener(v -> {
            CameraUtil.timer = 60000;
            binding.timer60.setBackgroundResource(R.drawable.bg_black_corner_5);
            binding.timer30.setBackgroundResource(R.drawable.bg_white_corner_5);

            binding.timer60.setTextColor(context.getResources().getColor(R.color.white));
            binding.timer30.setTextColor(context.getResources().getColor(R.color.black));


        });
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

}
