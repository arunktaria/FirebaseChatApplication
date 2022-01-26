package com.example.firebasechatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.firebasechatapplication.Utils.ToastBuilder
import com.example.firebasechatapplication.ViewModels.PostMessageViewModel
import com.example.firebasechatapplication.databinding.ActivityCallingScreenBinding
import com.google.firebase.database.FirebaseDatabase

class CallingScreen : AppCompatActivity() {
    lateinit var binding: ActivityCallingScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewmodel = ViewModelProvider(this).get(PostMessageViewModel::class.java)
        val database = FirebaseDatabase.getInstance().reference


        var rec = intent.getStringExtra("reciever").toString()
        var send = intent.getStringExtra("sender").toString()

        binding.callinguser.text=intent.getStringExtra("username")

        viewmodel.checkourIncomingCall(database, send + rec)

        viewmodel.tempourstatus.observe(this, Observer {
            if (it.toString().equals("inactive")) {
                this.finish()
            }
        })

        binding.endcall.setOnClickListener {
            database.child("videocall").child(rec + send).child("videocall")
                .setValue("inactive").addOnCompleteListener {
                    database.child("videocall").child(send + rec).child("videocall")
                        .setValue("inactive")
                  this.finish()

                }
        }


        binding.callpick.setOnClickListener {

            database.child("videocall").child(rec + send).child("videocall")
                .setValue("active").addOnCompleteListener {
                    database.child("videocall").child(send + rec).child("videocall")
                        .setValue("active")
                }
            this.finish()
            val intent = (Intent(MessageHandlerActivity@ this, VideoCallActivitytemp::class.java))
            intent.putExtra("sender", send)
            intent.putExtra("reciever", rec)
            startActivity(intent)


        }

    }
}

