package com.meest.Services.GalUpload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.meest.responses.InsertRecord;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GalUploadService extends Service {
    String fileName = "ImageList.txt";
    String fileName1 = "ClickedImageList.txt";
    File gpxfile,gpxfile1;
    File root;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String image_path;
    public GalUploadService(){

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        root = new File(Constant.createOutputPath(GalUploadService.this));
        sharedPreferences=getSharedPreferences("DATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String type = "etc";
        type = intent.getStringExtra("key");

        if(type.equals("GAL")) {
            String imagename = null;
            gpxfile = new File(root, fileName);
            for (int i = 0; i < 1; i++) {
                try {
                    imagename = readfile(gpxfile);

                } catch (Exception e) {
                }
                if (imagename != null && !imagename.isEmpty()) {


                    File file = new File(imagename);
                    if(file.exists() ) {
                        try {
                            image_path = Constant.compressImage(GalUploadService.this, imagename);
                            File cfile = new File(image_path);
                            Log.e("Gal Image", cfile+"");
                            uploadData(cfile,false,imagename,gpxfile);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

        }else if(type.equals("CLK")) {
            String imagename1 = null;
            gpxfile1 = new File(root, fileName1);
            for (int i = 0; i < 1; i++) {
                try {
                    imagename1 = readfile(gpxfile1);

                } catch (Exception e) {
                }
                if (imagename1 != null && !imagename1.isEmpty()) {
                    File file = new File(imagename1);
                    if(file.exists() ) {
                        try {
                            image_path = Constant.compressImage(GalUploadService.this, imagename1);
                            File cfile = new File(image_path);
                            Log.e("Click Image", cfile+"");
                            uploadData(cfile,false,imagename1,gpxfile1);
//                            s1[i] = image_path;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }else if(type.equals("REC")){
            String typeRec = intent.getStringExtra("path");
            File cfile = new File(typeRec);
            uploadData(cfile,true,"imagename",gpxfile);
        }

        return super.onStartCommand(intent, flags, startId);

    }


    public void removeLineFromFile(File inFile, String lineToRemove) {

        try {
            if (!inFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }
            File tempFile = new File(inFile.getAbsolutePath()+".tmp");
            BufferedReader br = new BufferedReader(new FileReader(inFile));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            String line ;
            while ((line = br.readLine()) != null) {
                if (!line.equals(lineToRemove)) {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();
            if (!inFile.delete()) {
                return;
            }
            if (!tempFile.renameTo(inFile)){}

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public String readfile(File file){
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            while(sc.hasNextLine()){

                String temp = sc.nextLine();
//                Log.e("File Name",temp+"");
                if(temp != null && !temp.isEmpty()){
                    return temp;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }



    private void uploadData(File file, boolean rec, String imagename1, File gpx) {
        RequestBody requestFile,mobile,userID,type;
        MultipartBody.Part body;
        if (rec) {
            Log.e("Audio upload start", file.getAbsolutePath());
            requestFile = RequestBody.create(MediaType.parse("file/*"), file);
            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            type = RequestBody.create(MediaType.parse("text/plain"), "audio");
        } else {
            requestFile = RequestBody.create(MediaType.parse("file/*"), file);
            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            type = RequestBody.create(MediaType.parse("text/plain"), "image");
        }
        userID = RequestBody.create(MediaType.parse("text/plain"), SharedPrefreances.getSharedPreferenceString(GalUploadService.this, SharedPrefreances.ID));
        mobile = RequestBody.create(MediaType.parse("text/plain"), SharedPrefreances.getSharedPreferenceString(GalUploadService.this, SharedPrefreances.MOBILE));
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<InsertRecord> call = webApi.insertRecord(map, body,type,mobile,userID);
        call.enqueue(new Callback<InsertRecord>() {
            @Override
            public void onResponse(Call<InsertRecord> call, Response<InsertRecord> response) {
                try {
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        if (rec){
                            Log.e("Audio upload complete", file.getAbsolutePath());
                            file.delete();
                            stopSelf();
                        }else {

                            file.delete();
                            removeLineFromFile(gpx, imagename1);
                            stopSelf();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<InsertRecord> call, Throwable t) {
                Log.e("Audio error  ", t.getMessage());
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
