package com.meest.social.socialViewModel.view.postVisibilitySetting;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.meest.R;
import com.meest.databinding.PostVisibilitySettingModelBinding;
import com.meest.social.socialViewModel.viewModel.postVisibilitySettingViewModel.PostVisibilitySettingViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class PostVisibilitySetting extends AppCompatActivity {
    private PostVisibilitySettingModelBinding binding;
    private PostVisibilitySettingViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.post_visibility_setting_model);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new PostVisibilitySettingViewModel(this, PostVisibilitySetting.this, binding)).createFor()).get(PostVisibilitySettingViewModel.class);
        viewModel.getData();
        clickEvents();
    }

    private void clickEvents() {
        binding.vpvsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> viewModel.radioGroupCheckChange(checkedId));
        binding.imageBack.setOnClickListener(v -> viewModel.backPressed());
    }

    @Override
    public void onBackPressed() {
        viewModel.backPressed();
        super.onBackPressed();


    }
}