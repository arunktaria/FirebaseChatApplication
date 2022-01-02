package com.example.firebasechatapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatapplication.RecyclerAdapters.MessagelistAdapter

import com.example.firebasechatapplication.UserChatsModel
import com.example.firebasechatapplication.UserData
import com.example.firebasechatapplication.Utils.ToastBuilder
import com.example.firebasechatapplication.ViewModels.PostMessageViewModel
import com.example.firebasechatapplication.databinding.ChatmainscreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


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