package com.meest.metme.jitsi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.facebook.react.modules.core.PermissionListener
import com.meest.Meeast
import com.meest.meestbhart.utilss.SharedPrefreances
import com.meest.metme.viewmodels.ChatBoatViewModel
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.jitsi.meet.sdk.*
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


// Example
//
class JitsiCallActivity : FragmentActivity(), JitsiMeetActivityInterface {
    private var mSocket: Socket? = null
    private var view: JitsiMeetView? = null
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        JitsiMeetActivityDelegate.onActivityResult(
            this, requestCode, resultCode, data
        )
    }

    override fun onBackPressed() {
        JitsiMeetActivityDelegate.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = JitsiMeetView(this)
        val app = application as Meeast
        mSocket = app.socket
     //   mSocket!!.on("connected", onConnect)
       // mSocket!!.on("callEnd", endCall)
        mSocket!!.on("headsRefresh", headsRefresh)
        mSocket!!.connect()
       // val serverURL = URL("https://" + intent.getStringExtra("serverURL") + "/")
        val serverURL = URL("https://meet.jit.si");
        val jwtToken = intent.getStringExtra("jwtToken")
        val callRoomId = intent.getStringExtra("callRoomId")
        val roomId = intent.getStringExtra("roomId")
        val isVideo = intent.getStringExtra("isVideo")
        val isAudio = intent.getBooleanExtra("isAudio", false)
        val name = intent.getStringExtra("name")
        val toUserId = intent.getStringExtra("toUserId")
        val builder = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)
            .setRoom(callRoomId)
            .setFeatureFlag("video-mute.enabled", isVideo.toBoolean())
        //  builder.setToken(jwtToken)
        builder.setAudioMuted(isAudio)
        builder.setVideoMuted(!isVideo.toBoolean())
       // builder.setAudioOnly(!isVideo.toBoolean())
        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = name
        builder.setUserInfo(jitsiMeetUserInfo)
        builder.setWelcomePageEnabled(false)
        val options = builder.build()
        view!!.join(options)
        view!!.listener = object : JitsiMeetViewListener {
            override fun onConferenceJoined(map: Map<String, Any>) {
            }

            override fun onConferenceTerminated(map: Map<String, Any>) {
                val jsonObject = JSONObject()
              //  mSocket!!.emit("headsRefresh", jsonObject)
                try {
                    jsonObject.put(
                        "userId",
                        SharedPrefreances.getSharedPreferenceString(
                            applicationContext,
                            SharedPrefreances.ID
                        )
                    )
                    jsonObject.put(
                        "toUserId", toUserId
                    )
                    jsonObject.put("roomId", roomId)
                    mSocket!!.emit("endCall", jsonObject)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
               // onBackPressed()

            }

            override fun onConferenceWillJoin(map: Map<String, Any>) {
            }
        }

        setContentView(view)
    }

    fun funJitsiConfig(
        jwtToken: String?,
        serverURL: URL,
        roomId: String?,
        isVideo: String?,
        isAudio: Boolean
    ) {
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)
            // .setToken(jwtToken)
            //                .setFeatureFlag("chat.enabled", false)
            //                .setFeatureFlag("invite.enabled", false)
            //                .setFeatureFlag("meeting-name.enabled", false)
            //                ///////
            //                .setFeatureFlag("raise-hand.enabled", false)
            //                .setFeatureFlag("overflow-menu.enabled", true)
            .setWelcomePageEnabled(false)
            .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
        val options = JitsiMeetConferenceOptions.Builder()
            .setRoom(roomId) // Settings for audio and video
            .setAudioMuted(isAudio)
            .setVideoMuted(isVideo.toBoolean()) // .setAudioOnly(false)
            .build()
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        //JitsiMeetActivity.launch(ChatBoatViewModel.context, options)
        view!!.join(options)
    }

    override fun onDestroy() {
        super.onDestroy()
        view!!.dispose()
        view = null
        JitsiMeetActivityDelegate.onHostDestroy(this)
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        JitsiMeetActivityDelegate.onNewIntent(intent)
    }

    override fun requestPermissions(
        permissions: Array<String>,
        requestCode: Int,
        listener: PermissionListener
    ) {
        JitsiMeetActivityDelegate.requestPermissions(this, permissions, requestCode, listener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
        JitsiMeetActivityDelegate.onHostResume(this)
    }

    override fun onStop() {
        super.onStop()
        JitsiMeetActivityDelegate.onHostPause(this)
    }

    private val headsRefresh = Emitter.Listener { args ->
        this@JitsiCallActivity.runOnUiThread {
            try {
                val data = args[0] as JSONObject
                onBackPressed()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val endCall = Emitter.Listener { args ->
        this@JitsiCallActivity.runOnUiThread {
            try {
                val data = args[0] as JSONObject
                onBackPressed()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    var onConnect =
        Emitter.Listener { args ->
            this@JitsiCallActivity.runOnUiThread(Runnable {
                 val data = args[0] as JSONObject
                try {
                    Log.w("demodemo", data.getString("msg"))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                val jsonObject = JSONObject()
                try {
                    jsonObject.put(
                        "userId",
                        SharedPrefreances.getSharedPreferenceString(
                            applicationContext,
                            SharedPrefreances.ID
                        )
                    )
                    jsonObject.put(
                        "name", SharedPrefreances.getSharedPreferenceString(
                            applicationContext, SharedPrefreances.F_NAME
                        ) + " " + SharedPrefreances.getSharedPreferenceString(
                            applicationContext, SharedPrefreances.L_NAME
                        )
                    )
                    jsonObject.put("isGroup", false)
                    mSocket!!.emit("createSession", jsonObject)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            })
        }
}