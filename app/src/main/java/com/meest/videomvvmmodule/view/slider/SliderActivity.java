package com.meest.videomvvmmodule.view.slider;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.meest.R;
import com.meest.databinding.ActivitySliderBinding;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;

public class SliderActivity extends BaseActivity {

    ActivitySliderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_slider);
        initListener();
    }

    private void initListener() {
        binding.tvSkip.setOnClickListener(v -> {
            openActivity(new MainVideoActivity());
            finish();
        });
    }
}
