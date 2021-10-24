package com.example.chatingapp.Activity

import android.content.Intent


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatingapp.loadingScreen.LoadingScreen
import com.example.chatingapp.databinding.ActivityOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
       private lateinit var binding : ActivityOtpactivityBinding
       lateinit var auth: FirebaseAuth
       var verificationid: String = "null"
       var progressbar  :  LoadingScreen?  = LoadingScreen(this@OTPActivity)


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        progressbar?.loadingScreen()
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "OTP verification "
        auth= FirebaseAuth.getInstance()
        val addNumber:String = intent.getStringExtra("phoneNumber").toString()
        binding.verifynumberShow.append(" $addNumber")
        var option : PhoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(addNumber)
            .setActivity(this)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    auth.signInWithCredential(p0).addOnCompleteListener(this@OTPActivity) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this@OTPActivity, SetupProfileActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        } else {
                            Toast.makeText(this@OTPActivity, "failed log in Try again", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(this@OTPActivity,p0.message, Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(verifyid: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verifyid, p1)
                    progressbar?.loadingScreenStop()
                    binding.otpEntry.requestFocus()
                    verificationid = verifyid
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(option)
        binding.verifyOtp.setOnClickListener {
            var credential: PhoneAuthCredential =
                PhoneAuthProvider.getCredential(verificationid, binding.otpEntry.text.toString())
            auth.signInWithCredential(credential).addOnCompleteListener(this@OTPActivity) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@OTPActivity, SetupProfileActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    Toast.makeText(this@OTPActivity, "failed log in Try again", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}



