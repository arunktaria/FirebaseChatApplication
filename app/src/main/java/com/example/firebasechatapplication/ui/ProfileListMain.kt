package com.example.firebasechatapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasechatapplication.RecyclerAdapters.UserProfilesAdapter

import com.example.firebasechatapplication.UserChatsModel
import com.example.firebasechatapplication.UserData
import com.example.firebasechatapplication.Utils.ToastBuilder
import com.example.firebasechatapplication.databinding.ActivityChatListMainBinding
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ProfileListMain : AppCompatActivity() {
var TAG="TAG"
   lateinit var binding:ActivityChatListMainBinding
    lateinit var adapter: FirebaseListAdapter<UserChatsModel>
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    var context=this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatListMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance().reference.child("user")

        var list=ArrayList<UserData>()
        list= arrayListOf()

        binding.recyclerview.layoutManager=LinearLayoutManager(this)
        var adapter= UserProfilesAdapter(context,list)
        binding.recyclerview.adapter=adapter
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               for(i in snapshot.children)
               {
                   val ob=i.getValue(UserData::class.java)
                   list.add(ob!!)
                   Log.d(TAG, "onDataChange: "+ob.username)
                   adapter.notifyDataSetChanged()

               }

            }

            override fun onCancelled(error: DatabaseError) {
                ToastBuilder.toast(context,error.message.toString())
            }
        })




    }




}