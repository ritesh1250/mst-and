package com.meest.videomvvmmodule.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.meest.R;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String message = "message";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: ");
    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().containsKey(message)) {
            Log.d(TAG, "onMessageReceived: " + remoteMessage.getData().get(message));
            String msg = remoteMessage.getData().get(message);
            if (msg != null && msg.equalsIgnoreCase("delete")) {
                new SessionManager(getApplicationContext()).clear();
            } else {
                sendNotification(remoteMessage.getData());
            }
        }
    }


    private void sendNotification(Map<String, String> data) {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.notification_icon);

        int requestID = (int) System.currentTimeMillis();

        Intent intent = new Intent(this, MainVideoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID, intent, PendingIntent.FLAG_UPDATE_CURRENT
                | PendingIntent.FLAG_ONE_SHOT);
        String channelId = "01";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("BubbleTok")
                .setContentText(data.containsKey(message) ? data.get(message) : "Hello...!")
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                .setContentIntent(pendingIntent)
                .setContentInfo("Hello")
                .setLargeIcon(icon)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.notification_icon);

        try {
            URL url = new URL(data.get("picture_url"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            Bitmap bigPicture = BitmapFactory.decodeStream(inputStream);
            notificationBuilder.setStyle(
                    new NotificationCompat.BigPictureStyle().bigPicture(bigPicture)
            );

        } catch (IOException e) {
            e.printStackTrace();
        }


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String name = "Channel_001";
            String description = "Channel Description";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}