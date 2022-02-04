package com.meest.social.socialViewModel.view.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.meest.R;
import com.meest.databinding.ChangePasswordModelBinding;
import com.meest.social.socialViewModel.viewModel.loginViewModel.ChangePasswordViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class ChangePassword extends AppCompatActivity {

    ChangePasswordModelBinding binding;
    ChangePasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.change_password_model);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new ChangePasswordViewModel(this,binding)).createFor()).get(ChangePasswordViewModel.class);
        viewModel.setToggleForPassword();
        clickEvents();
    }

    private void clickEvents() {
        binding.resetPassword.setOnClickListener(v -> viewModel.resetBtnPress());
    }
}