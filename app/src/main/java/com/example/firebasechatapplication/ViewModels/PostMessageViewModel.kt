package com.example.firebasechatapplication.ViewModels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasechatapplication.UserChatsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlin.math.log

class PostMessageViewModel :ViewModel() {

    fun postMessage(
        context: Context,
        databaseReference: DatabaseReference,
        senderid: String,
        recieverid: String,
        messageinput: String
    ) {
        val ob = UserChatsModel()
        ob.message = messageinput
        ob.senderuid = senderid

                databaseReference.child("chat")
                    .child(senderid+recieverid)
                    .child("message")
                    .push().setValue(ob)
                    .addOnCompleteListener {

                        databaseReference.child("chat")
                            .child(recieverid+senderid)
                            .child("message")
                            .push().setValue(ob)
                            .addOnCompleteListener {



                            }


                    }




            }


  private  val ourstatus=MutableLiveData<String>()
    val tempourstatus:LiveData<String>
    get() = ourstatus

    private  val clientstatus=MutableLiveData<String>()
    val tempclientstatus:LiveData<String>
        get() = clientstatus


    fun checkourIncomingCall(databaseReference: DatabaseReference,id:String)
    {
        databaseReference.child("videocall").child(id).child("videocall").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var status=snapshot.value.toString()
                    ourstatus.postValue(status)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: "+error.message.toString())
                }
            }
        )
    }





/*
    fun checkclientIncomingCall(databaseReference: DatabaseReference,id:String)
    {
        databaseReference.child("videocall").child(id).child("videocall").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                var status=snapshot.value.toString()
                    ourstatus.postValue(status)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: "+error.message.toString())
                }
            }
        )
    }*/
}











