package com.example.chatingapp

 class User
 {
     var uid : String? = null
     var phoneNumber: String? = null
     var name: String? = null
     var profilePic: String? = null
     var token: String? = null

        constructor( )
        constructor(uid: String, phoneNumber: String, name: String, profilePic: String,token : String?){
            this.uid=uid
            this.phoneNumber=phoneNumber
            this.name=name
            this.profilePic=profilePic
            this.token=token
        }



}
