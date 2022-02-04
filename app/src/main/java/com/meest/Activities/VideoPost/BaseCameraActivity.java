package com.meest.Activities.VideoPost;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Activities.VideoPost.ARGearAsset.ARGearApi;
import com.meest.Activities.VideoPost.ARGearAsset.ARGearDataModel;
import com.meest.Activities.VideoPost.ARGearAsset.ARGearWebService;
import com.meest.Activities.VideoPost.ARGearAsset.GLView;
import com.meest.Activities.VideoPost.ARGearAsset.camera.ReferenceCamera;
import com.meest.Activities.VideoPost.ARGearAsset.camera.ReferenceCamera2;
import com.meest.Activities.VideoPost.ARGearAsset.rendering.CameraTexture;
import com.meest.Activities.VideoPost.ARGearAsset.rendering.ScreenRenderer;
import com.meest.Activities.VideoPost.ARGearItemsAdapter.ARGearFilterAdapter;
import com.meest.Activities.VideoPost.ARGearItemsAdapter.ARGearStickerAdapter;
import com.meest.Activities.VideoPost.HelperClasses.DownloadAsyncResponse;
import com.meest.Activities.VideoPost.HelperClasses.DownloadAsyncTask;
import com.meest.Activities.VideoPost.HelperClasses.FileDeleteAsyncTask;
import com.meest.Activities.VideoPost.HelperClasses.PreferenceUtil;
import com.meest.Activities.VideoPost.HelperClasses.VideoPermissionHelper;
import com.meest.Adapters.CameraSpeedAdapter;
import com.meest.Adapters.MusicTrackAdapter;
import com.meest.Adapters.RecordOptionAdapter;
import com.meest.Interfaces.DownloadMedia;
import com.meest.R;
import com.meest.responses.AudioCategoryResponse;
import com.meest.Services.DownloadVideo;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.utils.FileUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.Widget.CameraFilterAdapter;
import com.meest.svs.models.AudioUploadResponse;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.seerslab.argear.exceptions.InvalidContentsException;
import com.seerslab.argear.exceptions.NetworkException;
import com.seerslab.argear.exceptions.SignedUrlGenerationException;
import com.seerslab.argear.session.ARGAuth;
import com.seerslab.argear.session.ARGContents;
import com.seerslab.argear.session.ARGFrame;
import com.seerslab.argear.session.ARGMedia;
import com.seerslab.argear.session.ARGSession;
import com.seerslab.argear.session.config.ARGCameraConfig;
import com.seerslab.argear.session.config.ARGConfig;
import com.seerslab.argear.session.config.ARGInferenceConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static com.arthenica.mobileffmpeg.FFmpeg.RETURN_CODE_CANCEL;
//import static com.arthenica.mobileffmpeg.FFmpeg.RETURN_CODE_SUCCESS;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;



@SuppressWarnings("ResultOfMethodCallIgnored")
public class BaseCameraActivity extends AppCompatActivity implements DownloadMedia {

    private ImageView ivRecord, ivCancelVideo, ivVideoDone, ivDeleteClip, img_file_upload;
    LinearLayout viewFlipCamera, viewFlash, viewFilters, viewSpeed, selectMusic/*, viewTimer, viewCameraEffect*/;
    private RelativeLayout viewCapture;
    LinearLayout filterLayout, stickerLayout, viewSticker;
    private ProgressBar progressbar;
    protected int cameraWidth = 960;
    protected int cameraHeight = 540;
    protected int videoWidth = 960;
    protected int videoHeight = 540;
    int AUDIO_PICKER_CODE = 111;
    int SELECTED_SPEED = 1;
    int SELECTED_TIME_PROGRESS;
    int CURRENT_VIDEO_PROGRESS, LAST_VIDEO_PROGRESS;
    int endTime = 250;
    TextView tvTime, tvSpeed, txt_song_name;
    public static MediaPlayer audioPlayer = null;
    private RecyclerView rvFilterList, rvSpeed, rvStickerList;
    FrameLayout cameraView;
    CountDownTimer countDownTimer;
    CircularProgressBar cpb;
    RelativeLayout viewProgressbar;
    RecyclerView rvRecordOption;
    double VIDEO_TIME_SECONDS = 60;
    boolean isAudioSelected, isSpeedOn, isTimerSet;
    String audio_merge_command = "", speed_video_command = "";
    String audiopath = "";
    public static String audioUrl, selectedAudioType = "none", audioId = "";
    CameraSpeedAdapter cameraSpeedAdapter;
    private static final int REQUEST_CODE_EDIT = 1;
    private static final int SELECT_VIDEO = 12;
    private static final int DISCARD_UPLOAD_CODE = 5;
    LinearLayout addMusic;
    GLView glView;
    List<String> speedList;
    ARGSession argsession;
    ReferenceCamera2 referenceCamera;
    private int mDeviceWidth = 0;
    private int mDeviceHeight = 0;
    private int mGLViewWidth = 0;
    private int mGLViewHeight = 0;
    private ARGFrame.Ratio mScreenRatio = ARGFrame.Ratio.RATIO_FULL;
    private ARGMedia.Ratio ratio = ARGMedia.Ratio.RATIO_16_9;
    private ARGMedia argMedia;
    private String parentFolderPath;
    private String mVideoFilePath;
    final int USE_CAMERA_API = 1;
    private ScreenRenderer screenRenderer;
    private CameraTexture cameraTexture;
    private boolean isShooting = false;
    ARGearDataModel arGearDataModel;
    boolean isFlashAvailable;
    String mediaFolderPath;
    boolean isFlashOn = false;
    List<ARGearDataModel.ARGearItem> filterList = new ArrayList<>();
    List<ARGearDataModel.ARGearItem> stickerList = new ArrayList<>();
    private ARGearDataModel.ARGearItem mCurrentStickeritem = null;
    private boolean mHasTrigger = false;
    private Toast mTriggerToast = null;
    boolean isAudioPlaying = false;
    TextView time_count_down;
    String type = "svs", duoVideo, trioVideo1, trioVideo2;
    String halfSpeed = "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5[a]";
    String oneAndHalfSpeed = "[0:v]setpts=0.75*PTS[v];[0:a]atempo=1.5[a]";
    String doubleSpeed = "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]";

    protected void onCreateActivity() {
        initUI();
    }

    private void initUI() {
        // for flash
        isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

        findIds();
        fetchARGearData();

        if (type.equalsIgnoreCase("story") ||
                type.equalsIgnoreCase("duo") ||
                type.equalsIgnoreCase("trio")) {
            // if feed then removing extra detail
            addMusic.setVisibility(View.GONE);
            selectMusic.setVisibility(View.GONE);
            viewSpeed.setVisibility(View.GONE);
            img_file_upload.setVisibility(View.GONE);

            mScreenRatio = ARGFrame.Ratio.RATIO_FULL;

            if (type.equalsIgnoreCase("story")) {
                selectedAudioType = "story";
            }
        } else if (type.equalsIgnoreCase("feed")) {
            addMusic.setVisibility(View.GONE);
            selectMusic.setVisibility(View.GONE);
            viewSpeed.setVisibility(View.GONE);
            img_file_upload.setVisibility(View.GONE);
            selectedAudioType = "feed";

            mScreenRatio = ARGFrame.Ratio.RATIO_4_3;
        }

        // initializing media player in case of duo or trio
        if (type.equalsIgnoreCase("duo") || type.equalsIgnoreCase("trio")) {
            try {
                if (audioPlayer == null)
                    audioPlayer = new MediaPlayer();
                else
                    audioPlayer.release();
                audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                if (type.equalsIgnoreCase("duo")) {
                    audioPlayer.setDataSource(this, Uri.parse(duoVideo));
                } else {
                    audioPlayer.setDataSource(this, Uri.parse(trioVideo1));
                }
                audioPlayer.setLooping(false);
                audioPlayer.prepare();
            } catch (IOException e) {
                Utilss.showToast(BaseCameraActivity.this, getString(R.string.Something_went_wrong_please_try_again_later),
                        R.color.msg_fail);
                finish();
                e.printStackTrace();
            }
        }

        parentFolderPath = getFilesDir().getAbsolutePath();
        mediaFolderPath = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/Meest";
        File mediaFolder = new File(mediaFolderPath);
        if (!mediaFolder.exists()) {
            mediaFolder.mkdir();
        }

        Point realSize = new Point();
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getRealSize(realSize);
        mDeviceWidth = realSize.x;
        mDeviceHeight = realSize.y;
        mGLViewWidth = realSize.x;
        mGLViewHeight = realSize.y;

        rvRecordOption.setLayoutManager(new LinearLayoutManager(BaseCameraActivity.this, RecyclerView.HORIZONTAL, false));
        rvRecordOption.setAdapter(new RecordOptionAdapter(BaseCameraActivity.this));

        // setting speed
        rvSpeed.setLayoutManager(new LinearLayoutManager(BaseCameraActivity.this, RecyclerView.HORIZONTAL, false));
        speedList = new ArrayList<>();
        speedList.add("0.5x");
        speedList.add("1x");
        speedList.add("1.5x");
        speedList.add("2x");
        cameraSpeedAdapter = new CameraSpeedAdapter(BaseCameraActivity.this, speedList);
        rvSpeed.setAdapter(cameraSpeedAdapter);
        cameraSpeedAdapter.setOnItemClickListener(new CameraSpeedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                SELECTED_SPEED = position;
                isSpeedOn = position != 2;
            }
        });

        File videoPath = new File(getAppVideoFolder().getAbsolutePath());
        if (!videoPath.exists()) {
            try {
                videoPath.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        img_file_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), SELECT_VIDEO);
            }
        });

        addMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent audio_picker_intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(audio_picker_intent, AUDIO_PICKER_CODE);
            }
        });

        ivCancelVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openExitConfirmDialog();
            }
        });

        ivRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ivRecord.getTag()) == null) {
                    startRecording();
                    ivRecord.setTag("Stop");
                    viewFlipCamera.setVisibility(View.GONE);
                    ivRecord.setImageResource(R.drawable.ic_camera_pause);
                    hideVideoPlayViews();
                    startTimeCounting();
                    LAST_VIDEO_PROGRESS = 0;
                    if (audioPlayer != null) {
                        audioPlayer.seekTo(0);
                        audioPlayer.start();
                    }
                } else if (ivRecord.getTag().equals(getString(R.string.app_record))) {
                    // pausing recording
                    argMedia.pauseRecording();

                    ivRecord.setTag("Stop");
                    ivRecord.setImageResource(R.drawable.ic_camera_pause);
                    hideVideoPlayViews();
                    startTimeCounting();
                    if (audioPlayer != null) {
                        audioPlayer.start();
                    }

                } else {
                    // pausing recording
                    argMedia.pauseRecording();

                    addMusic.setVisibility(View.GONE);
                    selectMusic.setVisibility(View.GONE);
                    stopTimeCounting();
                    Log.w("ravi_testing", "1");
                    if (audioPlayer != null) {
                        audioPlayer.pause();
                        Log.w("ravi_audio", "1");
                    }
                    ivRecord.setTag(getString(R.string.app_record));
                    showVideoPlayViews();
                    ivDeleteClip.setVisibility(View.VISIBLE);
                    ivVideoDone.setVisibility(View.VISIBLE);
                    ivRecord.setImageResource(R.drawable.ic_capture_round_white);
                    isTimerSet = false;
                    SELECTED_TIME_PROGRESS = 0;
                }
            }
        });

        viewFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterLayout.getVisibility() == View.VISIBLE) {
                    rvFilterList.setVisibility(View.GONE);
                    filterLayout.setVisibility(View.GONE);
//                    viewCapture.setVisibility(View.VISIBLE);
                    return;
                }
                rvFilterList.setVisibility(View.VISIBLE);
                filterLayout.setVisibility(View.VISIBLE);

//                viewCapture.setVisibility(View.GONE);
            }
        });

        viewSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stickerLayout.getVisibility() == View.VISIBLE) {
                    stickerLayout.setVisibility(View.GONE);
                    rvStickerList.setVisibility(View.GONE);
//                    viewCapture.setVisibility(View.VISIBLE);
                    return;
                }

                rvStickerList.setVisibility(View.VISIBLE);
                stickerLayout.setVisibility(View.VISIBLE);
//                viewCapture.setVisibility(View.GONE);
            }
        });

        viewFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFlashAvailable) {
                    Utilss.showToast(BaseCameraActivity.this, getString(R.string.Flash_is_not_available_in_your_device), R.color.msg_fail);
                    return;
                }

                // changing flash status
                try {
                    isFlashOn = !isFlashOn;
                    referenceCamera.setCameraFlash(isFlashOn);
//                    cameraManager.setTorchMode(cameraId, isFlashOn);
                } catch (Exception e) {
                    Utilss.showToast(BaseCameraActivity.this, getString(R.string.Some_error_occurred_while_changing_flash_status_please_try_again_later),
                            R.color.msg_fail);
                    e.printStackTrace();
                }
            }
        });

        viewFlipCamera.setOnClickListener(v -> {
            if (referenceCamera == null) {
                Utilss.showToast(BaseCameraActivity.this, getString(R.string.Some_error_occurred_while_changing_camera_please_try_again_later),
                        R.color.msg_fail);
                return;
            }

            if (referenceCamera.isCameraFacingFront()) {
                if (referenceCamera.changeCameraFacing()) {
                    viewFlash.setVisibility(View.VISIBLE);
                } else {
                    Utilss.showToast(BaseCameraActivity.this, getString(R.string.Some_error_occurred_while_changing_camera_please_try_again_later),
                            R.color.msg_fail);
                }
            } else {
                if (referenceCamera.changeCameraFacing()) {
                    viewFlash.setVisibility(View.GONE);
                } else {
                    Utilss.showToast(BaseCameraActivity.this, getString(R.string.Some_error_occurred_while_changing_camera_please_try_again_later),
                            R.color.msg_fail);
                }
            }
        });

        viewSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvSpeed.getVisibility() == View.VISIBLE) {
                    rvSpeed.setVisibility(View.GONE);
                    tvSpeed.setText("Speed\nOff");
                } else {
                    rvSpeed.setVisibility(View.VISIBLE);
                    tvSpeed.setText("Speed\nOn");
                    rvFilterList.setVisibility(View.GONE);
                    filterLayout.setVisibility(View.GONE);
                    viewCapture.setVisibility(View.VISIBLE);
                    stickerLayout.setVisibility(View.GONE);
                    rvStickerList.setVisibility(View.GONE);
                }
            }
        });

        selectMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCollectionsDialog();
            }
        });

        cameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                rvFilterList.setVisibility(View.GONE);
                filterLayout.setVisibility(View.GONE);
                stickerLayout.setVisibility(View.GONE);
                rvStickerList.setVisibility(View.GONE);
                return true;
            }
        });

        ivVideoDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareVideo();
            }
        });

        ivDeleteClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivRecord.setVisibility(View.VISIBLE);
            }
        });
    }

    private void prepareVideo() {
        if (audioPlayer != null) {
            audioPlayer.stop();
            Log.w("ravi_audio", "2");
        }
        if (argMedia.isRecording()) {
            argMedia.stopRecording();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SELECTED_SPEED != 1) {
                    String app_folder_path = Constant.createOutputPath(BaseCameraActivity.this,
                            mVideoFilePath.substring(mVideoFilePath.lastIndexOf(".")));
                    switch (SELECTED_SPEED) {
                        case 0:
                            changePlaybackSpeed(mVideoFilePath, app_folder_path, halfSpeed);
                            break;
                        case 2:
                            changePlaybackSpeed(mVideoFilePath, app_folder_path, oneAndHalfSpeed);
                            break;
                        case 3:
                            changePlaybackSpeed(mVideoFilePath, app_folder_path, doubleSpeed);
                            break;
                    }
                } else {
                    if (type.equalsIgnoreCase("duo")) {
                        String app_folder_path = Constant.createOutputPath(BaseCameraActivity.this,
                                mVideoFilePath.substring(mVideoFilePath.lastIndexOf(".")));
                        addTwoVideos(mVideoFilePath, duoVideo, app_folder_path);
                    } else if (type.equalsIgnoreCase("trio")) {
                        String app_folder_path = Constant.createOutputPath(BaseCameraActivity.this,
                                mVideoFilePath.substring(mVideoFilePath.lastIndexOf(".")));
                        addThreeVideos(mVideoFilePath, trioVideo1, trioVideo2, app_folder_path);
                    } else {
                        prepareAudio(mVideoFilePath);
                    }
                }
            }
        }, 300);
    }

    private void prepareAudio(String videoPath) {
        switch (selectedAudioType) {
            case "story":
            case "feed":
                // now need to extract audio
                Log.e("View", "1");
                createFinalVideo(videoPath);
            case "none":
                // now need to extract audio
                extractAudio(videoPath);
//               createFinalVideo(videoPath);
                break;
            case "internal":
                // now need to upload audio
                changeAudio(videoPath, audiopath);
                break;
            case "api":
                // now need to select audio
                mVideoFilePath = videoPath;
                viewProgressbar.setVisibility(View.VISIBLE);
                ivVideoDone.setVisibility(View.GONE);
                new DownloadVideo(this, "audioDownload", BaseCameraActivity.this).execute(audioUrl);
                break;
        }
    }

    private void createFinalVideo(String path) {
//        Intent intent;
//        if (type.equalsIgnoreCase("svs")) {
//            intent = new Intent(BaseCameraActivity.this, PostVideoActivity.class);
//            intent.putExtra("video_path", videoPath);
//            intent.putExtra("audioUrl", audioUrl);
//            startActivityForResult(intent, DISCARD_UPLOAD_CODE);
//            finish();
//        } else if (type.equalsIgnoreCase("feed")) {
//            intent = new Intent(BaseCameraActivity.this, NewPostUploadActivity.class);
//            intent.putExtra("mediaPath", videoPath);
//            intent.putExtra("isVideo", true);
//            startActivityForResult(intent, DISCARD_UPLOAD_CODE);
//            finish();
//        } else
//        if (type.equalsIgnoreCase("duo")) {
//            // making duo locally
//            String app_folder_path = createOutputPath(mVideoFilePath.substring(mVideoFilePath.lastIndexOf(".")));
//
//            addTwoVideos(videoPath, duoVideo, app_folder_path);
//        } else if (type.equalsIgnoreCase("trio")) {
//            // making duo locally
//            String app_folder_path = createOutputPath(mVideoFilePath.substring(mVideoFilePath.lastIndexOf(".")));
//            addThreeVideos(videoPath, trioVideo1, trioVideo2, app_folder_path);
//        } else {
//        }

        Intent intent_for_play = new Intent(this, PreviewVideoActivity.class);
        intent_for_play.putExtra("type", type);
        intent_for_play.putExtra("videoPath", path);
        intent_for_play.putExtra("audioId", audioId);
        intent_for_play.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent_for_play);
        finish();
    }

    private void hideViewsForProcessing() {
        viewProgressbar.setVisibility(View.VISIBLE);
        ivVideoDone.setVisibility(View.GONE);
        ivRecord.setVisibility(View.GONE);
    }

    private void showViewsForProcessing() {
        viewProgressbar.setVisibility(View.GONE);
        ivVideoDone.setVisibility(View.VISIBLE);
        ivRecord.setVisibility(View.VISIBLE);
    }

    private void extractAudio(String videoPath) {
        hideViewsForProcessing();
        String output = Constant.createOutputPath(BaseCameraActivity.this,
                ".aac");

        try {

            new Thread() {
                public void run() {

                    ArrayList<String> cmdList = new ArrayList<>();
                    cmdList.add("-i");
                    cmdList.add(videoPath);
                    cmdList.add("-vn");
                    cmdList.add("-acodec");
                    cmdList.add("copy");
                    cmdList.add(output);

                    StringBuilder sb = new java.lang.StringBuilder();

                    for (String str : cmdList) {
                        sb.append(str).append(" ");
                    }

                    String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                    int rc = FFmpeg.execute(cmd);
                    if (rc == RETURN_CODE_SUCCESS) {
                        uploadAudio(output, videoPath);
                    } else if (rc == RETURN_CODE_CANCEL) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showViewsForProcessing();
                                Toast.makeText(BaseCameraActivity.this, getApplicationContext().getString(R.string.Something_went_wrong), Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("alhaj", "Command execution cancelled by user.");
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showViewsForProcessing();
                                Toast.makeText(BaseCameraActivity.this,  getApplicationContext().getString(R.string.Something_went_wrong), Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                    }
                }
            }.start();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchARGearData() {
        ARGearApi arGearApi = ARGearWebService.createContentsService();

        arGearApi.getContents(Constant.ARGEAR_API_KEY).enqueue(new Callback<ARGearDataModel>() {
            @Override
            public void onResponse(Call<ARGearDataModel> call, Response<ARGearDataModel> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    Log.v("harsh", "body == " + response.body());
                    arGearDataModel = response.body();

                    extractData();
                } else {
                    Utilss.showToast(BaseCameraActivity.this, getString(R.string.Something_went_wrong_please_try_again_later),
                            R.color.msg_fail);
//                    finish();
                }
            }

            @Override
            public void onFailure(Call<ARGearDataModel> call, Throwable t) {
                Utilss.showToast(BaseCameraActivity.this,  getString(R.string.Something_went_wrong_please_try_again_later),
                        R.color.msg_fail);
//                finish();
            }
        });
    }

    private void initGLView() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        glView = new GLView(this, glViewListener);
        glView.setZOrderMediaOverlay(true);

        cameraView.addView(glView, params);
    }

    private void initCamera() {
        if (USE_CAMERA_API == 1) {
//            referenceCamera = new ReferenceCamera1(this, cameraListener);
//        } else {
            referenceCamera = new ReferenceCamera2(this, cameraListener, getWindowManager().getDefaultDisplay().getRotation());
        }
    }

    // extracting different types of data from ARGear
    private void extractData() {
        // clearing old items
        filterList.clear();
        stickerList.clear();

        for (ARGearDataModel.ARGearCategory arGearCategory : arGearDataModel.getCategories()) {
            for (ARGearDataModel.ARGearItem arGearItem : arGearCategory.getARGearItems()) {
                if (arGearItem.getType().equalsIgnoreCase("filter")) {
                    filterList.add(arGearItem);
                } else {
                    stickerList.add(arGearItem);
                }
            }
        }
        // binding data
        bindData();
    }

    private void changePlaybackSpeed(String input, String output, String speed) {
        hideViewsForProcessing();
        try {

            new Thread() {
                public void run() {

                    ArrayList<String> cmdList = new ArrayList<>();

                    cmdList.add("-i");
                    cmdList.add(input);
                    cmdList.add("-filter_complex");
                    cmdList.add(speed);
                    cmdList.add("-map");
                    cmdList.add("[v]");
                    cmdList.add("-map");
                    cmdList.add("[a]");
                    cmdList.add(output);

                    StringBuilder sb = new java.lang.StringBuilder();

                    for (String str : cmdList) {
                        sb.append(str).append(" ");
                    }

                    String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                    int rc = FFmpeg.execute(cmd);

                    if (rc == RETURN_CODE_SUCCESS) {
                        prepareAudio(output);
                    } else if (rc == RETURN_CODE_CANCEL) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivVideoDone.setVisibility(View.VISIBLE);
                                viewProgressbar.setVisibility(View.GONE);
                                Toast.makeText(BaseCameraActivity.this, getString(R.string.Something_went_wrong_please_try_again_later), Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("alhaj", "Command execution cancelled by user.");
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showViewsForProcessing();
                                Toast.makeText(BaseCameraActivity.this,  getString(R.string.Something_went_wrong_please_try_again_later), Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                    }
                }

            }.start();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addThreeVideos(String input1, String input2, String input3, String output) {
        hideViewsForProcessing();
        try {

            new Thread() {
                public void run() {

                    ArrayList<String> cmdList = new ArrayList<>();

                    cmdList.add("-i");
                    cmdList.add(input1);
                    cmdList.add("-i");
                    cmdList.add(input2);
                    cmdList.add("-i");
                    cmdList.add(input3);
                    cmdList.add("-filter_complex");
                    cmdList.add("[1:v]scale=1080:-1[v0];[2:v]scale=1080:-1[v1];[v0][v1]hstack=inputs=2[main1];[0:v]crop=in_w:in_h-600[v2];[v2]scale=1080:-1[v3];[main1]scale=1080:-1[v4];[v3][v4]vstack=inputs=2");
                    cmdList.add("-c:v");
                    cmdList.add("libx264");
                    cmdList.add("-crf");
                    cmdList.add("23");
                    cmdList.add("-preset");
                    cmdList.add("veryfast");
                    cmdList.add(output);

                    StringBuilder sb = new java.lang.StringBuilder();

                    for (String str : cmdList) {
                        sb.append(str).append(" ");
                    }

                    String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                    int rc = FFmpeg.execute(cmd);

                    if (rc == RETURN_CODE_SUCCESS) {
                        prepareAudio(output);
                    } else if (rc == RETURN_CODE_CANCEL) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showViewsForProcessing();
                                Toast.makeText(BaseCameraActivity.this, R.string.Something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("alhaj", "Command execution cancelled by user.");
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showViewsForProcessing();
                                Toast.makeText(BaseCameraActivity.this, R.string.Something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                    }
                }

            }.start();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addTwoVideos(String input1, String input2, String output) {
        hideViewsForProcessing();

        try {
            new Thread() {
                public void run() {

                    ArrayList<String> cmdList = new ArrayList<>();

                    cmdList.add("-i");
                    cmdList.add(input1);
                    cmdList.add("-i");
                    cmdList.add(input2);
                    cmdList.add("-filter_complex");
                    cmdList.add("[0:v]scale=-1:600[v0];[1:v]scale=-1:600[v1];[v0][v1]hstack=inputs=2");
                    cmdList.add("-c:v");
                    cmdList.add("libx264");
                    cmdList.add("-crf");
                    cmdList.add("23");
                    cmdList.add("-preset");
                    cmdList.add("veryfast");
                    cmdList.add(output);

                    StringBuilder sb = new StringBuilder();

                    for (String str : cmdList) {
                        sb.append(str).append(" ");
                    }

                    String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                    int rc = FFmpeg.execute(cmd);

                    if (rc == RETURN_CODE_SUCCESS) {
                        prepareAudio(output);
                    } else if (rc == RETURN_CODE_CANCEL) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showViewsForProcessing();
                                Toast.makeText(BaseCameraActivity.this, R.string.Something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("alhaj", "Command execution cancelled by user.");
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showViewsForProcessing();
                                Toast.makeText(BaseCameraActivity.this, R.string.Something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                    }

                }
            }.start();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void changeAudio(String videoPath, String songPath) {
        hideViewsForProcessing();
        String output = Constant.createOutputPath(BaseCameraActivity.this,
                videoPath.substring(videoPath.lastIndexOf(".")));
        try {

            new Thread() {
                public void run() {

                    ArrayList<String> cmdList = new ArrayList<>();

                    cmdList.add("-i");
                    cmdList.add(videoPath);
                    cmdList.add("-i");
                    cmdList.add(songPath);
                    cmdList.add("-c:v");
                    cmdList.add("copy");
                    cmdList.add("-map");
                    cmdList.add("0:v:0");
                    cmdList.add("-map");
                    cmdList.add("1:a:0");
                    cmdList.add("-shortest");
                    cmdList.add(output);

                    StringBuilder sb = new java.lang.StringBuilder();

                    for (String str : cmdList) {
                        sb.append(str).append(" ");
                    }

                    String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                    int rc = FFmpeg.execute(cmd);
                    if (rc == RETURN_CODE_SUCCESS) {
                        if (selectedAudioType.equalsIgnoreCase("api")) {
                            Log.e("View", "2");
                            createFinalVideo(output);
                        } else {
                            uploadAudio(songPath, output);
                        }
                    } else if (rc == RETURN_CODE_CANCEL) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showViewsForProcessing();
                                Toast.makeText(BaseCameraActivity.this, R.string.Something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("alhaj", "Command execution cancelled by user.");
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showViewsForProcessing();
                                Toast.makeText(BaseCameraActivity.this, R.string.Something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                    }
                }
            }.start();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void trimToOneMinute(String input, Uri uri, boolean isVideo) {
        hideViewsForProcessing();
        String output = Constant.createOutputPath(BaseCameraActivity.this,
                input.substring(input.lastIndexOf(".")));

        try {

            new Thread() {
                public void run() {
                    ArrayList<String> cmdList = new ArrayList<>();

                    cmdList.add("-ss");
                    cmdList.add("00:00:00");
                    cmdList.add("-i");
                    cmdList.add(input);
                    cmdList.add("-to");
                    cmdList.add("00:01:00");
                    cmdList.add("-c");
                    cmdList.add("copy");
                    cmdList.add(output);

                    StringBuilder sb = new java.lang.StringBuilder();

                    for (String str : cmdList) {
                        sb.append(str).append(" ");
                    }

                    String[] cmd = cmdList.toArray(new String[cmdList.size()]);

                    int rc = FFmpeg.execute(cmd);
                    if (rc == RETURN_CODE_SUCCESS) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewProgressbar.setVisibility(View.GONE);
                                if (isVideo) {
                                    extractAudio(output);
                                } else {
                                    selectAudio(output, uri);
                                }
                            }
                        });
                    } else if (rc == RETURN_CODE_CANCEL) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showViewsForProcessing();
                                Toast.makeText(BaseCameraActivity.this, R.string.Something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("alhaj", "Command execution cancelled by user.");
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showViewsForProcessing();
                                Toast.makeText(BaseCameraActivity.this, R.string.Something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.i("alhaj", String.format("Command execution failed with rc=%d and the output below.", rc));
                    }

                }

            }.start();

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindData() {
        // binding filter data
        ARGearFilterAdapter filterAdapter = new ARGearFilterAdapter(this, filterList);
        rvFilterList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFilterList.setAdapter(filterAdapter);
        filterAdapter.setOnItemClickListener(new CameraFilterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                setFilter(filterList.get(position));
                viewCapture.setVisibility(View.VISIBLE);
            }
        });

        // binding sticker data
        ARGearStickerAdapter stickerAdapter = new ARGearStickerAdapter(this, stickerList);
        rvStickerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvStickerList.setAdapter(stickerAdapter);
        stickerAdapter.setOnItemClickListener(new CameraFilterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                setSticker(stickerList.get(position));
                viewCapture.setVisibility(View.VISIBLE);
            }
        });

    }

    public void setFilter(ARGearDataModel.ARGearItem item) {
        String filePath = parentFolderPath + "/" + item.getUuid();
        showPrd();
        if (getLastUpdateAt(BaseCameraActivity.this) > getFilterUpdateAt(BaseCameraActivity.this, item.getUuid())) {
            new FileDeleteAsyncTask(new File(filePath), new FileDeleteAsyncTask.OnAsyncFileDeleteListener() {
                @Override
                public void processFinish(Object result) {
                    Log.d("harsh", "file delete success!");

                    setFilterUpdateAt(BaseCameraActivity.this, item.getUuid(), getLastUpdateAt(BaseCameraActivity.this));
                    requestSignedUrl(item, filePath, false);
                }
            }).execute();
        } else {
            if (new File(filePath).exists()) {
                setItem(ARGContents.Type.FilterItem, filePath, item);
            } else {
                requestSignedUrl(item, filePath, false);
            }
        }
    }

    // region - network
    private void requestSignedUrl(ARGearDataModel.ARGearItem item, String path, final boolean isArItem) {
        showPrd();
        argsession.auth().requestSignedUrl(item.getZipFile(), item.getTitle(), item.getType(), new ARGAuth.Callback() {
            @Override
            public void onSuccess(String url) {
                requestDownload(path, url, item, isArItem);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SignedUrlGenerationException) {
                    Log.e("harsh", "SignedUrlGenerationException !! ");
                } else if (e instanceof NetworkException) {
                    Log.e("harsh", "NetworkException !!");
                }

                hidePrd();
            }
        });
    }

    public void setSticker(ARGearDataModel.ARGearItem item) {
        String filePath = parentFolderPath + "/" + item.getUuid();
        showPrd();

        if (getLastUpdateAt(BaseCameraActivity.this) > getStickerUpdateAt(BaseCameraActivity.this, item.getUuid())) {
            new FileDeleteAsyncTask(new File(filePath), new FileDeleteAsyncTask.OnAsyncFileDeleteListener() {
                @Override
                public void processFinish(Object result) {
                    Log.d("harsh", "file delete success!");

                    setStickerUpdateAt(BaseCameraActivity.this, item.getUuid(), getLastUpdateAt(BaseCameraActivity.this));
                    requestSignedUrl(item, filePath, true);
                }
            }).execute();
        } else {
            if (new File(filePath).exists()) {
                setItem(ARGContents.Type.ARGItem, filePath, item);
            } else {
                requestSignedUrl(item, filePath, true);
            }
        }
    }

    private void requestDownload(String targetPath, String url, ARGearDataModel.ARGearItem item, boolean isSticker) {
        new DownloadAsyncTask(targetPath, url, new DownloadAsyncResponse() {
            @Override
            public void processFinish(boolean result) {
                // hiding progress dialog
                hidePrd();
                if (result) {
                    if (isSticker) {
                        setItem(ARGContents.Type.ARGItem, targetPath, item);
                    } else {
                        setItem(ARGContents.Type.FilterItem, targetPath, item);
                    }
                } else {
                    Utilss.showToast(BaseCameraActivity.this, getString(R.string.Something_went_wrong_while_processing_your_request_please_try_again_late),
                            R.color.msg_fail);
                }
            }
        }).execute();
    }

    public void setItem(ARGContents.Type type, String path, ARGearDataModel.ARGearItem itemModel) {

        mCurrentStickeritem = null;
        mHasTrigger = false;
        hidePrd();

        argsession.contents().setItem(type, path, itemModel.getUuid(), new ARGContents.Callback() {
            @Override
            public void onSuccess() {
                if (type == ARGContents.Type.ARGItem) {
                    mCurrentStickeritem = itemModel;
                    mHasTrigger = itemModel.getHasTrigger();

                    Utilss.showToast(BaseCameraActivity.this, getString(R.string.Sticker_applied),
                            R.color.green);
                } else {
                    Utilss.showToast(BaseCameraActivity.this, getString(R.string.Sticker_applied),
                            R.color.green);
                }
            }

            @Override
            public void onError(Throwable e) {
                mCurrentStickeritem = null;
                mHasTrigger = false;
                if (e instanceof InvalidContentsException) {
                    Log.e("harsh", "InvalidContentsException");
                }
                Utilss.showToast(BaseCameraActivity.this, getString(R.string.Something_went_wrong_while_processing_your_request_please_try_again_late),
                        R.color.msg_fail);
            }
        });
    }

    private void takePictureOnGlThread(int textureId) {
        isShooting = false;

        ARGMedia.Ratio ratio;
        if (mScreenRatio == ARGFrame.Ratio.RATIO_FULL) {
            ratio = ARGMedia.Ratio.RATIO_16_9;
        } else if (mScreenRatio == ARGFrame.Ratio.RATIO_4_3) {
            ratio = ARGMedia.Ratio.RATIO_4_3;
        } else {
            ratio = ARGMedia.Ratio.RATIO_1_1;
        }

        String path;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            path = parentFolderPath + "/" + System.currentTimeMillis() + ".jpg";
        } else {
            path = mediaFolderPath + "/" + System.currentTimeMillis() + ".jpg";
        }

        argMedia.takePicture(textureId, path, ratio);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseCameraActivity.this,getString(R.string.The_file_has_been_saved_to_your_Gallery), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateTriggerStatus(final int triggerstatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCurrentStickeritem != null && mHasTrigger) {
                    String strTrigger = null;
                    if ((triggerstatus & 1) != 0) {
                        strTrigger = "Open your mouth.";
                    } else if ((triggerstatus & 2) != 0) {
                        strTrigger = "Move your head side to side.";
                    } else if ((triggerstatus & 8) != 0) {
                        strTrigger = "Blink your eyes.";
                    } else {
                        if (mTriggerToast != null) {
                            mTriggerToast.cancel();
                            mTriggerToast = null;
                        }
                    }

                    if (strTrigger != null) {
                        mTriggerToast = Toast.makeText(BaseCameraActivity.this, strTrigger, Toast.LENGTH_SHORT);
                        mTriggerToast.setGravity(Gravity.CENTER, 0, 0);
                        mTriggerToast.show();
                        mHasTrigger = false;
                    }
                }
            }
        });
    }

    private void setLastUpdateAt(Context context, long updateAt) {
        PreferenceUtil.putLongValue(context, Constant.USER_PREF_NAME, "ContentLastUpdateAt", updateAt);
    }

    private long getLastUpdateAt(Context context) {
        return PreferenceUtil.getLongValue(context, Constant.USER_PREF_NAME, "ContentLastUpdateAt");
    }

    private void setFilterUpdateAt(Context context, String itemId, long updateAt) {
        PreferenceUtil.putLongValue(context, Constant.USER_PREF_NAME_FILTER, itemId, updateAt);
    }

    private long getFilterUpdateAt(Context context, String itemId) {
        return PreferenceUtil.getLongValue(context, Constant.USER_PREF_NAME_FILTER, itemId);
    }

    private void setStickerUpdateAt(Context context, String itemId, long updateAt) {
        PreferenceUtil.putLongValue(context, Constant.USER_PREF_NAME_STICKER, itemId, updateAt);
    }

    private long getStickerUpdateAt(Context context, String itemId) {
        return PreferenceUtil.getLongValue(context, Constant.USER_PREF_NAME_STICKER, itemId);
    }

    GLView.GLViewListener glViewListener = new GLView.GLViewListener() {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            screenRenderer.create(gl, config);
            cameraTexture.createCameraTexture();
        }

        @Override
        public void onDrawFrame(GL10 gl, int width, int height) {
            if (cameraTexture.getSurfaceTexture() == null) {
                return;
            }

            if (referenceCamera != null) {
                referenceCamera.setCameraTexture(cameraTexture.getTextureId(), cameraTexture.getSurfaceTexture());
            }

            ARGFrame frame = argsession.drawFrame(gl, mScreenRatio, width, height);
            screenRenderer.draw(frame, width, height);

            if (mHasTrigger) updateTriggerStatus(frame.getItemTriggerFlag());

            if (argMedia != null) {
                if (argMedia.isRecording()) argMedia.updateFrame(frame.getTextureId());
                if (isShooting) takePictureOnGlThread(frame.getTextureId());
            }
        }
    };

    ReferenceCamera.CameraListener cameraListener = new ReferenceCamera.CameraListener() {
        @Override
        public void setConfig(int previewWidth, int previewHeight, float verticalFov, float horizontalFov, int orientation, boolean isFrontFacing, float fps) {
            argsession.setCameraConfig(new ARGCameraConfig(previewWidth,
                    previewHeight,
                    verticalFov,
                    horizontalFov,
                    orientation,
                    isFrontFacing,
                    fps));
        }

        // region - for camera api 1
        @Override
        public void updateFaceRects(Camera.Face[] faces) {
            argsession.updateFaceRects(faces);
        }

        @Override
        public void feedRawData(byte[] data) {
            argsession.feedRawData(data);
        }
        // endregion

        // region - for camera api 2
        @Override
        public void updateFaceRects(int numFaces, int[][] bbox) {
            argsession.updateFaceRects(numFaces, bbox);
        }

        @Override
        public void feedRawData(Image data) {
            argsession.feedRawData(data);
        }
        // endregion
    };

    private void findIds() {
        tvTime = findViewById(R.id.tvTime);
        tvSpeed = findViewById(R.id.tvSpeed);
        progressbar = findViewById(R.id.progressbar);
        ivRecord = findViewById(R.id.ivRecord);
        txt_song_name = findViewById(R.id.txt_song_name);
        img_file_upload = findViewById(R.id.img_file_upload);
        viewFlipCamera = findViewById(R.id.viewFlipCamera);
        viewFlash = findViewById(R.id.viewFlash);
        viewFilters = findViewById(R.id.viewFilters);
        viewCapture = findViewById(R.id.viewCapture);
        viewSpeed = findViewById(R.id.viewSpeed);
        selectMusic = findViewById(R.id.selectMusic);
        viewSticker = findViewById(R.id.viewSticker);
        ivCancelVideo = findViewById(R.id.ivCancelVideo);
        stickerLayout = findViewById(R.id.stickerLayout);
        filterLayout = findViewById(R.id.filterLayout);
        ivVideoDone = findViewById(R.id.ivVideoDone);
        ivDeleteClip = findViewById(R.id.ivDeleteClip);
        cameraView = findViewById(R.id.cameraView);
        rvFilterList = findViewById(R.id.rvFilterList);
        rvStickerList = findViewById(R.id.rvStickerList);
        rvRecordOption = findViewById(R.id.rvRecordOption);
        rvSpeed = findViewById(R.id.rvSpeed);
        addMusic = findViewById(R.id.addMusic);
        cpb = findViewById(R.id.cpb);
        viewProgressbar = findViewById(R.id.viewProgressbar);
        time_count_down = findViewById(R.id.time_count_down);
    }

    public void showCollectionsDialog() {
        // stopping audio player if it is playing
        if (audioPlayer != null && audioPlayer.isPlaying()) {
            audioPlayer.stop();
        }

        View view = getLayoutInflater().inflate(R.layout.fragment_music_category_list, null);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_music_track);
        LottieAnimationView loading = view.findViewById(R.id.loading);

        loading.setAnimation("loading.json");
        loading.playAnimation();
        loading.loop(true);
        loading.setVisibility(View.VISIBLE);

        BottomSheetDialog dialog = new BottomSheetDialog(BaseCameraActivity.this);
        dialog.setContentView(view);
        dialog.show();

        final AudioCategoryResponse[] audioCategoryResponses = new AudioCategoryResponse[1];
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<AudioCategoryResponse> call = webApi.getAllAudioCategory(header);
        call.enqueue(new Callback<AudioCategoryResponse>() {
            @Override
            public void onResponse(Call<AudioCategoryResponse> call, Response<AudioCategoryResponse> response) {
                loading.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getSuccess()) {
                    audioCategoryResponses[0] = response.body();

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(BaseCameraActivity.this));

                    MusicTrackAdapter adapter = new MusicTrackAdapter(BaseCameraActivity.this, audioCategoryResponses[0], dialog);

                    //setting adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                } else {
                    Utilss.showToast(BaseCameraActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<AudioCategoryResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Utilss.showToast(BaseCameraActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void hideVideoPlayViews() {
        ivCancelVideo.setVisibility(View.GONE);
        viewSticker.setVisibility(View.GONE);
        viewFlipCamera.setVisibility(View.GONE);
        viewFlash.setVisibility(View.GONE);
        viewFilters.setVisibility(View.GONE);
        viewSpeed.setVisibility(View.GONE);
        addMusic.setVisibility(View.GONE);
        img_file_upload.setVisibility(View.GONE);
        ivVideoDone.setVisibility(View.GONE);
        ivDeleteClip.setVisibility(View.GONE);
        selectMusic.setVisibility(View.GONE);
    }

    private void showVideoPlayViews() {
        ivCancelVideo.setVisibility(View.VISIBLE);
//        viewSticker.setVisibility(View.VISIBLE);
        viewFlipCamera.setVisibility(View.VISIBLE);
        if (!referenceCamera.isCameraFacingFront()) {
            viewFlash.setVisibility(View.VISIBLE);
        }
//        viewFilters.setVisibility(View.VISIBLE);
        ivDeleteClip.setVisibility(View.VISIBLE);
        ivVideoDone.setVisibility(View.VISIBLE);
    }

    private void openExitConfirmDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Tips");
        alertDialog.setMessage(getString(R.string.Discard_current_sound_and_recording_progress));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.Exit), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                if (audioPlayer != null) {
                    audioPlayer.stop();
                    Log.w("ravi_audio", "3");
                }
                dialog.dismiss();
                if (getAppVideoFolder().exists()) {
                    getAppVideoFolder().delete();
                }
                finish();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                if (audioPlayer != null) {
                    audioPlayer.stop();
                    Log.w("ravi_audio", "4");
                }
                dialog.dismiss();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Reshoot", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                resetShoot();
            }
        });
        alertDialog.show();

        TextView textView = alertDialog.findViewById(android.R.id.message);
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
        if (argMedia.isRecording()) {
            argMedia.stopRecording();
        }
        CURRENT_VIDEO_PROGRESS = 0;
        SELECTED_SPEED = 1;
        isAudioSelected = false;
        isSpeedOn = false;
        if (type.equalsIgnoreCase("svs")) {
            time_count_down.setVisibility(View.GONE);
            selectMusic.setVisibility(View.VISIBLE);
            viewCapture.setVisibility(View.VISIBLE);
            addMusic.setVisibility(View.VISIBLE);
            viewSpeed.setVisibility(View.VISIBLE);
            img_file_upload.setVisibility(View.VISIBLE);
        }
        viewFilters.setVisibility(View.VISIBLE);
        viewSticker.setVisibility(View.VISIBLE);
        ivRecord.setVisibility(View.VISIBLE);
        progressbar.setProgress(CURRENT_VIDEO_PROGRESS);
        Log.w("file_mode", "1");
        ivRecord.setTag(null);
        argMedia.stopRecording();
        if (mVideoFilePath != null && mVideoFilePath.length() > 0) {
            File checkFile = new File(mVideoFilePath);
            if (checkFile.exists()) {
                checkFile.delete();
            }
        }
        if (audioPlayer != null) {
            audioPlayer.reset();
            txt_song_name.setText(getString(R.string.Add_sound));
        }
    }

    private void setupARGear() {
        if (argsession == null) {

            if (!VideoPermissionHelper.hasPermission(this)) {
                if (VideoPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                    finish();
                    Toast.makeText(this, getString(R.string.Permissions_required_for_creating_video), Toast.LENGTH_SHORT).show();
                    return;
                }
                VideoPermissionHelper.requestPermission(this);
                return;
            }

            ARGConfig config
                    = new ARGConfig(Constant.ARGEAR_API_URL, Constant.ARGEAR_API_KEY, Constant.ARGEAR_SECRET_KEY, Constant.ARGEAR_AUTH_KEY);
            Set<ARGInferenceConfig.Feature> inferenceConfig
                    = EnumSet.of(ARGInferenceConfig.Feature.FACE_HIGH_TRACKING);

            argsession = new ARGSession(this, config, inferenceConfig);
            argMedia = new ARGMedia(argsession);

            screenRenderer = new ScreenRenderer();
            cameraTexture = new CameraTexture();

            initGLView();
            initCamera();
        }
        referenceCamera.startCamera();
        setGLViewSize(referenceCamera.getPreviewSize());

        if (referenceCamera.isCameraFacingFront()) {
            viewFlash.setVisibility(View.GONE);
        } else {
            viewFlash.setVisibility(View.VISIBLE);
        }

        argsession.resume();
    }

    private void setGLViewSize(int[] cameraPreviewSize) {
        int previewWidth = cameraPreviewSize[1];
        int previewHeight = cameraPreviewSize[0];

        if (mScreenRatio == ARGFrame.Ratio.RATIO_FULL) {
            mGLViewHeight = mDeviceHeight;
            mGLViewWidth = (int) ((float) mDeviceHeight * previewWidth / previewHeight);
        } else {
            mGLViewWidth = mDeviceWidth;
            mGLViewHeight = (int) ((float) mDeviceWidth * previewHeight / previewWidth);
        }

        if (glView != null
                && (mGLViewWidth != glView.getViewWidth() || mGLViewHeight != glView.getHeight())) {
            cameraView.removeView(glView);
            glView.getHolder().setFixedSize(mGLViewWidth, mGLViewHeight);
            cameraView.addView(glView);
        }
    }

    public int getGLViewWidth() {
        return mGLViewWidth;
    }

    public int getGLViewHeight() {
        return mGLViewHeight;
    }

    public void setMeasureSurfaceView(View view) {
        if (view.getParent() instanceof FrameLayout) {
            view.setLayoutParams(new FrameLayout.LayoutParams(mGLViewWidth, mGLViewHeight));
        } else if (view.getParent() instanceof RelativeLayout) {
            view.setLayoutParams(new RelativeLayout.LayoutParams(mGLViewWidth, mGLViewHeight));
        }

        /* to align center */
        if ((mScreenRatio == ARGFrame.Ratio.RATIO_FULL) && (mGLViewWidth > mDeviceWidth)) {
            view.setX((mDeviceWidth - mGLViewWidth) / 2);
        } else {
            view.setX(0);
        }
    }

    public void clearFilter() {
        argsession.contents().clear(ARGContents.Type.FilterItem);
    }

    public void clearStickers() {
        mCurrentStickeritem = null;
        mHasTrigger = false;

        argsession.contents().clear(ARGContents.Type.ARGItem);
    }

    private void startRecording() {
        if (referenceCamera == null) {
            return;
        }

        int bitrate = 10 * 500 * 1000; // 10M

        int[] previewSize = referenceCamera.getPreviewSize();
        mVideoFilePath = getVideoFilePath("Meest_" + System.currentTimeMillis());

        argMedia.initRecorder(mVideoFilePath, previewSize[0], previewSize[1], bitrate,
                false, false, false, ratio);
        argMedia.startRecording();

        Toast.makeText(this, getString(R.string.started_recording), Toast.LENGTH_SHORT).show();
    }

    private void stopRecording() {
        argMedia.stopRecording();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupARGear();
        if (audioPlayer != null && isAudioPlaying) {
            audioPlayer.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (argsession != null) {
            referenceCamera.stopCamera();
            argsession.pause();
        }
        if (audioPlayer != null && audioPlayer.isPlaying()) {
            isAudioPlaying = true;
            audioPlayer.stop();
        } else {
            isAudioPlaying = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioPlayer != null) {
            audioPlayer.stop();
            audioPlayer = null;
        }
        if (argsession != null) {
            argsession.destroy();
        }
        if (referenceCamera != null) {
            referenceCamera.destroy();
        }
    }

    public static String getVideoFilePath(String filename) {
        return getAppVideoFolder().getAbsolutePath() + "/" + filename + ".mp4";
    }

    public static File getAppVideoFolder() {
        return Environment.getExternalStoragePublicDirectory(Constant.APP_DIRECTORY);
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
                    time_count_down.setVisibility(View.VISIBLE);
                    time_count_down.setText((60 - CURRENT_VIDEO_PROGRESS) + " sec");

                    if (isTimerSet && CURRENT_VIDEO_PROGRESS == SELECTED_TIME_PROGRESS) {
                        stopTimeCounting();
                        Log.w("ravi_testing", "2");
                        stopRecording();
                        time_count_down.setVisibility(View.GONE);
                        ivRecord.setTag(getString(R.string.app_record));
                        showVideoPlayViews();
                        ivDeleteClip.setVisibility(View.VISIBLE);
                        ivRecord.setImageResource(R.drawable.ic_capture_round_white);
                        isTimerSet = false;
                        SELECTED_TIME_PROGRESS = 0;

                    }
                    if (CURRENT_VIDEO_PROGRESS >= VIDEO_TIME_SECONDS) {
                        countDownTimer.onFinish();
                        time_count_down.setVisibility(View.GONE);
                        if (audioPlayer != null) {
                            audioPlayer.stop();
                            Log.w("ravi_audio", "6");
                            Log.w("ravi_testing", "stopped");
                        }
                    }
                }

                @Override
                public void onFinish() {
                    setProgress(CURRENT_VIDEO_PROGRESS, endTime);
                    ivRecord.setVisibility(View.GONE);
                    stopTimeCounting();
                    Log.w("ravi_testing", "3");
                    if (audioPlayer != null) {
                        audioPlayer.stop();
                        Log.w("ravi_audio", "7");
                        Log.w("ravi_testing", "stopped");
                    }
                    stopRecording();
                    ivRecord.setTag(getString(R.string.app_record));
                    showVideoPlayViews();
                    ivRecord.setImageResource(R.drawable.ic_capture_round_white);
                }
            };
        }
        countDownTimer.start();

    }

    private void stopTimeCounting() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        Log.w("finished", "done");
    }

    public void setProgress(int startTime, int endTime) {
        progressbar.setMax(endTime);
        progressbar.setProgress(startTime);

    }

    private void hidePrd() {
        if (cpb != null && viewProgressbar != null) {
            viewProgressbar.setVisibility(View.GONE);
            ivVideoDone.setVisibility(View.VISIBLE);
            ((CircularProgressDrawable) cpb.getIndeterminateDrawable()).progressiveStop();
        }
    }

    private void showPrd() {
        if (cpb != null && viewProgressbar != null) {
            viewProgressbar.setVisibility(View.VISIBLE);
            ((CircularProgressDrawable) cpb.getIndeterminateDrawable()).start();
        }
    }

    @Override
    public void onBackPressed() {
        openExitConfirmDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUDIO_PICKER_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                audiopath = FileUtils.getPath(BaseCameraActivity.this, uri);

                selectedAudioType = "internal";
                try {
                    if (audiopath != null) {
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(this, uri);
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        long timeInMillisec = Long.parseLong(time);
                        retriever.release();

                        if (timeInMillisec > 60000) {
                            VIDEO_TIME_SECONDS = 60;
                            Log.v("harsh", "VIDEO_TIME_SECONDS == " + VIDEO_TIME_SECONDS);
                            trimToOneMinute(audiopath, uri, false);
                        } else {
                            selectAudio(audiopath, uri);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, getString(R.string.Something_went_wrong_while_processing_your_request_please_try_again_late), Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK) {
            Uri contentURI = data.getData();
            String selectedVideoPath = FileUtils.getPath(BaseCameraActivity.this, contentURI);
            Log.d("harsh", "selectedVideoPath == " + selectedVideoPath);
            try {
                if (selectedVideoPath != null) {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(this, contentURI);
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long timeInMillisec = Long.parseLong(time);
                    retriever.release();

                    if (timeInMillisec > 60000) {
                        VIDEO_TIME_SECONDS = 60;
                        trimToOneMinute(selectedVideoPath, contentURI, true);
                    } else {
                        Intent intent = new Intent(BaseCameraActivity.this, PostVideoActivity.class);
                        intent.putExtra("video_path", selectedVideoPath);
                        startActivity(intent);
                        finish();
                    }
//                    Intent intent = new Intent(BaseCameraActivity.this, PostVideoActivity.class);
//                    intent.putExtra("video_path", selectedVideoPath);
//                    startActivityForResult(intent, DISCARD_UPLOAD_CODE);
//                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.Something_went_wrong_while_processing_your_request_please_try_again_late), Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == DISCARD_UPLOAD_CODE && data != null) {
            if (data.getExtras().getBoolean("discard", false)) {
                // resetting video recording
                resetShoot();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void selectAudio(String audio_path, Uri uri) {
        showViewsForProcessing();
        txt_song_name.setText(String.valueOf(uri.getLastPathSegment()));

        isAudioSelected = true;
        CURRENT_VIDEO_PROGRESS = 0;
        VIDEO_TIME_SECONDS = 30;
        countDownTimer = null;
        stopRecording();
        progressbar.setProgress(CURRENT_VIDEO_PROGRESS);

        try {
            audioPlayer = new MediaPlayer();
            audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            audioPlayer.setDataSource(getApplicationContext(), Uri.parse(audio_path));
            audioPlayer.setLooping(true);
            audioPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadAudio(String audioPath, String videoPath) {
        File file = new File(audioPath);
        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("audio", file.getName(), requestBody);

        // posting video
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<AudioUploadResponse> call = webApi.uploadAudio(header, fileToUpload);
        call.enqueue(new Callback<AudioUploadResponse>() {
            @Override
            public void onResponse(Call<AudioUploadResponse> call, Response<AudioUploadResponse> response) {
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    // header for form data
                    audioId = response.body().getData().getId();
                    Log.e("View", "3");
                    createFinalVideo(videoPath);
                } else {
                    hidePrd();
                    Utilss.showToast(BaseCameraActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<AudioUploadResponse> call, Throwable t) {
                hidePrd();
                Utilss.showToast(BaseCameraActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    @Override
    public void downloadDone(boolean isDone, String mediaPath) {
        viewProgressbar.setVisibility(View.GONE);
        ivVideoDone.setVisibility(View.VISIBLE);
        changeAudio(mVideoFilePath, mediaPath);
    }
}
