package com.meest.Activities.VideoPost;

import android.os.Bundle;
import android.view.WindowManager;

import com.meest.R;

public class CameraActivity extends BaseCameraActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_camera_post);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // changing static variables to default
        BaseCameraActivity.audioUrl = "=";
        BaseCameraActivity.audioId = "";
        BaseCameraActivity.selectedAudioType = "none";

        if(getIntent().getExtras() != null) {
            type = getIntent().getExtras().getString("type", "svs");

            if(type.equalsIgnoreCase("duo")) {
                duoVideo = getIntent().getExtras().getString("duoVideo");
                audioId = getIntent().getExtras().getString("audioId", "");
            } else if (type.equalsIgnoreCase("trio")) {
                trioVideo1 = getIntent().getExtras().getString("trioVideo1");
                trioVideo2 = getIntent().getExtras().getString("trioVideo2");
                audioId = getIntent().getExtras().getString("audioId", "");
            }
        }

        onCreateActivity();
        videoWidth = 540;
        videoHeight = 960;
        cameraWidth = 960;
        cameraHeight = 540;
    }



}
