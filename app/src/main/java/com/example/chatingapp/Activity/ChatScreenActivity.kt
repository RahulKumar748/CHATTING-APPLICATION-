package com.example.chatingapp.Activity


import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Header
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.load.model.Headers
import com.example.chatingapp.Adapter.MessageAdapter
import com.example.chatingapp.Models.Message
import com.example.chatingapp.databinding.ActivityChatScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatScreenActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatScreenBinding

    lateinit var mesasgeAdapter : MessageAdapter
    private lateinit var database: FirebaseDatabase

    var senderRoom : String = ""
    private var reciverRoom : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var data : SharedPreferences? = getSharedPreferences("data", MODE_PRIVATE)
        var senderName  : String = data!!.getString("name","").toString()

        val messages = ArrayList<Message>()
        mesasgeAdapter = MessageAdapter(this,messages)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = mesasgeAdapter



        val reciverUid : String? = intent.getStringExtra("uid")
        val name : String? = intent.getStringExtra("name")
        val token = intent.getStringExtra("token")

        val senderUid   = FirebaseAuth.getInstance().uid

        senderRoom = senderUid + reciverUid
        reciverRoom = reciverUid + senderUid

        database = FirebaseDatabase.getInstance()
        

        database.reference.child("chats").child(senderRoom).child("messages").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messages.clear()
                for (snapshot1 : DataSnapshot in  snapshot.children){

                    var message : Message = snapshot1.getValue(Message::class.java)!!

                    messages.add(message)
                }

                mesasgeAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.sendButton.setOnClickListener {

            if (binding.messageType.text.isEmpty()){
                binding.messageType.error = "Type a message."
                return@setOnClickListener
            }

            var messageText: Editable? = binding.messageType.text
            val mssg : String = binding.messageType.text.toString()

            var date : Date = Date()

            val message : Message = Message(messageText,senderUid,date.time)

            binding.messageType.text.clear()

            database.reference.child("chats").child(senderRoom).child("messages").push().setValue(message).addOnSuccessListener{
                database.reference.child("chats").child(reciverRoom).child("messages").push().setValue(message).addOnSuccessListener {
                    sendNotification(senderName ,mssg,token)
                }
            }

        }
        supportActionBar?.title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // back arrow top in action bar ...this code active it




    }


  private fun sendNotification(name : String, mssg : String?, token : String?){

      var queue : RequestQueue = Volley.newRequestQueue(this)
      val url : String= "https://fcm.googleapis.com/fcm/send"
      var data : JSONObject = JSONObject()
      data.put("title",name )
      data.put("body",mssg)

      var notificationData : JSONObject = JSONObject()
      notificationData.put("notification",data)
      notificationData.put("to",token)

      var request  : JsonObjectRequest = object : JsonObjectRequest(
          Request.Method.POST,
          url,
          notificationData,

          Response.Listener<JSONObject>{ Toast.makeText(this,"success",Toast.LENGTH_SHORT) },

          Response.ErrorListener{ Toast.makeText(this,it.localizedMessage,Toast.LENGTH_SHORT) }
      ) {
          override fun getHeaders(): MutableMap<String, String> {
              var map : HashMap<String,String> = HashMap()
              val key ="key=AAAAqrl9rkM:APA91bGaHtHwBTFWgHiQsh3V4mNVVArqQuCUxdrpZihBioAoOrSR2rMhGcL72cOf4JH-oyCM6zTGCKHVvbsjmrvwMnJRwRYnN3idLJb8MTmoEL-Zr70YGz_czrR5FdJoDh8Qhbgj_Z4a"
              map["Authorization"] = key
              map["context-Type"] = "application/json"
              return map
          }
      }


      queue.add(request)

  }

    override fun onSupportNavigateUp(): Boolean {   // back arrow top in action bar ...this do when top back arrow touch
        finish()
        return super.onSupportNavigateUp()
    }


}


