package com.example.chatingapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatingapp.Activity.ChatScreenActivity
import com.example.chatingapp.R
import com.example.chatingapp.User
import com.example.chatingapp.databinding.RowConversionBinding



class UserAdapter(private var context: Context, private var users: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding : RowConversionBinding = RowConversionBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
      val view : View = LayoutInflater.from(parent.context).inflate(R.layout.row_conversion,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user : User = users[position]
        holder.binding.name.text = user.name
        Glide.with(context).load(user.profilePic).placeholder(R.drawable.avatar).into(holder.binding.dp)
        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatScreenActivity::class.java)
            intent.putExtra("name",users[position].name)
            intent.putExtra("uid",users[position].uid)
            intent.putExtra("token",users[position].token)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

}