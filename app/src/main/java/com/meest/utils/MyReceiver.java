package com.meest.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.meest.metme.jitsi.IncomingCallActivity;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent incoming = new Intent(context, IncomingCallActivity.class);
        incoming.putExtra("data", intent.getStringExtra("data"));
        incoming.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
        incoming.setAction(Intent.ACTION_MAIN);
        incoming.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(incoming);
    }
}
