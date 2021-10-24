package com.example.chatingapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatingapp.Models.Message
import com.example.chatingapp.R
import com.example.chatingapp.databinding.ItemReciveBinding
import com.example.chatingapp.databinding.ItemSendBinding
import com.google.firebase.auth.FirebaseAuth

const val ITEM_SEND = 1
const val ITEM_RECIVE = 2

class MessageAdapter(private var context: Context, private var messages: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding : ItemSendBinding = ItemSendBinding.bind(itemView)
    }
    class ReciverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding : ItemReciveBinding = ItemReciveBinding.bind(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        val message : Message = messages[position]
        return if (FirebaseAuth.getInstance().uid.equals(message.senderId)){
            ITEM_SEND
        }else{
            ITEM_RECIVE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == ITEM_SEND){
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_send,parent,false)
            MessageAdapter.SendViewHolder(view)
        }else{
            val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_recive,parent,false)
            MessageAdapter.ReciverViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message : Message = messages[position]
        if ( holder.javaClass == SendViewHolder::class.java){
            var viewHolder : SendViewHolder = holder as SendViewHolder
            viewHolder.binding.sendText.text = message.message
        }else{
            var viewHolder : ReciverViewHolder = holder as ReciverViewHolder
            viewHolder.binding.reciveText.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }


}