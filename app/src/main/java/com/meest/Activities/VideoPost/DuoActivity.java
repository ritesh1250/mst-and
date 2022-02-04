package com.meest.Activities.VideoPost;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.daasuu.gpuv.camerarecorder.CameraRecordListener;
import com.daasuu.gpuv.camerarecorder.GPUCameraRecorderBuilder;
import com.daasuu.gpuv.camerarecorder.LensFacing;
import com.meest.R;
import com.meest.Widget.SampleCameraGLView;
import com.meest.meestbhart.utilss.Constant;
import com.meest.videoEditing.UtilCommand;
import com.meest.videoEditing.videocutter.VideoCutter;
import com.xw.repo.BubbleSeekBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DuoActivity extends AppCompatActivity /*implements Player.EventListener*/ {
    SampleCameraGLView sampleGLView;
    String videoFile, thumbnail;
    FrameLayout cameraView;
    ImageView videoView_thumbnail;
    ProgressBar progressbar;
    RelativeLayout viewProgressbar;
    CircularProgressBar circularProgressBar;
    ImageView ivVideoDone, ivRecord, ivCancelVideo;
    BubbleSeekBar bubbleSeekBar;
    RelativeLayout viewTimerContent;
    LinearLayout viewFlash, viewFlipCamera;
    VideoView videoView;
    CountDownTimer countDownTimer;
    com.daasuu.gpuv.camerarecorder.GPUCameraRecorder gpuCameraRecorder;
    LensFacing lensFacing = LensFacing.BACK;
    boolean toggleClick = false, isTimerSet, isPlaying = false;
    int SELECTED_TIME_PROGRESS;
    int CURRENT_VIDEO_PROGRESS, LAST_VIDEO_PROGRESS;
    List<String> filepathListTemp = new ArrayList<>();
    List<Integer> videoDurationList = new ArrayList<>();
    int pathCounter = 0, endTime = 250;
    double VIDEO_TIME_SECONDS = 30;
    MediaPlayer mediaPlayer;
    private String n;
//    FFmpeg ffmpeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duo_activity);

        findIds();

        if (getIntent().getExtras() != null) {
            videoFile = getIntent().getExtras().getString("videoFile");
            thumbnail = getIntent().getExtras().getString("videoFile");

            /// loading thumbnail
            Glide.with(this).load(thumbnail).into(videoView_thumbnail);
        }

        // setting media controller
        Uri video = Uri.parse(videoFile);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;
            }
        });

//        setupExoPlayer();
//        ffmpeg = com.github.hiteshsondhi88.libffmpeg.FFmpeg.getInstance(this);
//        versionFFmpeg();
      /*  if (ffmpeg.getInstance(this).isSupported()) {
            versionFFmpeg();
        }*/

        File videoPath = new File(getAppVideoFolder().getAbsolutePath());
        if (!videoPath.exists()) {
            try {
                videoPath.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File videoTempPath = new File(getAppVideoTempFolder().getAbsolutePath());
        if (!videoTempPath.exists()) {
            try {
                videoTempPath.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            videoTempPath.isDirectory();
            /*String[] children = videoTempPath.list();
                for (int i = 0; i < children.length; i++) {
                    new File(videoTempPath, children[i]).delete();
                }*/
            try {
                videoTempPath.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int seekBarMaxTime = (int) (VIDEO_TIME_SECONDS - CURRENT_VIDEO_PROGRESS);
        bubbleSeekBar.getConfigBuilder()
                .min(0)
                .max(seekBarMaxTime)
                .progress(seekBarMaxTime).build();

        ivCancelVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openExitConfirmDialog();
            }
        });

        viewFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gpuCameraRecorder != null && gpuCameraRecorder.isFlashSupport()) {
                    gpuCameraRecorder.switchFlashMode();
                    gpuCameraRecorder.changeAutoFocus();
                }
            }
        });

        viewFlipCamera.setOnClickListener(v -> {
            releaseCamera();
            if (lensFacing == LensFacing.BACK) {
                viewFlash.setVisibility(View.GONE);
                lensFacing = LensFacing.FRONT;
            } else {
                viewFlash.setVisibility(View.VISIBLE);
                lensFacing = LensFacing.BACK;
            }
            toggleClick = true;
        });

        ivRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((String) ivRecord.getTag()) == null) {
                    filepathListTemp.add(getVideoFilePath("vid_temp_" + pathCounter));
                    gpuCameraRecorder.start(getVideoFilePath("vid_temp_" + pathCounter));
                    // playing from start
                    videoView.start();
                    // hiding thumbnail
                    videoView_thumbnail.setVisibility(View.GONE);
                    ivRecord.setTag("Stop");
                    ivRecord.setImageResource(R.drawable.ic_camera_pause);
                    hideVideoPlayViews();
                    startTimeCounting();
                    //ivVideoDone.setVisibility(View.VISIBLE);
                    pathCounter++;
                    LAST_VIDEO_PROGRESS = 0;
                } else if (((String) ivRecord.getTag()).equals(getString(R.string.app_record))) {
                    filepathListTemp.add(getVideoFilePath("vid_temp_" + pathCounter));
                    gpuCameraRecorder.start(getVideoFilePath("vid_temp_" + pathCounter));
                    resumeVideo();
                    ivRecord.setTag("Stop");
                    ivRecord.setImageResource(R.drawable.ic_camera_pause);
                    hideVideoPlayViews();
                    startTimeCounting();
                    pathCounter++;
                } else {
                    stopTimeCounting();
                    Log.w("ravi_testing", "1");
                    gpuCameraRecorder.stop();
                    pauseVideo();
                    ivRecord.setTag(getString(R.string.app_record));
                    showVideoPlayViews();
                    ivVideoDone.setVisibility(View.VISIBLE);
                    videoDurationList.add(LAST_VIDEO_PROGRESS);
                    ivRecord.setImageResource(R.drawable.ic_capture_round_white);
                    isTimerSet = false;
                    SELECTED_TIME_PROGRESS = 0;
                }
            }
        });

        ivVideoDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filepathListTemp.size() > 1) {
                    showPrd();
                    concatenate(getAppVideoFolder() + "/VID_" + System.currentTimeMillis() + ".mp4");
                } else {
                    String outputFile = getAppVideoFolder() + "/VID_" + System.currentTimeMillis() + ".mp4";
                    copyFileOrDirectory(filepathListTemp.get(0), outputFile);
                    hidePrd();
                    Intent intent = new Intent(DuoActivity.this, PostVideoActivity.class);
                    intent.putExtra("video_path", outputFile);
                    Log.v("harsh", "1 outputFile == " + outputFile);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void pauseVideo() {
        if(mediaPlayer != null){
            isPlaying = mediaPlayer.isPlaying();
            mediaPlayer.pause();
        }
    }

    private void resumeVideo() {
        if(mediaPlayer != null && isPlaying) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gpuCameraRecorder != null) {
            gpuCameraRecorder.stop();
            gpuCameraRecorder.release();
            gpuCameraRecorder = null;
        }
    }

    @Override
    public void onBackPressed() {
        openExitConfirmDialog();
    }

    private void hidePrd() {
        if (circularProgressBar != null && viewProgressbar != null) {
            viewProgressbar.setVisibility(View.GONE);
            ((CircularProgressDrawable) circularProgressBar.getIndeterminateDrawable()).progressiveStop();
        }
    }

    private void showPrd() {
        if (circularProgressBar != null && viewProgressbar != null) {
            viewProgressbar.setVisibility(View.VISIBLE);
            ((CircularProgressDrawable) circularProgressBar.getIndeterminateDrawable()).start();
        }
    }

    private void findIds() {
        viewProgressbar = findViewById(R.id.viewProgressbar);
        progressbar = findViewById(R.id.progressbar);
        viewFlipCamera = findViewById(R.id.viewFlipCamera);
        viewFlash = findViewById(R.id.viewFlash);
        ivCancelVideo = findViewById(R.id.ivCancelVideo);
        viewTimerContent = findViewById(R.id.viewTimerContent);
        cameraView = findViewById(R.id.cameraView);
        circularProgressBar = findViewById(R.id.cpb);
        ivRecord = findViewById(R.id.ivRecord);
        bubbleSeekBar = findViewById(R.id.timerSeekbar);
        videoView_thumbnail = findViewById(R.id.videoView_thumbnail);
//        tvStartShooting = findViewById(R.id.tvStartShooting);
        ivVideoDone = findViewById(R.id.ivVideoDone);
        videoView = findViewById(R.id.videoView);
//        playerView = findViewById(R.id.videoView);
    }

    private void setUpCameraView() {
        runOnUiThread(() -> {
            cameraView.removeAllViews();
            sampleGLView = null;
            sampleGLView = new SampleCameraGLView(getApplicationContext());
            sampleGLView.setTouchListener((event, width, height) -> {
                if (gpuCameraRecorder == null) return;
                gpuCameraRecorder.changeManualFocusPoint(event.getX(), event.getY(), width, height);
            });
            cameraView.addView(sampleGLView);
        });
    }

    private void setUpCamera() {
        setUpCameraView();

        gpuCameraRecorder = new GPUCameraRecorderBuilder(this, sampleGLView)
                .cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport(boolean flashSupport) {
                        runOnUiThread(() -> viewFlash.setEnabled(flashSupport));
                    }

                    @Override
                    public void onRecordComplete() {

                    }

                    @Override
                    public void onRecordStart() {
                        runOnUiThread(() -> {
                        });
                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e("GPUCameraRecorder", exception.toString());
                    }

                    @Override
                    public void onCameraThreadFinish() {
                        if (toggleClick) {
                            runOnUiThread(() -> setUpCamera());
                        }
                        toggleClick = false;
                    }
                })
                .lensFacing(lensFacing)
                .build();
    }

    private void releaseCamera() {
        if (sampleGLView != null) {
            sampleGLView.onPause();
        }

        if (gpuCameraRecorder != null) {
            gpuCameraRecorder.stop();
            gpuCameraRecorder.release();
            gpuCameraRecorder = null;
        }

        if (sampleGLView != null) {
            cameraView.removeView(sampleGLView);
            sampleGLView = null;
        }
    }

    private void hideVideoPlayViews() {
        ivCancelVideo.setVisibility(View.GONE);
        viewFlipCamera.setVisibility(View.GONE);
        viewFlash.setVisibility(View.GONE);
        ivVideoDone.setVisibility(View.GONE);
    }

    private void showVideoPlayViews() {
        ivCancelVideo.setVisibility(View.VISIBLE);
        viewFlipCamera.setVisibility(View.VISIBLE);
        viewFlash.setVisibility(View.VISIBLE);
        ivVideoDone.setVisibility(View.VISIBLE);
    }

    private void startTimeCounting() {
        if (countDownTimer == null) {
            CURRENT_VIDEO_PROGRESS = 0;
            endTime = (int) VIDEO_TIME_SECONDS; // up to finish time
            countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    setProgress(CURRENT_VIDEO_PROGRESS, endTime);
                    CURRENT_VIDEO_PROGRESS = CURRENT_VIDEO_PROGRESS + 1;
                    LAST_VIDEO_PROGRESS = LAST_VIDEO_PROGRESS + 1;

                    if (isTimerSet && CURRENT_VIDEO_PROGRESS == SELECTED_TIME_PROGRESS) {
                        stopTimeCounting();
                        Log.w("ravi_testing", "2");
                        gpuCameraRecorder.stop();
                        videoView.pause();

                        ivRecord.setTag(getString(R.string.app_record));
                        showVideoPlayViews();
                        ivRecord.setImageResource(R.drawable.ic_capture_round_white);
                        isTimerSet = false;
                        SELECTED_TIME_PROGRESS = 0;
                        videoDurationList.add(LAST_VIDEO_PROGRESS);

                    }
                    if (CURRENT_VIDEO_PROGRESS >= VIDEO_TIME_SECONDS) {
                        countDownTimer.onFinish();
                    }
                }

                @Override
                public void onFinish() {
                    setProgress(CURRENT_VIDEO_PROGRESS, endTime);
                    ivRecord.setVisibility(View.GONE);
                    stopTimeCounting();
                    Log.w("ravi_testing", "3");
                    if (gpuCameraRecorder != null) {
                        gpuCameraRecorder.stop();
                        videoView.pause();
                    }
                    ivRecord.setTag(getString(R.string.app_record));
                    showVideoPlayViews();
                    ivRecord.setImageResource(R.drawable.ic_capture_round_white);
                    videoDurationList.add(LAST_VIDEO_PROGRESS);
                }
            };
            countDownTimer.start();
        } else {
            countDownTimer.start();
        }
    }

    private void stopTimeCounting() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            int SEEKBAR_TIME_MAX_SECONDS = (int) (VIDEO_TIME_SECONDS - CURRENT_VIDEO_PROGRESS);
            bubbleSeekBar.getConfigBuilder()
                    .min(0)
                    .max(SEEKBAR_TIME_MAX_SECONDS)
                    .progress(SEEKBAR_TIME_MAX_SECONDS).build();
        }
        Log.w("finished", "done");
    }

    public void setProgress(int startTime, int endTime) {
        progressbar.setMax(endTime);
        progressbar.setProgress(startTime);
    }

    public void concatenate(String outputFile) {
        n=outputFile;
        List<String> cmdList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filepathListTemp.size(); i++) {
            cmdList.add("-i");
            cmdList.add(filepathListTemp.get(i));
            sb.append("[").append(i).append(":0] [").append(i).append(":1]");
        }
        sb.append(" concat=n=").append(filepathListTemp.size()).append(":v=1:a=1 [v] [a]");
        cmdList.add("-filter_complex");
        cmdList.add(sb.toString());
        cmdList.add("-map");
        cmdList.add("[v]");
        cmdList.add("-map");
        cmdList.add("[a]");
        cmdList.add("-preset");
        cmdList.add("ultrafast");
        cmdList.add(outputFile);

        sb = new StringBuilder();
        for (String str : cmdList) {
            sb.append(str).append(" ");
        }

        String[] cmd = cmdList.toArray(new String[cmdList.size()]);

     execFFmpegBinaryForConcat(cmd, outputFile);
    }

    private void execFFmpegBinaryForConcat(String[] strArr, final String str) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.Please_Wait));
        progressDialog.show();
        String ffmpegCommand = UtilCommand.main(strArr);
        FFmpeg.executeAsync(ffmpegCommand, new ExecuteCallback() {

            @Override
            public void apply(final long executionId, final int returnCode) {
                Log.d("TAG", String.format("FFmpeg process exited with rc %d.", returnCode));

                Log.d("TAG", "FFmpeg process output:");

                Config.printLastCommandOutput(Log.INFO);

                progressDialog.dismiss();
                if (returnCode == RETURN_CODE_SUCCESS) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(DuoActivity.this, PostVideoActivity.class);
                    intent.putExtra("video_path", n);
                    startActivity(intent);
                    Log.v("harsh", "2 outputFile == " + n);
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.d("ffmpegfailure", str);
                    try {
                        new File(str).delete();
                        Toast.makeText(DuoActivity.this, getString(R.string.Error_Creating_Video), Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    try {
                        new File(str).delete();
                        Toast.makeText(DuoActivity.this, getString(R.string.Error_Creating_Video), Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", returnCode));
                }


            }
        });

        getWindow().clearFlags(16);
    }



//    private void execFFmpegBinaryForConcat(final String[] command, final String outputFile) {
//        try {
//            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
//                @Override
//                public void onFailure(String s) {
//                    //Utils.showLog("=== ffmpeg onFailure" + s);
//                    hidePrd();
//                }
//
//                @Override
//                public void onSuccess(String s) {
//                    //Utils.showLog("=== ffmpeg onSuccess" + s);
//                    for (int i = 0; i < filepathListTemp.size(); i++) {
//                        File file = new File(filepathListTemp.get(i));
//                        if (file.exists()) {
//                            file.delete();
//                            Log.w("file_mode", "2");
//                        }
//                    }
//                }
//
//                @Override
//                public void onProgress(String s) {
////                    Utils.showLog("=== ffmpeg onProgress" + s);
//                }
//
//                @Override
//                public void onStart() {
//                    //Utils.showLog("=== ffmpeg onStart");
//                }
//
//                @Override
//                public void onFinish() {
//                    hidePrd();
//                    Intent intent = new Intent(DuoActivity.this, PostVideoActivity.class);
//                    intent.putExtra("video_path", outputFile);
//                    startActivity(intent);
//                    Log.v("harsh", "2 outputFile == " + outputFile);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void versionFFmpeg() {
//        try {
//            FFmpeg.getInstance(this).execute(new String[]{"-version"}, new ExecuteBinaryResponseHandler() {
//                @Override
//                public void onSuccess(String message) {
//                }
//
//                @Override
//                public void onProgress(String message) {
//                }
//            });
//        } catch (FFmpegCommandAlreadyRunningException e) {
//            e.printStackTrace();
//        }
//    }

    public void copyFileOrDirectory(String srcDir, String dstDir) {
        try {
            File src = new File(srcDir);
            File dst = new File(dstDir);

            if (src.isDirectory()) {
                String[] files = src.list();
                for (String file : files) {
                    String src1 = (new File(src, file).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);
                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("file", e);
        }
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        Log.v("harsh", "source exists == " + sourceFile.exists());

        if (!destFile.exists()) {
            try {
                destFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                sourceFile.delete();
                Log.w("file_mode","6");
                source.close();
            }
            if (destination != null) {
                destination.close();
            }

            Log.v("harsh", "destination exists == " + destFile.exists());
        }
    }

    private void openExitConfirmDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Tips");
        alertDialog.setMessage(getString(R.string.discard));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.Exit), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (getAppVideoTempFolder().exists()) {
                    getAppVideoTempFolder().delete();
                }
                videoDurationList.clear();
                finish();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Reshoot", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                resetShoot();
            }
        });
        alertDialog.show();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(16);

        Button btnPositive = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
        btnPositive.setTextSize(16);
        btnPositive.setTextColor(getResources().getColor(R.color.green));
        btnPositive.setAllCaps(false);

        Button btnNegative = alertDialog.getButton(Dialog.BUTTON_NEGATIVE);
        btnNegative.setTextSize(16);
        btnNegative.setTextColor(getResources().getColor(R.color.green));
        btnNegative.setAllCaps(false);

        Button btnNeural = alertDialog.getButton(Dialog.BUTTON_NEUTRAL);
        btnNeural.setTextSize(16);
        btnNeural.setTextColor(getResources().getColor(R.color.green));
        btnNeural.setAllCaps(false);
    }

    private void resetShoot() {
        stopTimeCounting();
        gpuCameraRecorder.stop();
        ivRecord.setTag(null);
        ivVideoDone.setVisibility(View.GONE);
        showVideoPlayViews();
        ivRecord.setImageResource(R.drawable.ic_capture_round_white);
        isTimerSet = false;
        SELECTED_TIME_PROGRESS = 0;
        videoDurationList.clear();
        VIDEO_TIME_SECONDS = 15;
        CURRENT_VIDEO_PROGRESS = 0;
        viewTimerContent.setVisibility(View.GONE);

        // showing thumbnail
        videoView_thumbnail.setVisibility(View.VISIBLE);

        if (getAppVideoTempFolder().exists()) {
            getAppVideoTempFolder().delete();
        }

        ivRecord.setVisibility(View.VISIBLE);
        progressbar.setProgress(CURRENT_VIDEO_PROGRESS);
        if (pathCounter > 0) {
            File file = new File(filepathListTemp.get(pathCounter - 1));
            file.delete();
            Log.w("file_mode", "1");
            pathCounter = 0;
        }
    }

    public static String getVideoFilePath(String filename) {
        return getAppVideoTempFolder().getAbsolutePath() + "/" + filename + ".mp4";
    }

    public static File getAppVideoTempFolder() {
        return Environment.getExternalStoragePublicDirectory(Constant.APP_DIRECTORY + "/" + Constant.APP_VIDEO_DIRECTORY + "/" + Constant.APP_TEMP_DIRECTORY);
    }

    public static File getAppVideoFolder() {
        return Environment.getExternalStoragePublicDirectory(Constant.APP_DIRECTORY + "/" + Constant.APP_VIDEO_DIRECTORY);
    }
}