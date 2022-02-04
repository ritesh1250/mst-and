package com.meest.social.socialViewModel.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.meest.R;
import com.meest.social.socialViewModel.viewModel.splashViewModel.SplashViewModel;
import com.meest.databinding.SplashScreenActivityModelBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.util.HashMap;

import static com.meest.social.socialViewModel.utils.ParameterConstant.SocialPrefrences.POST;
import static com.meest.social.socialViewModel.utils.ParameterConstant.SocialPrefrences.USER;
import static com.meest.social.socialViewModel.utils.ParameterConstant.SocialPrefrences.USER_ID;
import static com.meest.social.socialViewModel.utils.ParameterConstant.SocialPrefrences.VIDEO;

public class SplashScreen extends AppCompatActivity {
    SplashScreenActivityModelBinding binding;
    SplashViewModel splashViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.splash_screen_activity_model);
        splashViewModel = ViewModelProviders.of(this, new ViewModelFactory(new SplashViewModel(this, this, binding)).createFor()).get(SplashViewModel.class);
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        onNewIntent(getIntent());
        initView();
        binding.setSplashModel(splashViewModel);

    }

    private void initView() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        splashViewModel.localSaveValue();
        getDynamikLink();
        if (getIntent().getExtras() != null && SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN).length() > 0) {
            HashMap<String, String> data = new HashMap<>();
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                data.put(key, value);
                Log.d("harsh", "Key: " + key + " Value: " + value);
            }
            if (data.size() > 2) {
                splashViewModel.handleNotification(data);
            }
        }
        splashViewModel.setVideoView(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hello));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getStringExtra(Const.userId) != null && !getIntent().getStringExtra(Const.userId).isEmpty()) {
            SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.GET_NOTIFICATION_INTENT_USER, getIntent().getStringExtra(Const.userId));
            setIntent(new Intent());
        }
        if (getIntent().getStringExtra(Const.videoId) != null && !getIntent().getStringExtra(Const.videoId).isEmpty()) {
            SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.GET_NOTIFICATION_INTENT_VIDEO, getIntent().getStringExtra(Const.videoId));
            setIntent(new Intent());
        }
    }

    private void getDynamikLink() {
        SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.GETINTENT_USER, "");
        SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.GETINTENT_VIDEO, "");
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        Log.d("TAG", "==========onSuccess: " + deepLink);
                        Log.d("TAG", "==========onSuccess: " + deepLink.getQueryParameter(USER));

                        Log.d("TAG", "==========onSuccessVideo: " + deepLink.getQueryParameter(VIDEO));
                        Log.d("TAG", "==========onSuccessPost: " + deepLink.getQueryParameter(POST));
                        Log.d("TAG", "==========onSuccessUserid: " + deepLink.getQueryParameter(USER_ID));
                        if (deepLink.getQueryParameter("post") != null && deepLink.getQueryParameter(USER_ID) != null) {
                            Intent intent = new Intent(SplashScreen.this, NotificationSocialFeedActivity.class);
                            intent.putExtra("userId", deepLink.getQueryParameter(USER_ID));
                            intent.putExtra("postId", deepLink.getQueryParameter(POST));
                            startActivity(intent);
                        }
                        SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.GETINTENT_USER, deepLink.getQueryParameter(USER));
                        SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.GETINTENT_VIDEO, deepLink.getQueryParameter(VIDEO));
                    }
                })
                .addOnFailureListener(this, e -> Log.w("TAG", "getDynamicLink:onFailure", e))
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        setIntent(new Intent());
                    }
                });
    }

}
