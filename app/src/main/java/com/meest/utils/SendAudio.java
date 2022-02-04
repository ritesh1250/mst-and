package com.meest.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;

import com.meest.R;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SendAudio {

    private static String mFileName = null;
    private MediaRecorder mRecorder = null;

    Context context;

    public SendAudio(Context context, EditText message_field) {

        mFileName = context.getExternalCacheDir().getAbsolutePath();
        mFileName += "/audiorecordtest.mp3";

    }

    public SendAudio() {

    }


    // this function will start the recrding
    private void startRecording() {

        if(mRecorder!=null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder=null;
        }

        mRecorder = new MediaRecorder();

        if(mRecorder!=null)
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        if(mRecorder!=null)
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        if(mRecorder!=null)
            mRecorder.setOutputFile(mFileName);

        if(mRecorder!=null)
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            if(mRecorder!=null)
                mRecorder.prepare();
        } catch (IOException e) {
            Log.e("resp", "prepare() failed");
        }
        if(mRecorder!=null)
            mRecorder.start();


    }



    // stop the recording and then call a function to upload the audio file into database

    public void stopRecording() {
        stop_timer_without_recoder();
        if(mRecorder!=null ) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder=null;
            Runbeep("stop");
            UploadAudio();
        }
    }

    Handler handler;
    Runnable runnable;
    public void Runbeep(final String action){

        // within 700 milisecond the timer will be start
        handler=new Handler();
        if(action.equals("start")) {
           // message_field.setText("00:00");
            runnable = new Runnable() {
                @Override
                public void run() {
                    start_timer();
                }
            };

            handler.postDelayed(runnable, 700);
        }


        // this will run a beep sound
        final MediaPlayer beep = MediaPlayer.create(context, R.raw.hello);
        beep.setVolume(100,100);
        beep.start();
        beep.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                beep.release();

                // if our action is start a recording the recording will start
                if(action.equals("start"))
                    startRecording();
            }
        });
    }


    // this method will upload audio  in firebase database
    public void UploadAudio(){

        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);




        Uri uri = Uri.fromFile(new File(mFileName));





    }



    CountDownTimer timer;
    public void start_timer() {

        timer=new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time=30000-millisUntilFinished;

                int min = (int) (time/1000) / 60;
                int sec = (int) (time/1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", min, sec);
              //  message_field.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                stopRecording();
            //    message_field.setText(null);
            }
        };

        timer.start();
    }


    // this  will stop timer when audio file have some data
    public void stop_timer(){

        if(mRecorder!=null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder=null;
        }

        if(handler!=null && runnable!=null)
            handler.removeCallbacks(runnable);

      //  message_field.setText(null);

        if(timer!=null){
            timer.cancel();
        //    message_field.setText(null);
        }

    }


    // this will stop timer when audio file does not have data
    public void stop_timer_without_recoder(){

        if(handler!=null && runnable!=null)
            handler.removeCallbacks(runnable);

     //   message_field.setText(null);

        if(timer!=null){
            timer.cancel();
           // message_field.setText(null);
        }

    }




}
