package com.meest.videomvvmmodule.view.video;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meest.R;
import com.meest.databinding.ActivityPlayerBinding;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.viewmodel.VideoPlayerViewModel;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PlayerActivity extends AppCompatActivity {

    private VideoPlayerViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStatusBarTransparentFlag();
        ActivityPlayerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        viewModel = new ViewModelProvider(this).get(VideoPlayerViewModel.class);
        initIntent();
        binding.setViewModel(viewModel);
    }

    private void initIntent() {
        if (getIntent().getBooleanExtra("isSingleVideo", false)) {

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(Global.initRetrofit().getPostById(Global.apikey, getIntent().getStringExtra("postId"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
//                    .doOnSubscribe(disposable1 -> loadingPost.set(true))
                    .subscribe((video, throwable) -> {
//                        String videoStr = getIntent().getStringExtra("video_list");
                        if (video.getData().size() == 0) {
                            finish();
                            Toast.makeText(PlayerActivity.this, getResources().getString(R.string.post_not_found), Toast.LENGTH_SHORT).show();
                        } else {
                            viewModel.position = getIntent().getIntExtra("position", 0);
                            viewModel.type = getIntent().getIntExtra("type", 0);
                            viewModel.handleType(getIntent());
                            viewModel.list.add(video.getData().get(0));
                            getSupportFragmentManager().beginTransaction().replace(R.id.frame, new VideoFragment()).commit();
                        }
                    }));

        } else {
            String videoStr = getIntent().getStringExtra("video_list");
            viewModel.position = getIntent().getIntExtra("position", 0);
            viewModel.type = getIntent().getIntExtra("type", 0);
            viewModel.handleType(getIntent());
            if (videoStr != null && !videoStr.isEmpty()) {
                viewModel.list = new Gson().fromJson(videoStr, new TypeToken<ArrayList<Video.Data>>() {
                }.getType());
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new VideoFragment()).commit();
            }
        }
//        viewModel.list = Global.tempList;
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new VideoFragment()).commit();
    }

    @Override
    public void onBackPressed() {
//        Intent resultIntent = new Intent();
//        resultIntent.putExtra("videoDeleted",postId);
//        PlayerActivity.this.setResult(RESULT_OK, resultIntent);
//        PlayerActivity.this.finish();
        finish();
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult();
        }
    }*/

}