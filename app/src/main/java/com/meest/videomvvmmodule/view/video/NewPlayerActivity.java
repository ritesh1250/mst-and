package com.meest.videomvvmmodule.view.video;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meest.Meeast;
import com.meest.R;
import com.meest.databinding.ActivityNewPlayerBinding;
import com.meest.databinding.ItemVideoListBinding;
import com.meest.videomvvmmodule.adapter.NewVideoPagerAdapter;
import com.meest.videomvvmmodule.adapter.VideoFullAdapter;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.home.CommentSheetFragment;
import com.meest.videomvvmmodule.view.home.OptionsSheetFragment;
import com.meest.videomvvmmodule.view.home.ReportSheetFragment;
import com.meest.videomvvmmodule.view.home.SoundVideosActivity;
import com.meest.videomvvmmodule.view.recordvideo.DuoActivity;
import com.meest.videomvvmmodule.view.recordvideo.Utilss;
import com.meest.videomvvmmodule.view.search.FetchUserActivity;
import com.meest.videomvvmmodule.view.search.HashTagActivity;
import com.meest.videomvvmmodule.view.wallet.WalletActivity;
import com.meest.videomvvmmodule.viewmodel.VideoPlayerViewModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NewPlayerActivity extends FragmentActivity {

    private VideoPlayerViewModel viewModel;

    private ProgressDialog pDialog;
    File directoryName, fileName;
    private static final int Permission_CODE = 1000;
    private ActivityNewPlayerBinding binding;
    private SimpleExoPlayer cachePlayer;
    public NewVideoPagerAdapter adapter;
    //    public static boolean isFABOpen = false;
    private int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_player);
        viewModel = new ViewModelProvider(this).get(VideoPlayerViewModel.class);
        initIntent();
        binding.setViewModel(viewModel);

        initListener();
        initObserver();
//        viewModel.sessionManager = sessionManager;
    }

    private void initObserver() {
        viewModel.onCommentSuccess.observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                binding.etComment.setText("");
            }
        });
        viewModel.onLoadMoreComplete.observe(this, onLoadMore -> {
            binding.refreshlout.finishLoadMore();
            if (onLoadMore != null && onLoadMore) {
                callCache();
            }
        });
        viewModel.coinSend.observe(this, coinSend -> showSendResult(coinSend.getStatus()));
    }

    private void initIntent() {
        if (getIntent().getBooleanExtra("isSingleVideo", false)) {
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(Global.initRetrofit().getPostById(Global.apikey, getIntent().getStringExtra("postId"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe((video, throwable) -> {
                        viewModel.position = getIntent().getIntExtra("position", 0);
                        viewModel.type = getIntent().getIntExtra("type", 0);
                        viewModel.handleType(getIntent());
                        viewModel.list.add(video.getData().get(0));
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new VideoFragment()).commit();
                    }));
        } else {
            String videoStr = getIntent().getStringExtra("video_list");
            viewModel.position = getIntent().getIntExtra("position", 0);
            viewModel.type = getIntent().getIntExtra("type", 0);
            viewModel.handleType(getIntent());
            if (videoStr != null && !videoStr.isEmpty()) {
                viewModel.list = new Gson().fromJson(videoStr, new TypeToken<ArrayList<Video.Data>>() {
                }.getType());
//                Video.Data data = viewModel.list.get(0);
//
//                for (int i = 0; i <= viewModel.position; i++) {
//                    viewModel.list.add(data);
//                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame, new VideoFragment()).commit();
            }
        }
    }

    private void showSendResult(boolean success) {
        if (success) {
            new CustomDialogBuilder(NewPlayerActivity.this).showSendDiamondConfirm(success, viewModel.sendDiamond);
            return;
        }
        new CustomDialogBuilder(NewPlayerActivity.this).showSendCoinResultDialogue(success, success1 -> {
            if (!success1) {
//                CoinPurchaseSheetFragment fragment = new CoinPurchaseSheetFragment();
//                fragment.show(getChildFragmentManager(), fragment.getClass().getSimpleName());
                Intent intent = new Intent(NewPlayerActivity.this, WalletActivity.class);
                intent.putExtra("openCoin", true);
                startActivity(intent);
            }
        });
    }

    private void callCache() {
//        Log.d(TAG, "callCache: " + i);
        if (cachePlayer != null) {
            cachePlayer.release();
        }
        if (i < viewModel.adapter.getmList().size() && viewModel.adapter.getmList().get(i) != null) {

            if (cachePlayer != null)
                cachePlayer.release();

            if (this != null) {
                HttpProxyCacheServer proxy = Meeast.getProxy(this);
                String proxyUrl = proxy.getProxyUrl((viewModel.adapter.getmList().get(i).getPostVideo()));
//                String proxyUrl = proxy.getProxyUrl((Const.ITEM_BASE_URL + viewModel.adapter.getmList().get(i).getPostVideo()));

                LoadControl loadControl = new DefaultLoadControl.Builder()
                        .setAllocator(new DefaultAllocator(true, 16))
                        .setBufferDurationsMs(1024, 1024, 500, 1024)
                        .setTargetBufferBytes(-1)
                        .setPrioritizeTimeOverSizeThresholds(true)
                        .createDefaultLoadControl();
                DefaultTrackSelector trackSelector = new DefaultTrackSelector();
                cachePlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                        Util.getUserAgent(this, getResources().getString(R.string.app_name)));
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(proxyUrl));
                cachePlayer.prepare(videoSource);
                cachePlayer.addListener(new Player.EventListener() {
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                        Log.d(TAG, "onPlayerStateChanged: " + playbackState);
                        if (playbackState == Player.STATE_READY) {
                            i++;
                            callCache();
                        }
                    }
                });
            }
        }
    }

    private void initListener() {
        adapter = new NewVideoPagerAdapter(NewPlayerActivity.this, () -> viewModel.onLoadMore(), new VideoFullAdapter.OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(Video.Data model, int type, ItemVideoListBinding binding, int position, SimpleExoPlayer player) {
                switch (type) {
                    // Send to FetchUser Activity
                    case 1:
                        Intent intent = new Intent(NewPlayerActivity.this, FetchUserActivity.class);
                        intent.putExtra("userid", model.getUserId());
                        startActivity(intent);
                        break;
                    // Play/Pause video
                    case 2:
                        if (cachePlayer != null) {
                            if (cachePlayer.isPlaying()) {
                                cachePlayer.setPlayWhenReady(false);
                            } else {
                                cachePlayer.setPlayWhenReady(true);
                            }
                        }
                        break;
                    // Send Bubble to creator
                    case 3:
                       /* if (!Global.accessToken.isEmpty()) {
                            showSendBubblePopUp(model.getUserId());
                        } else {
                            if (this instanceof PlayerActivity) {
                                ((PlayerActivity) this).initLogin(this, () -> showSendBubblePopUp(model.getUserId()));
                            }
                        }*/
                        if (new SessionManager(NewPlayerActivity.this).getBooleanValue(Const.IS_LOGIN)) {
                            showSendBubblePopUp(model.getUserId());
                        } else {
                            Utilss.callLoginSign(NewPlayerActivity.this);
                        }
                        break;
                    // On like btn click
                    case 4:
                        viewModel.likeUnlikePost(model.getPostId(), NewPlayerActivity.this, model, binding, position);
                        break;
                    // On Comment Click
                    case 5:
                        CommentSheetFragment fragment = new CommentSheetFragment();
                        fragment.setOnDismissListener(count -> {
                            model.setPostCommentsCount(count);
                            Objects.requireNonNull(viewModel.list.set(position, model));
                            binding.tvCommentCount.setText(Global.prettyCount(count));
                        });
                        Bundle args = new Bundle();
                        args.putString("postid", model.getPostId());
                        args.putInt("commentCount", model.getPostCommentsCount());
                        fragment.setArguments(args);
                        fragment.show(getSupportFragmentManager(), fragment.getClass().getSimpleName());
//                        isFABOpen = false;
//                        viewModel.closeFABMenu(binding);
                        break;
                    // On Share Click
                    case 6:
                        handleShareClick(model);
                        viewModel.sharePost(model.getPostId(), model, binding, position);
                        break;
                    // On Sound Disk Click
                    case 7:
                        Intent intent1 = new Intent(NewPlayerActivity.this, SoundVideosActivity.class);
                        intent1.putExtra("soundid", model.getSoundId());
                        intent1.putExtra("sound", model.getSound());
                        startActivity(intent1);
                        break;
                    // On Long Click (Report Video)
                    case 8:
                        new CustomDialogBuilder(NewPlayerActivity.this).showSimpleDialog(getString(R.string.Report_this_post), getString(R.string.Are_you_sure_you_want_to_report_this_post), getString(R.string.cancel), getString(R.string.Yes_Report), new CustomDialogBuilder.OnDismissListener() {
                            @Override
                            public void onPositiveDismiss() {
                                if (player != null) {
                                    player.setPlayWhenReady(false);
                                    if (player.getPlayWhenReady()) {
                                        binding.ivPause.setVisibility(View.INVISIBLE);
                                    } else {
                                        binding.ivPause.setVisibility(View.VISIBLE);
                                    }
                                }
                                reportPost(model);
                            }

                            @Override
                            public void onNegativeDismiss() {
                                if (player != null) {
                                    player.setPlayWhenReady(true);
                                    if (player.getPlayWhenReady()) {
                                        binding.ivPause.setVisibility(View.INVISIBLE);
                                    } else {
                                        binding.ivPause.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
                        break;

                    case 9:
                        //follow unfollow button
                        viewModel.followUnfollow(model, NewPlayerActivity.this, binding, adapter);
                        break;
                    case 11:
                 /*       if (!isFABOpen) {
                            isFABOpen = true;
                            viewModel.showFABMenu(binding);
                        } else {
                            isFABOpen = false;
                            viewModel.closeFABMenu(binding);
                        }
*/
                        OptionsSheetFragment optionsSheetFragment = new OptionsSheetFragment(binding.ivPause, player, model, NewPlayerActivity.this, false);
//                        OptionsSheetFragment optionsSheetFragment = new OptionsSheetFragment();
                        Bundle args1 = new Bundle();
//                        Log.e(TAG, "onItemClick: " + model.toString());
                        args1.putString("postid", model.getPostId());
                        args1.putInt("reporttype", 1);
                        args1.putString("userid", model.getUserId());
                        args1.putString("videoPath", model.getPostVideo());
                        args1.putString("audioPath", model.getSound());
                        args1.putString("soundId", model.getSoundId());
                        args1.putInt("canDuet", model.getCanDuet());
                        args1.putInt("canSave", model.getCanSave());
                        args1.putString("videoThumbnail", model.getThumbnail_image());
                        optionsSheetFragment.setArguments(args1);
                        optionsSheetFragment.show(getSupportFragmentManager(), optionsSheetFragment.getClass().getSimpleName());
                        break;
                    case 12:
                    /*    isFABOpen = false;
                        /*isFABOpen = false;
                        viewModel.closeFABMenu(binding);*/
                        viewModel.videoPath = model.getPostVideo();
                        viewModel.videoModel = model;
                        if (new SessionManager(NewPlayerActivity.this).getBooleanValue(Const.IS_LOGIN)) {
//                            rescaleURLVideo(model.getPostVideo());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (NewPlayerActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                    requestPermissions(permissions, Permission_CODE);
                                } else {
                                    new DownloadFileFromURL().execute(viewModel.videoPath);
//                                    downloadVideo(model.getPostVideo(), viewModel.videoModel);
                                }
                            } else {
                                new DownloadFileFromURL().execute(viewModel.videoPath);
//                                downloadVideo(model.getPostVideo(), viewModel.videoModel);
                            }

                        } else {
                            Utilss.callLoginSign(NewPlayerActivity.this);
                        }
                        break;

                    case 13:
                        viewModel.addView(model, binding);
                        break;
                }

            }

            @Override
            public void onHashTagClick(String hashTag) {
                Intent intent = new Intent(NewPlayerActivity.this, HashTagActivity.class);
                intent.putExtra("hashtag", hashTag);
                startActivity(intent);
            }

            @Override
            public void onDoubleClick(Video.Data model, MotionEvent event, ItemVideoListBinding binding) {
                if (!model.getVideoIsLiked()) {
                    binding.likebtn.performClick();
                }
                showHeart(event, binding);
            }
        });
        binding.imgBack.setOnClickListener(v ->
        {
            if (this != null) {
//                if (viewModel.type == 5) {
//                    startActivity(new Intent(this, MainVideoActivity.class));
//                }
                this.finish();
            }
        });

        binding.refreshlout.setEnableRefresh(false);
        binding.refreshlout.setOnLoadMoreListener(refreshLayout -> viewModel.onLoadMore());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(1);
        if (!viewModel.list.isEmpty()) {
            adapter.updateData(viewModel.list);
            binding.viewPager.setOffscreenPageLimit(viewModel.list.size());
            binding.viewPager.setCurrentItem(viewModel.position, false);
        }
    }

    private void handleShareClick(Video.Data model) {
        ProgressDialog dialog = ProgressDialog.show(NewPlayerActivity.this, "", "Please wait...", true);
        dialog.setCancelable(false);
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.meest4bharat.com/?video=" + model.getPostId()))
                .setDomainUriPrefix(Const.deepLinkingUriPrefix)
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder(Const.iosBundleId).build())
                .buildShortDynamicLink()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Tag", "====================" + e.getMessage());
                    }
                })
                .addOnCompleteListener(task -> {
                    dialog.dismiss();
                    if (task.isSuccessful()) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        String shareBody = getString(R.string.Watch_more_of_such) + task.getResult().getShortLink();
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Share_Video));
                        share.putExtra(Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(share, getString(R.string.Share_Video)));
                    } else {
                        Toast.makeText(NewPlayerActivity.this, getString(R.string.Error_Creating_Link), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void reportPost(Video.Data model) {
        ReportSheetFragment fragment = new ReportSheetFragment();
        Bundle args = new Bundle();
        args.putString("postid", model.getPostId());
        args.putInt("reporttype", 1);
        args.putString("userid", Global.userId);
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), fragment.getClass().getSimpleName());
    }

    public void showHeart(MotionEvent e, ItemVideoListBinding binding) {

        int x = (int) e.getX() - 200;
        int y = (int) e.getY() - 200;
        Log.i(TAG, "showHeart: " + x + "------" + y);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        final ImageView iv = new ImageView(this);
        lp.setMargins(x, y, 0, 0);
        iv.setLayoutParams(lp);
        Random r = new Random();
        int i1 = r.nextInt(30 + 30) - 30;
        iv.setRotation(i1);
        iv.setImageResource(R.drawable.double_tap);
        if (binding.rtl.getChildCount() > 0) {
            binding.rtl.removeAllViews();
        }
        binding.rtl.addView(iv);
        Animation fadeoutani = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        fadeoutani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.rtl.removeView(iv);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv.startAnimation(fadeoutani);

    }

    private void showSendBubblePopUp(String userId) {

        new CustomDialogBuilder(this).showSendCoinDialogue(new CustomDialogBuilder.OnCoinDismissListener() {
            @Override
            public void onCancelDismiss() {
            }

            @Override
            public void on20Dismiss() {
                viewModel.sendBubble(userId, "20");
            }

            @Override
            public void on40Dismiss() {
                viewModel.sendBubble(userId, "40");
            }

            @Override
            public void on80Dismiss() {
                viewModel.sendBubble(userId, "80");
            }

            @Override
            public void on100Dismiss() {
                viewModel.sendBubble(userId, "100");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permission_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new DownloadFileFromURL().execute(viewModel.videoPath);
//                    downloadVideo(viewModel.videoPath, viewModel.videoModel);
            } else {
                Toast.makeText(NewPlayerActivity.this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    ===================================duet video================================
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println(getResources().getString(R.string.Downloading));
            pDialog = new ProgressDialog(NewPlayerActivity.this);
            pDialog.setMessage(getResources().getString(R.string.please_wait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {

            int count;

            try {
                String root = Environment.getExternalStorageDirectory().toString();
                directoryName = new File(Environment.getExternalStorageDirectory(), "Medley_Video");
                if (!directoryName.exists()) {
                    directoryName.mkdirs();
                }
                fileName = new File(directoryName, "Medley_" + viewModel.videoModel.getUserName() + "_" + viewModel.videoModel.getPostId() + ".mp4");
                System.out.println("Downloading");
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file

                OutputStream output = new FileOutputStream(fileName);
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    output.write(data, 0, count);

                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        /**
         * After completing background task
         **/
        @Override
        protected void onPostExecute(String file_url) {
            System.out.println("Downloaded");
            Uri uri = Uri.fromFile(fileName);
            Log.e("uri===", "=========" + uri);
            Intent intent = new Intent(NewPlayerActivity.this, DuoActivity.class);
            intent.putExtra("videoPath", uri.toString());
            startActivity(intent);
            pDialog.dismiss();
        }

    }


//    @Override
//    public void onBackPressed() {
//        finish();
//    }
}