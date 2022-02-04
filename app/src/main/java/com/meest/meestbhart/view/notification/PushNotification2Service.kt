package com.meest.meestbhart.view.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.meest.MainActivity
import com.meest.R
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class PushNotification2Service : FirebaseMessagingService() {
    private val ADMIN_CHANNEL_ID = "admin_channel"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        var title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        if (title!!.indexOf("9876") != -1) {
            val intent = Intent(this, MainActivity::class.java)
            title = title.replace("9876", "")
            intent.putExtra("type", 2)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            notify(title, message!!, pendingIntent)

        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("type", 2)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            notify(title, message!!, pendingIntent)
        }

    }

    fun notify(title: String, message: String, pendingIntent: PendingIntent) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID: Int = Random().nextInt(3000)
        /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
      */


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }


        val largeIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

        val notificationSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)

        //Set notification color to match your app color template

        notificationManager.notify(notificationID, notificationBuilder.build())
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        val token = FirebaseInstanceId.getInstance().token

        // Once the token is generated, subscribe to topic with the userId
        FirebaseMessaging.getInstance().subscribeToTopic(token!!)
        Log.i("alhaj", "onTokenRefresh completed with token: $token")
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val adminChannelName: CharSequence = "New notification"
        val adminChannelDescription = "Device to devie notification"
        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(
                ADMIN_CHANNEL_ID,
                adminChannelName,
                NotificationManager.IMPORTANCE_HIGH
        )
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(adminChannel)
    }
}