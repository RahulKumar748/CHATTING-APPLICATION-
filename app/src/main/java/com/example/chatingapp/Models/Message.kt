package com.example.chatingapp.Models

import android.text.Editable

class Message {
    var messageId : String? = null
    var message : String? = null
    var senderId : String = ""
    var timestamp: Long? = null
    var filling: Int? = null
    constructor()
    constructor(message: Editable?, senderId: String?, timestamp: Long){
        this.message = message.toString()
        this.senderId= senderId.toString()
        this.timestamp=timestamp
    }


}