package com.meest.metme.camera2;


import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.meest.R;
import com.meest.metme.camera2.customVideoViews.BarThumb;
import com.meest.metme.camera2.customVideoViews.CustomRangeSeekBar;
import com.meest.metme.camera2.customVideoViews.FastVideoView;
import com.meest.metme.camera2.customVideoViews.OnRangeSeekBarChangeListener;
import com.meest.metme.camera2.customVideoViews.TileView;
import com.meest.metme.camera2.mp4compose.Rotation;
import com.meest.metme.camera2.mp4compose.composer.Mp4Composer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


//import com.daasuu.mp4compose.Rotation;
//import com.daasuu.mp4compose.composer.Mp4Composer;

public class AdjustActivity extends AppCompatActivity {

    SimpleExoPlayer absPlayerInternal;
    Uri imageUri;
    String videoURL, videoURL1;
    private ImageView RL;
    private ImageView RR;
    private ImageView FH;
    private ImageView FV;
    private ImageView close;
    private ImageView save;
    LinearLayout exoLayout, ll_adjust_1;
    RelativeLayout rl_adjust_1;
    int rotation = 0;

    FastVideoView videoView;
    private TextView time;
    private ImageView play;
    private TextView reset;
    int duration = 0;
    int current = 0;
    boolean isFlipVertical = false;
    boolean isFlipHorizontal = false;

    ArrayList<AdjustModel> adjustModelArrayList = new ArrayList<>();
    ArrayList<AdjustModel> adjustModelArrayListRedo = new ArrayList<>();
    private FastVideoView textureView;
    private ImageView undo;
    private ImageView redo;


    private TileView tileView;
    private CustomRangeSeekBar mCustomRangeSeekBarNew;
    private SeekBar seekBarVideo;
    private int mDuration = 0;
    private int mTimeVideo = 0;
    private int mStartPosition = 0;
    private int mEndPosition = 0;
    // set your max video trim seconds
    private int mMaxDuration = 60;
    private Handler mHandler = new Handler();

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust);

        ////////////////////////////////////////
        ll_adjust_1 = findViewById(R.id.ll_adjust_1);
        rl_adjust_1 = findViewById(R.id.rl_adjust_1);
//        String adjust_code;
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                adjust_code= null;
//            } else {
//                adjust_code= extras.getString("ADJ_CODE");
//            }
//        } else {
//            adjust_code= (String) savedInstanceState.getSerializable("ADJ_CODE");
//        }

//        switch (adjust_code) {
//            case "TRIM" :
//                ll_adjust_1.setVisibility(View.GONE);
//                break;
//            case "ADJUST" :
//                rl_adjust_1.setVisibility(View.GONE);
//                break;
//        }
        /////////////////////////////////////////

        initView();
        if (getIntent().getStringExtra("videoPath") != null) {
            videoURL = getIntent().getStringExtra("videoPath");
        }
        videoURL1 = videoURL.substring(0, videoURL.length()-4)+new Date().getTime()+ "a.mp4";
        Log.e("dataa","vv:"+videoURL1);

        listiners();
        PlayVideo();
        setupVideoEdit();


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (videoView != null) {
            PlayVideo();
            resetAll();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("videoPath", videoURL);
        setResult(RESULT_OK,resultIntent);
        finish();
    }


    public void PlayVideo() {
//        videoView.setMediaController(new MediaController(this));
        Uri uriOfContentUrl = Uri.parse(videoURL);
        videoView.setVideoURI(uriOfContentUrl);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                duration = videoView.getDuration();
                mDuration = videoView.getDuration() / 1000;
                setSeekBarPosition();
            }
        });
        videoView.setPlayPauseListener(new FastVideoView.PlayPauseListener() {

            @Override
            public void onPlay() {
                System.out.println("Play!");
            }

            @Override
            public void onPause() {
                System.out.println("Pause!");
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                seekBarVideo.setProgress(0);
                videoView.seekTo(mStartPosition * 1000);
                videoView.pause();
            }
        });


//        if (videoView.isPlaying()) {
        play.setImageDrawable(getDrawable(R.drawable.exo_controls_pause));
//            play.setText("Pause");
        updateProgressBar();
//        }
    }
    private void listiners() {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    play.setImageDrawable(getDrawable(R.drawable.exo_controls_play));
                    mHandler.removeCallbacks(mUpdateTimeTask);
//                    play.setText("Play");
                } else {
                    videoView.start();
                    updateProgressBar();
//                    play.setText("Pause");
                    play.setImageDrawable(getDrawable(R.drawable.exo_controls_pause));
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        RL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rotation <= 270) {
                    if (rotation >= 90) {
                        rotation = rotation - 90;
                    } else {
                        rotation = 270;
                    }
                } else {
                    rotation = 0;
                }
                Log.e("rotation", "rotation value: " + rotation);

                videoView.setRotation(rotation);

                AdjustModel adjustModel = new AdjustModel();
                adjustModel.setType("RotationL");
                adjustModel.setValue(rotation);
                adjustModelArrayList.add(adjustModel);
                if (adjustModelArrayListRedo.size() == 0) {
                    redo.setVisibility(View.GONE);
                } else {
                    redo.setVisibility(View.VISIBLE);
                }
                if (adjustModelArrayList.size() == 0) {
                    undo.setVisibility(View.GONE);
                } else {
                    undo.setVisibility(View.VISIBLE);
                }
            }
        });

        RR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rotation < 270) {
                    rotation = rotation + 90;

                } else {
                    rotation = 0;
                }
                Log.e("rotation", "rotation value: " + rotation);

                videoView.setRotation(rotation);
                AdjustModel adjustModel = new AdjustModel();
                adjustModel.setType("RotationR");
                adjustModel.setValue(rotation);

                adjustModelArrayList.add(adjustModel);
                if (adjustModelArrayListRedo.size() == 0) {
                    redo.setVisibility(View.GONE);
                } else {
                    redo.setVisibility(View.VISIBLE);
                }
                if (adjustModelArrayList.size() == 0) {
                    undo.setVisibility(View.GONE);
                } else {
                    undo.setVisibility(View.VISIBLE);
                }
            }
        });
        FV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.meest.metme.camera2.AdjustModel adjustModel = new com.meest.metme.camera2.AdjustModel();
                adjustModel.setType("flipV");
                if (isFlipVertical) {
                    isFlipVertical = false;
                    videoView.setScaleY(1f);
                    adjustModel.setValue(1);
                } else {
                    isFlipVertical = true;
                    videoView.setScaleY(-1f);
                    adjustModel.setValue(-1);

                }

                adjustModelArrayList.add(adjustModel);
                if (adjustModelArrayListRedo.size() == 0) {
                    redo.setVisibility(View.GONE);
                } else {
                    redo.setVisibility(View.VISIBLE);
                }
                if (adjustModelArrayList.size() == 0) {
                    undo.setVisibility(View.GONE);
                } else {
                    undo.setVisibility(View.VISIBLE);
                }

            }
        });
        FH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.meest.metme.camera2.AdjustModel adjustModel = new com.meest.metme.camera2.AdjustModel();
                adjustModel.setType("flipH");
                if (isFlipHorizontal) {
                    isFlipHorizontal = false;
                    videoView.setScaleX(1f);
                    adjustModel.setValue(1);
                } else {
                    isFlipHorizontal = true;
                    videoView.setScaleX(-1f);
                    adjustModel.setValue(-1);
                }

                adjustModelArrayList.add(adjustModel);
                if (adjustModelArrayListRedo.size() == 0) {
                    redo.setVisibility(View.GONE);
                } else {
                    redo.setVisibility(View.VISIBLE);
                }
                if (adjustModelArrayList.size() == 0) {
                    undo.setVisibility(View.GONE);
                } else {
                    undo.setVisibility(View.VISIBLE);
                }


            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoFun();
            }
        });

        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedoFun();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVideo();
//                FlipTypeRotation();
            }
        });
    }

    public void undoFun() {
        if (adjustModelArrayList.size() >= 1) {
            com.meest.metme.camera2.AdjustModel adjustModel = adjustModelArrayList.get(adjustModelArrayList.size() - 1);
            Log.e("data undo", "value: " + adjustModel.getType() + "::" + adjustModel.getValue());
            if (adjustModel.getType().equalsIgnoreCase("RotationR")) {
                rotation = adjustModel.getValue();
                if (rotation == 0) {
                    rotation = 270;
                } else {
                    rotation = rotation - 90;
                }
                videoView.setRotation(rotation);
            }
            if (adjustModel.getType().equalsIgnoreCase("RotationL")) {
                rotation = adjustModel.getValue();
                if (rotation < 270) {
                    rotation = rotation + 90;

                } else {
                    rotation = 0;
                }
                videoView.setRotation(rotation);
            }
            if (adjustModel.getType().equalsIgnoreCase("FlipV")) {
                int flipVal = adjustModel.getValue();
                if (flipVal == 1) {
                    isFlipVertical = true;
                    videoView.setScaleY(-1f);
                }
                if (flipVal == -1) {
                    isFlipVertical = false;
                    videoView.setScaleY(1f);
                }
            }
            if (adjustModel.getType().equalsIgnoreCase("FlipH")) {
                int flipVal = adjustModel.getValue();
                if (flipVal == 1) {
                    isFlipHorizontal = true;
                    videoView.setScaleX(-1f);
                }
                if (flipVal == -1) {
                    isFlipHorizontal = false;
                    videoView.setScaleX(1f);
                }


            }
            adjustModelArrayListRedo.add(adjustModel);
            if (adjustModelArrayListRedo.size() == 0) {
                redo.setVisibility(View.GONE);
            } else {
                redo.setVisibility(View.VISIBLE);
            }
            adjustModelArrayList.remove(adjustModelArrayList.size() - 1);
            if (adjustModelArrayList.size() == 0) {
                undo.setVisibility(View.GONE);
            } else {
                undo.setVisibility(View.VISIBLE);
            }
        }
    }

    public void RedoFun() {
        if (adjustModelArrayListRedo.size() >= 1) {
            com.meest.metme.camera2.AdjustModel adjustModel = adjustModelArrayListRedo.get(adjustModelArrayListRedo.size() - 1);
            Log.e("data redo", "value: " + adjustModel.getType() + "::" + adjustModel.getValue());
            if (adjustModel.getType().equalsIgnoreCase("RotationR")) {
                rotation = adjustModel.getValue();

                videoView.setRotation(rotation);
                Log.e("data redo", "value: " + rotation);
            }
            if (adjustModel.getType().equalsIgnoreCase("RotationL")) {
                rotation = adjustModel.getValue();
                videoView.setRotation(rotation);
                Log.e("data redo", "value: " + rotation);
            }
            if (adjustModel.getType().equalsIgnoreCase("FlipV")) {
                int flipVal = adjustModel.getValue();
                Log.e(":361", "361: " + isFlipVertical);
                if (flipVal == 1) {
                    isFlipVertical = false;

                }
                if (flipVal == -1) {
                    isFlipVertical = true;
                }
                videoView.setScaleY(flipVal);
            }
            if (adjustModel.getType().equalsIgnoreCase("FlipH")) {
                int flipVal = adjustModel.getValue();
                Log.e(":373", "373: " + isFlipHorizontal);
                if (flipVal == 1) {
                    isFlipHorizontal = false;
                }
                if (flipVal == -1) {
                    isFlipHorizontal = true;
                }
                videoView.setScaleX(flipVal);


            }

            adjustModelArrayListRedo.remove(adjustModelArrayListRedo.size() - 1);
            adjustModelArrayList.add(adjustModel);
            if (adjustModelArrayListRedo.size() == 0) {
                redo.setVisibility(View.GONE);
            } else {
                redo.setVisibility(View.VISIBLE);
            }
            if (adjustModelArrayList.size() == 0) {
                undo.setVisibility(View.GONE);
            } else {
                undo.setVisibility(View.VISIBLE);
            }
        }
    }

    public void resetAll() {
        rotation = 0;
        isFlipHorizontal = false;
        isFlipVertical = false;
        videoView.setRotation(rotation);
        videoView.setScaleX(1f);
        videoView.setScaleY(1f);
        adjustModelArrayList.clear();
        adjustModelArrayListRedo.clear();
        redo.setVisibility(View.GONE);
        undo.setVisibility(View.GONE);

    }

    public void progressBar(){
        mProgressDialog = new ProgressDialog(com.meest.metme.camera2.AdjustActivity.this);
        mProgressDialog.setMessage("Please Wait");
        mProgressDialog.setTitle("Saving..");
        mProgressDialog.setIndeterminate(false);
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void saveVideo() {

        Log.e("rotation value", "rortation: " + rotation + "::" + isFlipHorizontal + "::" + isFlipVertical);

        Mp4Composer mp4Composer = new Mp4Composer(videoURL, videoURL1)
                .listener(new Mp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
//                        0.878787878;
                        int prog= (int) (progress*100);
                        Log.e("TAG", "onProgress = " + progress+"::"+prog);

                        mProgressDialog.setProgress(prog);
//                        publishProgress((int) (total * 100 / fileLength));
                    }

                    @Override
                    public void onCompleted() {
                        mProgressDialog.dismiss();
                        Log.e("TAG", "onCompleted()");
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("videoPath", videoURL1);
                        setResult(RESULT_OK,resultIntent);
//                        File file=new File(videoURL1);
                        finish();
//                        Intent intent = new Intent(com.meest.metme.camera2.AdjustActivity.this, FinalPreview.class);
//                        intent.putExtra("videoPath", videoURL1);
//                        startActivity(intent);

//                        FlipTypeRotation();

//                        CropVideo();
                    }

                    @Override
                    public void onCanceled() {

                    }

                    @Override
                    public void onCurrentWrittenVideoTime(long timeUs) {

                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Log.e("TAG", "onFailed() " + exception.getMessage());
                    }
                });
        Log.e("rotation value", "rortation: " + rotation);
        if (rotation == 0) {
            mp4Composer.rotation(Rotation.NORMAL);
        }
        if (rotation == 90) {
            mp4Composer.rotation(Rotation.ROTATION_90);
        }
        if (rotation == 180) {
            mp4Composer.rotation(Rotation.ROTATION_180);
        }
        if (rotation == 270) {
            mp4Composer.rotation(Rotation.ROTATION_270);
        }
        Log.e("crop", "crop: " +  "::" + mStartPosition + "::" + mEndPosition);


        mp4Composer.trim(mStartPosition*1000,mEndPosition*1000);
        mp4Composer.flipHorizontal(isFlipHorizontal)
                .flipVertical(isFlipVertical)
                .start();
        progressBar();
    }

    public void CropVideo(){
        Log.e("crop", "crop: " +  "::" + mStartPosition + "::" + mEndPosition);
        Mp4Composer mp4Composer = new Mp4Composer(videoURL1, videoURL)

                .listener(new Mp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
//                        Log.e("TAG", "onProgress = " + progress);
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("TAG", "onCompleted()");
//                        Intent intent = new Intent(com.meest.metme.camera2.AdjustActivity.this, FinalPreview.class);
//                        intent.putExtra("videoPath", videoURL);
//                        startActivity(intent);


                    }

                    @Override
                    public void onCanceled() {

                    }

                    @Override
                    public void onCurrentWrittenVideoTime(long timeUs) {

                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Log.e("TAG", "onFailed() " + exception.getMessage());
                    }
                });
        Log.e("crop", "crop: " +  "::" + mStartPosition + "::" + mEndPosition);


        mp4Composer.trim(mStartPosition*1000,mEndPosition*1000)
                .start();
    }

    public void FlipTypeRotation() {
        Mp4Composer mp4Composer = new Mp4Composer(videoURL1, videoURL)

                .listener(new Mp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
//                        Log.e("TAG", "onProgress = " + progress);
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("TAG", "onCompleted()");
//                        Intent intent = new Intent(com.meest.metme.camera2.AdjustActivity.this, FinalPreview.class);
//                        intent.putExtra("videoPath", videoURL);
//                        startActivity(intent);


                    }

                    @Override
                    public void onCanceled() {

                    }

                    @Override
                    public void onCurrentWrittenVideoTime(long timeUs) {

                    }

                    @Override
                    public void onFailed(Exception exception) {
                        Log.e("TAG", "onFailed() " + exception.getMessage());
                    }
                });
        Log.e("rotation value", "rortation: " + rotation + "::" + isFlipHorizontal + "::" + isFlipVertical);


        mp4Composer.flipHorizontal(isFlipHorizontal)
                .flipVertical(isFlipVertical)
                .start();
    }



    private void initView() {
        videoView = (FastVideoView) findViewById(R.id.textureView);

        RL = (ImageView) findViewById(R.id.RL);
        RR = (ImageView) findViewById(R.id.RR);
        FH = (ImageView) findViewById(R.id.FH);
        FV = (ImageView) findViewById(R.id.FV);
        close = (ImageView) findViewById(R.id.close);
        save = (ImageView) findViewById(R.id.save);
        exoLayout = findViewById(R.id.exoLayout);
        time = (TextView) findViewById(R.id.time);
        play = (ImageView) findViewById(R.id.play);
        reset = (TextView) findViewById(R.id.reset);
        undo = (ImageView) findViewById(R.id.undo);
        redo = (ImageView) findViewById(R.id.redo);

        tileView = (TileView) findViewById(R.id.timeLineView);
        mCustomRangeSeekBarNew = (CustomRangeSeekBar) findViewById(R.id.timeLineBar);
        seekBarVideo = (SeekBar) findViewById(R.id.seekBarVideo);
    }

    private void setupVideoEdit() {
        tileView.post(new Runnable() {
            @Override
            public void run() {
                setBitmap(Uri.parse(videoURL));
            }
        });
        mCustomRangeSeekBarNew.addOnRangeSeekBarListener(new OnRangeSeekBarChangeListener() {
            @Override
            public void onCreate(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                // Do nothing
            }

            @Override
            public void onSeek(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                onSeekThumbs(index, value);
            }

            @Override
            public void onSeekStart(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                if (videoView != null) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    seekBarVideo.setProgress(0);
                    videoView.seekTo(mStartPosition * 1000);
                    videoView.pause();
                }
            }

            @Override
            public void onSeekStop(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                onStopSeekThumbs();
            }
        });

    }
    private void setBitmap(Uri mVideoUri) {
        tileView.setVideo(mVideoUri);
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (seekBarVideo.getProgress() >= seekBarVideo.getMax()) {
                seekBarVideo.setProgress((videoView.getCurrentPosition() - mStartPosition * 1000));
                videoView.seekTo(mStartPosition * 1000);
                videoView.pause();
                time.setText(milliSecondsToTimer(seekBarVideo.getProgress()) + "");
                seekBarVideo.setProgress(0);

            } else {
                time.setText(milliSecondsToTimer(seekBarVideo.getProgress()) + "");
                seekBarVideo.setProgress((videoView.getCurrentPosition() - mStartPosition * 1000));
                mHandler.postDelayed(this, 100);
            }
        }
    };

    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;
        String minutesString;


        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }

        finalTimerString = finalTimerString + minutesString + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private void setSeekBarPosition() {

        if (mDuration >= mMaxDuration) {
            mStartPosition = 0;
            mEndPosition = mMaxDuration;

            mCustomRangeSeekBarNew.setThumbValue(0, (mStartPosition * 100) / mDuration);
            mCustomRangeSeekBarNew.setThumbValue(1, (mEndPosition * 100) / mDuration);

        } else {
            mStartPosition = 0;
            mEndPosition = mDuration;
        }


        mTimeVideo = mDuration;
        mCustomRangeSeekBarNew.initMaxWidth();
        seekBarVideo.setMax(mMaxDuration * 1000);
        videoView.seekTo(mStartPosition * 1000);

        String mStart = mStartPosition + "";
        if (mStartPosition < 10)
            mStart = "0" + mStartPosition;

        int startMin = Integer.parseInt(mStart) / 60;
        int startSec = Integer.parseInt(mStart) % 60;

        String mEnd = mEndPosition + "";
        if (mEndPosition < 10)
            mEnd = "0" + mEndPosition;

        int endMin = Integer.parseInt(mEnd) / 60;
        int endSec = Integer.parseInt(mEnd) % 60;
        Log.e("data 186","186Line: "+String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));

//        txtVideoTrimSeconds.setText(String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));
    }

    private void onSeekThumbs(int index, float value) {
        switch (index) {
            case BarThumb.LEFT: {
                mStartPosition = (int) ((mDuration * value) / 100L);
                videoView.seekTo(mStartPosition * 1000);
                break;
            }
            case BarThumb.RIGHT: {
                mEndPosition = (int) ((mDuration * value) / 100L);
                break;
            }
        }
        mTimeVideo = (mEndPosition - mStartPosition);
        seekBarVideo.setMax(mTimeVideo * 1000);
        seekBarVideo.setProgress(0);
        videoView.seekTo(mStartPosition * 1000);

        String mStart = mStartPosition + "";
        if (mStartPosition < 10)
            mStart = "0" + mStartPosition;

        int startMin = Integer.parseInt(mStart) / 60;
        int startSec = Integer.parseInt(mStart) % 60;

        String mEnd = mEndPosition + "";
        if (mEndPosition < 10)
            mEnd = "0" + mEndPosition;
        int endMin = Integer.parseInt(mEnd) / 60;
        int endSec = Integer.parseInt(mEnd) % 60;

        Log.e("data 227","227Line: "+String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));
//        txtVideoTrimSeconds.setText(String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));

    }

    private void onStopSeekThumbs() {
//        mMessageHandler.removeMessages(SHOW_PROGRESS);
//        videoView.pause();
//        mPlayView.setVisibility(View.VISIBLE);
    }

}