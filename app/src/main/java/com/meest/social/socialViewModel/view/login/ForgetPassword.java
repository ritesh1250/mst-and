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
import com.meest.databinding.ActivityForgetPasswordModelBinding;
import com.meest.social.socialViewModel.viewModel.loginViewModel.ForgetViewModel;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class ForgetPassword extends AppCompatActivity {
    ActivityForgetPasswordModelBinding binding;
    ForgetViewModel model;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ForgetPassword.this, R.layout.activity_forget_password_model);
        model = new ViewModelProvider(this, new ViewModelFactory(new ForgetViewModel(binding)).createFor()).get(ForgetViewModel.class);
        initObserve();
        binding.setForgetModel(model);
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
        model.isforgot.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isforgot) {
                if (isforgot) {
                    Intent intent = new Intent(ForgetPassword.this, ForgetVerficationPassword.class);
                    startActivity(intent);

                }
            }
        });

    }


}
