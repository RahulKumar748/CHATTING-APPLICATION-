package com.example.chatingapp.Activity


import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.chatingapp.User
import com.example.chatingapp.databinding.ActivitySetupProfileBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class SetupProfileActivity : AppCompatActivity() {
    private lateinit var binding :  ActivitySetupProfileBinding
    private lateinit var database : FirebaseDatabase
    private lateinit var storage : FirebaseStorage
    private lateinit var auth: FirebaseAuth

    private var selectedImage : Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)



        supportActionBar?.title = "setup profile info "

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()


        val launch = registerForActivityResult(ActivityResultContracts.GetContent()
        ) { result ->
            binding.profilePicture.setImageURI(result)
            selectedImage = result
        }
        binding.profilePicture.setOnClickListener { launch.launch("image/*") }


        binding.SaveButton.setOnClickListener {

            binding.progressBar3.visibility = View.VISIBLE

            if (selectedImage == null){
                Toast.makeText(this, "Please Select Profile Pic", Toast.LENGTH_SHORT).show()
            }

            if (binding.nameEntry.text.isEmpty()){
                binding.nameEntry.error="enter name"
                return@setOnClickListener
            }
            if (binding.nameEntry.text.isNotEmpty()) {



                val reference : StorageReference = storage.reference.child("Profile").child(auth.uid.toString())
                selectedImage?.let { it1 ->
                    reference.putFile(it1).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            reference.downloadUrl.addOnSuccessListener { uri ->
                                FirebaseMessaging.getInstance().token.addOnSuccessListener {


                                    val imageUrl: String = uri.toString()
                                    val uid: String = auth.uid.toString()
                                    val phone: String = auth.currentUser?.phoneNumber.toString()
                                    val name: String = binding.nameEntry.text.toString()
                                    val user = User(uid, phone, name, imageUrl, it)



                                    database.reference.child("user").child(uid).setValue(user)
                                        .addOnSuccessListener {

                                            binding.progressBar3.visibility = View.GONE

                                            val intent = Intent(
                                                this@SetupProfileActivity,
                                                MainActivity::class.java
                                            )
                                            startActivity(intent)
                                            finish()
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

