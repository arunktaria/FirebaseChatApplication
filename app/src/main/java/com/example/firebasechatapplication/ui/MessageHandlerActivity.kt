package com.example.firebasechatapplication.ui

import android.content.Intent
import android.os.Bundle
import android.provider.UserDictionary.Words.APP_ID
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatapplication.*
import com.example.firebasechatapplication.RecyclerAdapters.MessagelistAdapter

import com.example.firebasechatapplication.Utils.ToastBuilder
import com.example.firebasechatapplication.ViewModels.PostMessageViewModel
import com.example.firebasechatapplication.databinding.ChatmainscreenBinding
import com.example.firebasechatapplication.databinding.VideocalllayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine


class MessageHandlerActivity : AppCompatActivity() {
    lateinit var binding: ChatmainscreenBinding
    lateinit var database: DatabaseReference
    lateinit var messageourref: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var senderId: String
    lateinit var recieverId: String
    lateinit var adapter: MessagelistAdapter
    lateinit var list: ArrayList<UserChatsModel>
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChatmainscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

       database=FirebaseDatabase.getInstance().reference
        list = arrayListOf()
        auth = FirebaseAuth.getInstance()
        senderId = intent.getStringExtra("uid").toString()

        val i = intent
        val ob = UserData()
        ob.username = i.getStringExtra("username").toString()
        ob.uid = i.getStringExtra("uid").toString()
        ob.profileimage = i.getStringExtra("profilephotourl").toString()
        binding.user = ob


        recieverId = auth.currentUser?.uid.toString()

        binding.chatrecycler.layoutManager = LinearLayoutManager(this)
        adapter = MessagelistAdapter(this, list)

        binding.chatrecycler.adapter = adapter

        val viewmodel = ViewModelProvider(this).get(PostMessageViewModel::class.java)

        viewmodel.checkourIncomingCall(database,senderId+recieverId)

        viewmodel.tempourstatus.observe(this, Observer {
            Log.d("TAG", "status: "+it.toString())

            if(it.toString().equals("active"))
            {
                val intent=Intent(MessageHandlerActivity@this,CallingScreen::class.java)
                intent.putExtra("sender",senderId)
                intent.putExtra("reciever",recieverId)
                intent.putExtra("username",i.getStringExtra("username").toString())
                startActivity(intent)
            }
            else
            {
                binding.onlinegreen.visibility=View.GONE
            }
        })





        messageourref = FirebaseDatabase.getInstance().reference

        binding.fab.setOnClickListener {
            viewmodel.postMessage(
                context,
                messageourref,
                senderId,
                recieverId,
                binding.messageinput.text.toString()
            )
            binding.messageinput.setText("")
            // ToastBuilder.toast(context,"pressed")
        }

        binding.videocallimgview.setOnClickListener {

            messageourref.child("videocall").child(senderId+recieverId).child("videocall")
                .setValue("inactive").addOnCompleteListener {
                    messageourref.child("videocall").child(recieverId+senderId).child("videocall")
                        .setValue("active")
                        .addOnCompleteListener {

                            messageourref .child("videocall").child(senderId+recieverId).child("videocall")
                                .setValue(auth.currentUser?.uid.toString())




                        }

                    val intent=(Intent(MessageHandlerActivity@this,VideoCallActivitytemp::class.java))
                    intent.putExtra("sender",senderId)
                    intent.putExtra("reciever",recieverId)
                    startActivity(intent)
                }
        }


        messageourref.child("chat").child(senderId + recieverId).child("message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (i in snapshot.children) {
                        val ob = i.getValue(UserChatsModel::class.java)
                        if (ob != null) {
                            list.add(ob)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    ToastBuilder.toast(context, error.message.toString())
                }
            })
    }

}