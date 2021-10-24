package com.example.chatingapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatingapp.databinding.ActivityPhoneNumberBinding
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth


class PhoneNumberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneNumberBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){

        val intent = Intent(this@PhoneNumberActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        supportActionBar?.title="login"

        binding.numberEntry.requestFocus()


        binding.sendOtp.setOnClickListener {
            if(binding.numberEntry.text.isEmpty()){
                binding.numberEntry.error="enter number"
                return@setOnClickListener
            }
            val intent = Intent(this@PhoneNumberActivity, OTPActivity::class.java)

            intent.putExtra("phoneNumber", binding.numberEntry.text.toString())
            startActivity(intent)
        }
    }
}

