package com.meest.Services.GalUpload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.annotation.Nullable;

import com.meest.meestbhart.utilss.Constant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ClickedPicUpload extends Service {
    TelephonyManager telephonyManager;
    String fileName = "ClickedImageList.txt";
    File gpxfile;
    File root;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";


    public ClickedPicUpload(){}
    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        root = new File(Constant.createOutputPath(ClickedPicUpload.this));
        telephonyManager=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        sharedPreferences=getSharedPreferences("ISFRIST", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        gpxfile = new File(root, fileName);
        Log.e("Click Image", gpxfile+"");
        try {
            gpxfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Date Modified = new Date(root.lastModified());
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDateString = formatter.format(Modified);
        String nowdate=formatter.format(now);


        Date lastModified=null,checked=null;
        String date= sharedPreferences.getString("checkeddate",nowdate);
        try {
            lastModified = formatter.parse(formattedDateString);
            checked=formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(lastModified.after(checked)){
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();
            try {

                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        Date date1 = new Date(file.lastModified());
                        String filedate = formatter.format(date1);
                        Date ffiledate=null;
                        try {
                            ffiledate = formatter.parse(filedate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (ffiledate.after(checked))
                            if(!file.getName().equals(fileName) && !file.getName().equals("ImageList.txt")) {
                                generateNoteOnSD(gpxfile + "/" + file.getName());
                            }
                    }
                }

            }catch (Exception e){
            }
        }
        editor.putString("checkeddate",formattedDateString);
        editor.commit();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(null != activeNetwork) {
            if (gpxfile.exists()) {
                double bytes = gpxfile.length();
                Log.e("Click Image", bytes+"");
                if (bytes != 0.0 ) {
                    Intent intentcall = new Intent(ClickedPicUpload.this, GalUploadService.class);
                    intentcall.putExtra("key","CLK");
                    startService(intentcall);
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }


    public void generateNoteOnSD(String sBody) {
        try
        {
            FileWriter writer = new FileWriter(gpxfile,true);
            writer.append(  sBody+'\n');
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
