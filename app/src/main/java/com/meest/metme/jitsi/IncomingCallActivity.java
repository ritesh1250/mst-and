package com.meest.metme.jitsi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.meest.R;
import com.meest.utils.IncomingCall;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class IncomingCallActivity extends AppCompatActivity {

    ImageView image;
    LinearLayout accept;
    LinearLayout decline;
    static MediaPlayer mediaPlayer;
    //    Vibrator vib;
    TextView name, callType;
    IncomingCall incomingCall;

    public static MediaPlayer getMediaPlayer(Context context) {
        /*if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.tone);
        }*/
        mediaPlayer = MediaPlayer.create(context, R.raw.tone);
        return mediaPlayer;
    }

    public static IncomingCallActivity context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_incoming_call);

        context=this;
        image = findViewById(R.id.image);
        accept = findViewById(R.id.accept);
        decline = findViewById(R.id.decline);
        name = findViewById(R.id.name);
        callType = findViewById(R.id.callType);

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("data");
        incomingCall = gson.fromJson(strObj, IncomingCall.class);


        name.setText(incomingCall.getFullName());
        callType.setText(incomingCall.getDescription());


        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("profilePicture"))/*.override(10, 10) */ //just set override like this
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(image);
        tone();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
//                vib.cancel();
              /*  funJitsiConfig(incomingCall.getUrl() + "/", incomingCall.getJitsiToken());
                // Build options object for joining the conference. The SDK will merge the default
                // one we set earlier and this one when joining.
                JitsiMeetConferenceOptions options
                        = new JitsiMeetConferenceOptions.Builder()
                        .setRoom(incomingCall.getCallRoomId())
                        // Settings for audio and video
                        .setAudioMuted(false)
                        .setVideoMuted(incomingCall.getVideo().equals("true"))
                        .setAudioOnly(false)
                        .build();
                // Launch the new activity with the given options. The launch() method takes care
                // of creating the required Intent and passing the options.
                JitsiMeetActivity.launch(IncomingCallActivity.this, options);*/

                Intent intent = new  Intent(context, JitsiCallActivity.class);
                intent.putExtra("serverURL",incomingCall.getUrl());
                intent.putExtra("jwtToken",incomingCall.getJitsiToken());
                intent.putExtra("callRoomId",incomingCall.getCallRoomId());
                intent.putExtra("isVideo",incomingCall.getVideo());
                intent.putExtra("isAudio",true);
                intent.putExtra("name",incomingCall.getFullName());
                intent.putExtra("roomId",incomingCall.getId());
                intent.putExtra("toUserId",incomingCall.getUserId());
                context.startActivity(intent);

                finish();
            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
//                vib.cancel();
                finish();
            }
        });


    }

    public void tone() {

        mediaPlayer=getMediaPlayer(this);
        getMediaPlayer(this).start();
//        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        vib.vibrate(111110000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getMediaPlayer(getApplicationContext()).stop();
//                vib.cancel();
                finish();
            }
        }, 20000);
    }


    private void funJitsiConfig(String url, String jwtText) {
        // Initialize default options for Jitsi Meet conferences.
        URL serverURL;
        try {
            // When using JaaS, replace "https://meet.jit.si" with the proper serverURL
            // serverURL = new URL("https://meet.jit.si");
            //  serverURL = new URL("https://jistinew.meest4bharat.net/");
            serverURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                // When using JaaS, set the obtained JWT here
                .setToken(jwtText)
                // Different features flags can be set
                // .setFeatureFlag("toolbox.enabled", false)
                // .setFeatureFlag("filmstrip.enabled", false)
                .setFeatureFlag("chat.enabled",false)
                .setFeatureFlag("invite.enabled",false)
                .setFeatureFlag("meeting-name.enabled",false)
                //////
                .setFeatureFlag("raise-hand.enabled", false)
                .setFeatureFlag("overflow-menu.enabled", true)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);

    }
}