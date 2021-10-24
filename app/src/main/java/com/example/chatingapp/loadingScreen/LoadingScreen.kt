package com.example.chatingapp.loadingScreen


import android.app.Activity
import android.app.AlertDialog
import com.example.chatingapp.R


public class LoadingScreen(private val mActivity: Activity) {
    lateinit var isdaloge:AlertDialog
//    lateinit var binding : LoadingScreenBinding
//    fun setText(text : String){
//        binding = LoadingScreenBinding.inflate(layout)    }
    fun loadingScreen(){
       val builder = AlertDialog.Builder(mActivity)
       builder.setView(mActivity.layoutInflater.inflate(R.layout.loading_screen,null))
       builder.setCancelable(false)
       isdaloge=builder.create()
       isdaloge=builder.show()
    }
    fun loadingScreenStop(){
        isdaloge.dismiss()
     }
}