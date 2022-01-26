package com.example.firebasechatapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasechatapplication.R
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import java.lang.Exception
import java.lang.RuntimeException


class AudioCall : AppCompatActivity() {
    var appId:String="9241e157864c4536989fde803607d02a"
    var token:String="0069241e157864c4536989fde803607d02aIAB49V3UYSKFYgj2j46qe70wAiFdqDL80P+9N8RyCTjgwqgy6Q0AAAAAEADC8VeAazLsYQEAAQBfMuxh"
    lateinit var mRtcEngine: RtcEngine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_call)




    }

    private fun initializeAndJoinChannel() {
        try {
            mRtcEngine = RtcEngine.create(baseContext, appId, mRtcEventHandler)
        } catch (e: Exception) {
            throw RuntimeException("Check the error")
        }
        mRtcEngine.joinChannel(token, "arun", "", 0)
    }

    private val mRtcEventHandler = object : IRtcEngineEventHandler() {


        
    }




    fun makeacall()
    {



    }

}