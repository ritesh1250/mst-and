package com.meest.metme.service;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.meest.Meeast;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.jitsi.IncomingCallActivity;
import com.meest.metme.jitsi.JitsiCallActivity;
import com.meest.utils.IncomingCall;

import org.json.JSONObject;

import io.socket.client.Socket;

public class CallNotificationActionReceiver extends BroadcastReceiver {


    Context mContext;
    IncomingCall incomingCall;;
    Socket mSocket;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext=context;
        if (intent != null && intent.getExtras() != null) {

            String action ="",data="";
            action=intent.getStringExtra("ACTION_TYPE");
            data=intent.getStringExtra("data");
            Gson gson = new Gson();
            incomingCall = gson.fromJson(data, IncomingCall.class);
            if (action != null&& !action.equalsIgnoreCase("")) {
                performClickAction(context, action,data);
            }

            // Close the notification after the click action is performed.
            Intent iclose = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(iclose);
            context.stopService(new Intent(context, HeadsUpNotificationService.class));

        }


    }
    private void performClickAction(Context context, String action,String data) {
        if(action.equalsIgnoreCase("RECEIVE_CALL")) {
            Intent intentCallReceive = new Intent(mContext, JitsiCallActivity.class);
            intentCallReceive.putExtra("serverURL",incomingCall.getUrl());
            intentCallReceive.putExtra("jwtToken",incomingCall.getJitsiToken());
            intentCallReceive.putExtra("callRoomId",incomingCall.getCallRoomId());
            intentCallReceive.putExtra("isVideo",incomingCall.getVideo());
            intentCallReceive.putExtra("isAudio",true);
            intentCallReceive.putExtra("name",incomingCall.getFullName());
            intentCallReceive.putExtra("roomId",incomingCall.getId());
            intentCallReceive.putExtra("toUserId",incomingCall.getUserId());
            intentCallReceive.putExtra("Call", "incoming");
            intentCallReceive.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intentCallReceive);
          /*  if (checkAppPermissions()) {
                Gson gson = new Gson();
                incomingCall = gson.fromJson(data, IncomingCall.class);
                Intent intentCallReceive = new Intent(mContext, JitsiCallActivity.class);
                intentCallReceive.putExtra("serverURL",incomingCall.getUrl());
                intentCallReceive.putExtra("jwtToken",incomingCall.getJitsiToken());
                intentCallReceive.putExtra("callRoomId",incomingCall.getCallRoomId());
                intentCallReceive.putExtra("isVideo",incomingCall.getVideo());
                intentCallReceive.putExtra("isAudio",true);
                intentCallReceive.putExtra("name",incomingCall.getFullName());
                intentCallReceive.putExtra("roomId",incomingCall.getId());
                intentCallReceive.putExtra("toUserId",incomingCall.getUserId());
                intentCallReceive.putExtra("Call", "incoming");
                intentCallReceive.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(intentCallReceive);
            }
            else{
                Intent intent = new Intent(Meeast.context(), IncomingCallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("CallFrom","call from push");
                intent.putExtra("data",data);
                mContext.startActivity(intent);

            }*/
        }
        else if(action.equalsIgnoreCase("DIALOG_CALL")){

            // show ringing activity when phone is locked
            Intent intent = new Intent(Meeast.context(), IncomingCallActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("CallFrom","call from push");
            intent.putExtra("data",data);
            mContext.startActivity(intent);
        }

        else {
            try {
                Meeast app = (Meeast) mContext.getApplicationContext();
                mSocket = app.getSocket();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(mContext,SharedPrefreances.ID));
                jsonObject.put("toUserId",incomingCall.getUserId());
                jsonObject.put("roomId",incomingCall.getId());
                mSocket.emit("endCall", jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }
            context.stopService(new Intent(context, HeadsUpNotificationService.class));
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);
        }
    }

    private Boolean checkAppPermissions() {
        return hasReadPermissions() && hasWritePermissions() && hasCameraPermissions() && hasAudioPermissions();
    }

    private boolean hasAudioPermissions() {
        return (ContextCompat.checkSelfPermission(Meeast.context(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(Meeast.context(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(Meeast.context(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
    private boolean hasCameraPermissions() {
        return (ContextCompat.checkSelfPermission(Meeast.context(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }
}
