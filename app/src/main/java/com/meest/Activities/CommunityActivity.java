package com.meest.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.meest.R;
import com.meest.videoEditing.videocollage.utils.ImageViewTouchListener;
import com.meest.videomvvmmodule.utils.Const;

public class CommunityActivity extends AppCompatActivity {
    WebView webview;
    ImageView imgBack;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        webview=findViewById(R.id.webview);
        imgBack=findViewById(R.id.img_back);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.loadUrl(Const.COMMUNITY_GUIDELINES);
        imgBack.setOnClickListener(v -> onBackPressed());
    }
}