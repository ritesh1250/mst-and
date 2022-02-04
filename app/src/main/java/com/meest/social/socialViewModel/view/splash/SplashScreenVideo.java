package com.meest.social.socialViewModel.view.splash;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.meest.MainActivity;
import com.meest.R;
import com.meest.databinding.SplashVideoScreenActivityBinding;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.social.socialViewModel.utils.Utils;
import com.meest.social.socialViewModel.view.home.main.HomeActivity;
import com.meest.social.socialViewModel.viewModel.splashViewModel.SplashScreenVideoModel;
import com.meest.videomvvmmodule.model.videos.UploadVideoResponse;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SplashScreenVideo extends BaseActivity implements MediaPlayer.OnPreparedListener {
    public SplashVideoScreenActivityBinding binding;
    public SplashScreenVideoModel model;
    private int length;
    String whereFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getWindow(this);
        binding = DataBindingUtil.setContentView(this, R.layout.splash_video_screen_activity);
        model = ViewModelProviders.of(this, new ViewModelFactory(new SplashScreenVideoModel(this, this, binding)).createFor()).get(SplashScreenVideoModel.class);
        model.getLanguage();
        inItView();
        binding.setSplashVideoModel(model);

    }

    private void inItView() {
        whereFrom = getIntent().getStringExtra("whereFrom");
        Uri video;
        if (whereFrom.equalsIgnoreCase("social")) {
            video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.filp_video);
        } else if (whereFrom.equalsIgnoreCase("register")) {
            video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.filp_video);
        } else if (whereFrom.equalsIgnoreCase("skip")) {
            video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.filp_video);
        } else {
            video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.flip_reverse);
        }

        binding.videoView.setVideoURI(video);
        binding.videoView.setOnPreparedListener(this);
        binding.videoView.setOnCompletionListener(mp -> {
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(Global.initRetrofit().getUniqueKey(Global.password, Global.email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe((UploadVideoResponse uploadVideoResponse, Throwable throwable) -> Global.apikey = uploadVideoResponse.getData()));
            if (whereFrom.equalsIgnoreCase("social")) {
                Global.accessToken = "";
                if (SocialPrefrences.getisLogin(this)) {
                    Global.userId = sessionManager.getUser().getData().getUserId();
                    startActivity(new Intent(SplashScreenVideo.this, MainVideoActivity.class));
                    finish();
                } else {
                    handleSignInResultpart2();
                }
            } else if (whereFrom.equalsIgnoreCase("register")) {

                Global.accessToken = "";
                if (SocialPrefrences.getisLogin(this)) {
                    Global.userId = sessionManager.getUser().getData().getUserId();
                    startActivity(new Intent(SplashScreenVideo.this, MainVideoActivity.class));
                    finish();
                } else {
                    handleSignInResultpart2();

                }
            } else if (whereFrom.equalsIgnoreCase("skip")) {

                if (!SocialPrefrences.getisLogin(this)) {
                    Intent intent = new Intent(SplashScreenVideo.this, MainVideoActivity.class);
                    intent.putExtra("isSkipCase", true);
                    startActivity(intent);
                    finish();
                }
            } else {
                Intent intent = new Intent(SplashScreenVideo.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        binding.videoView.start();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        mp.seekTo(length);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (binding.videoView.isPlaying()) {
            binding.videoView.pause();
            length = binding.videoView.getCurrentPosition();
        }
    }
}
