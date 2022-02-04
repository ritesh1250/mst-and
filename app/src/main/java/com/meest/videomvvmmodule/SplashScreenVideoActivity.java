package com.meest.videomvvmmodule;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.meest.MainActivity;
import com.meest.R;
import com.meest.videomvvmmodule.model.videos.UploadVideoResponse;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class SplashScreenVideoActivity extends BaseActivity implements MediaPlayer.OnPreparedListener {
    VideoView videoView;
    String whereFrom = "";
    private int length;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLanguage();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_video_screen_activity);
        whereFrom = getIntent().getStringExtra("whereFrom");
        videoView = findViewById(R.id.videoView);
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

        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(mp -> {
//            System.out.println("Meestid: ==============/==========================" + SharedPrefreances.getSharedPreferenceString(SplashScreenVideoActivity.this, SharedPrefreances.ID));
//            Log.e("user_id", "======================================================" + Global.userId);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(Global.initRetrofit().getUniqueKey(Global.password, Global.email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
//                                    .doOnSubscribe(it -> customDialogBuilder.showLoadingDialog())
//                                    .doOnTerminate(() -> customDialogBuilder.hideLoadingDialog())
                    .subscribe((uploadVideoResponse, throwable) -> {
                        if (uploadVideoResponse != null)
                            Global.apikey = uploadVideoResponse.getData();
                    }));
            if (whereFrom.equalsIgnoreCase("Social")||whereFrom.equalsIgnoreCase("social")) {
                Global.accessToken = "";
//                Log.e("firebase_token====", "==============================================" + Global.firebaseDeviceToken);
                if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                    Global.userId = sessionManager.getUser().getData().getUserId();
//                    Log.e("access_token", "======================================================" + Global.accessToken);
                    startActivity(new Intent(SplashScreenVideoActivity.this, MainVideoActivity.class));
                    finish();
                } else {
                    handleSignInResultpart2();
//                        handleSignInResult();
                }
            } else if (whereFrom.equalsIgnoreCase("register")) {
                Global.accessToken = "";
//                Log.e("firebase_token====", "==============================================" + Global.firebaseDeviceToken);
                if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                    Global.userId = sessionManager.getUser().getData().getUserId();
//                    Log.e("access_token", "======================================================" + Global.accessToken);
//                    Log.e("user_id", "======================================================" + Global.userId);
                    startActivity(new Intent(SplashScreenVideoActivity.this, MainVideoActivity.class));
                    finish();
                } else {
                    handleSignInResultpart2();
//                        handleSignInResult();
                }
            }
//            else if (whereFrom.equalsIgnoreCase("Video")||whereFrom.equalsIgnoreCase("video")) {
//                Global.accessToken = "";
//                if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
//                    Global.userId = sessionManager.getUser().getData().getUserId();
//                    startActivity(new Intent(SplashScreenVideoActivity.this, MainVideoActivity.class));
//                    finish();
//                } else {
//                    handleSignInResultpart2();
//                }
//            }
            else if (whereFrom.equalsIgnoreCase("skip")) {
//                Log.d("user_id", "======================================================" + Global.userId);
                if (!sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                    Intent intent = new Intent(SplashScreenVideoActivity.this, MainVideoActivity.class);
                    intent.putExtra("isSkipCase", true);
                    startActivity(intent);
                    finish();
                }
            } else {
                Intent intent = new Intent(SplashScreenVideoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        videoView.start();
    }

//    private void buildAlertMessageNoGps() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(getString(R.string.Your_GPS_seems_to_be_disabled_do_you_want_to_enable_it))
//                .setCancelable(false)
//                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                    }
//                })
//                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        dialog.cancel();
//                    }
//                });
//        final AlertDialog alert = builder.create();
//        alert.show();
//    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.pause();
            length = videoView.getCurrentPosition();
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void onPrepared(MediaPlayer mp) {
        mp.start();
        mp.seekTo(length);
    }


    private void getLanguage() {

        final CompositeDisposable disposable = new CompositeDisposable();

        disposable.add(Global.initRetrofit().getLanguage(Global.apikey, Global.userId, "hindi")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((languageResponseMedley, throwable) -> {
                    if (languageResponseMedley != null) {
                        Global.initLanguage(languageResponseMedley.getData());
                    }
                }));
    }

}