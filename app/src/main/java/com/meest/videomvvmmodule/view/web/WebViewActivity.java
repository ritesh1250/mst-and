package com.meest.videomvvmmodule.view.web;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebChromeClient;

import androidx.databinding.DataBindingUtil;

import com.meest.R;
import com.meest.databinding.ActivityWebViewBinding;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.view.base.BaseActivity;

public class WebViewActivity extends BaseActivity {

    ActivityWebViewBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        int type = getIntent().getIntExtra("type", 0);
        String url = "";
        switch (type) {
            //Terms
            case 0:
                url = Const.TERMS_URL;
                binding.appBarTitle.setText(getString(R.string.termsofuse));
                break;
            //Privacy
            case 1:
                url = Const.PRIVACY_URL;
                binding.appBarTitle.setText(getString(R.string.privacypolicy));
                break;
            //Help
            case 2:
                url = Const.HELP_URL;
                binding.appBarTitle.setText(getString(R.string.help));
                break;
            case 3:
                url = Const.COMMUNITY_GUIDELINES;
                binding.appBarTitle.setText(getString(R.string.community_guidelines));
                break;
        }
//        binding.webview.setWebChromeClient(new MyCustomChromeClient(this));
//        binding.webview.setWebViewClient(new MyCustomWebViewClient(this));
//        binding.webview.clearCache(true);
//        binding.webview.clearHistory();
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        binding.webview.getSettings().setDomStorageEnabled(true);
//        binding.webview.setWebChromeClient(new WebChromeClient());
        //  binding.webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.webview.loadUrl(url);
        binding.imgBack.setOnClickListener(v -> onBackPressed());

    }
}