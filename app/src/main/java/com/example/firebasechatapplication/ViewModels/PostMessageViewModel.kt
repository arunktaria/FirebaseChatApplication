package com.example.firebasechatapplication.ViewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.firebasechatapplication.UserChatsModel
import com.google.firebase.database.DatabaseReference

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
}











