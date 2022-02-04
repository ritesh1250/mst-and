package com.meest.videomvvmmodule.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.meest.BuildConfig;
import com.meest.R;
import com.meest.databinding.ActivitySplashBinding;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;

public class SplashActivity extends BaseActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        Global.apikey = BuildConfig.APIKEY;
        sessionManager.saveStringValue("ApiKey", BuildConfig.APIKEY);
        new Handler().postDelayed(() -> {
            if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                Global.apikey = BuildConfig.APIKEY;
                startActivity(new Intent(SplashActivity.this, MainVideoActivity.class));
                finish();
            }
        }, 500);
    }
}
