package com.meest.social.socialViewModel.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.meest.R;
import com.meest.databinding.ActivityResetModelBinding;

import com.meest.social.socialViewModel.viewModel.loginViewModel.ResetPasswordModel;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class ResetPassword extends AppCompatActivity {
    ActivityResetModelBinding binding;
    ResetPasswordModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_model);
        model = new ViewModelProvider(this, new ViewModelFactory(new ResetPasswordModel(this,binding)).createFor()).get(ResetPasswordModel.class);
        initObserve();
        binding.setResetModel(model);
        model.activity = this;
    }

    private void initObserve() {
        CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(this);
        model.isloading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isloading) {
                if (isloading) {
                    customDialogBuilder.showLoadingDialog();
                } else {
                    customDialogBuilder.hideLoadingDialog();
                }
            }
        });
        model.toast.observe(this, s -> {
            if (s != null && !s.isEmpty()) {
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            }
        });
        model.isReset.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isReset) {
                if (isReset) {
                    Intent intent = new Intent(ResetPassword.this, LoginSignUp.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }
        });

    }
}
