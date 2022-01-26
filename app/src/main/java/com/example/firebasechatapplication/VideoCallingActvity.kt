package com.example.firebasechatapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechatapplication.databinding.VideocalllayoutBinding
import com.google.firebase.database.R
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas

class VideoCallingActvity : AppCompatActivity() {
    lateinit var mRtcEngine:RtcEngine
    lateinit var binding :VideocalllayoutBinding
    val appId="arun"
    val token="0069241e157864c4536989fde803607d02aIAC4pX/zvZtuKBXKJBjb0//bdyewXJbCOiXNsaqYYJPf1agy6Q0AAAAAEACBgxuC5GXpYQEAAQDbZelh"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)
        {
        requestPermissions(arrayOf(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO),100)
        }
        initializeAndJoinChannel()

    }


    // Kotlin
    private fun initializeAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(this, appId, mRtcEventHandler)
        } catch (e: Exception) {

        }

        // By default, video is disabled, and you need to call enableVideo to start a video stream.
        mRtcEngine!!.enableVideo()


        // Call CreateRendererView to create a SurfaceView object and add it as a child to the FrameLayout.
        val localFrame = RtcEngine.CreateRendererView(this)
        binding.localVideoViewContainer.addView(localFrame)
        // Pass the SurfaceView object to Agora so that it renders the local video.
        mRtcEngine!!.setupLocalVideo(VideoCanvas(localFrame, VideoCanvas.RENDER_MODE_FIT, 0))

        // Join the channel with a token.
        mRtcEngine!!.joinChannel(token, "arun", "", 0)
    }

    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        // Listen for the remote user joining the channel to get the uid of the user.
        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread {
                // Call setupRemoteVideo to set the remote video view after getting uid from the onUserJoined callback.
                setupRemoteVideo(uid)
            }
        }
    }



    private fun setupRemoteVideo(uid: Int) {
        val remoteFrame = RtcEngine.CreateRendererView(this)
        remoteFrame.setZOrderMediaOverlay(true)
        binding.remoteVideoViewContainer.addView(remoteFrame)
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(remoteFrame, VideoCanvas.RENDER_MODE_FIT, uid))
    }

    override fun onDestroy() {
        super.onDestroy()
        mRtcEngine?.leaveChannel()
        RtcEngine.destroy()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       if (requestCode==100)
       {
           Toast.makeText(this,"permission granted",Toast.LENGTH_SHORT).show()
       }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}