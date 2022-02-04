package com.meest.social.socialViewModel.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.meest.MainActivity;
import com.meest.R;
import com.meest.databinding.ActivityMedleyOrMeestBinding;
import com.meest.meestbhart.register.DefaultAppResponse;
import com.meest.meestbhart.register.DefaultParam;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.social.socialViewModel.viewModel.loginViewModel.ForgetViewModel;
import com.meest.social.socialViewModel.viewModel.loginViewModel.MeedleyOrMeestViewModel;
import com.meest.social.socialViewModel.viewModel.loginViewModel.VerificationCodeViewModel;
import com.meest.videomvvmmodule.SplashScreenVideoActivity;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MedleyOrMeest extends AppCompatActivity {
    ActivityMedleyOrMeestBinding binding;
    MeedleyOrMeestViewModel model;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        sessionManager = new SessionManager(MedleyOrMeest.this);
        binding = DataBindingUtil.setContentView(MedleyOrMeest.this, R.layout.activity_medley_or_meest);
        model = new ViewModelProvider(this, new ViewModelFactory(new MeedleyOrMeestViewModel(binding)).createFor()).get(MeedleyOrMeestViewModel.class);
        initObserve();
        binding.setMeedleyOrMeestModel(model);
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
        model.isdefaultApp.observe(this, response -> {
            if (response.getData().getDefaultApp().equalsIgnoreCase("1")) {
                sessionManager.saveBooleanValue(Const.IS_LOGIN, false);
                Intent intent = new Intent(MedleyOrMeest.this, SplashScreenVideoActivity.class);
                intent.putExtra("whereFrom", "register");
                SharedPrefreances.setSharedPreferenceString(MedleyOrMeest.this, SharedPrefreances.WHEREFROM, "Video");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MedleyOrMeest.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SharedPrefreances.setSharedPreferenceString(MedleyOrMeest.this, SharedPrefreances.WHEREFROM, "Social");
                startActivity(intent);
            }


        });
    }

}