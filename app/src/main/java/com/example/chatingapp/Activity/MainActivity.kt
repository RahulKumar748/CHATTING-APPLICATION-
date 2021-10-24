package com.example.chatingapp.Activity

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
//import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatingapp.*
import com.example.chatingapp.Adapter.UserAdapter
import com.example.chatingapp.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var database : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.VISIBLE

        database = FirebaseDatabase.getInstance()

        FirebaseMessaging.getInstance().token.addOnSuccessListener {

            var map : HashMap<String,String> = HashMap<String,String>()
            map["token"] = it
           database.reference.child("user").child(FirebaseAuth.getInstance().uid.toString()).updateChildren(
               map as Map<String, Any>)
       }


        val users  = ArrayList<User>()


        val userAdapter   =  UserAdapter(this,users)
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = userAdapter

        database.reference.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                for (snapshot1 : DataSnapshot in snapshot.children){
                    val user : User = snapshot1.getValue(User::class.java)!!

                    if (FirebaseAuth.getInstance().uid.toString() != user.uid) {
                        users.add(user)
                    }else{
                        var data : SharedPreferences? = getSharedPreferences("data", MODE_PRIVATE)
                        var editor : SharedPreferences.Editor = data!!.edit()
                        editor.putString("name",user.name)
                        editor.apply()
                    }
                }

                binding.progressBar.visibility = View.GONE

                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })




    }



}