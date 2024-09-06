package com.chronelab.roomdatabase.model

import java.io.Serializable

data class User(val id:Int, var firstName: String, var lastName:String, var userName: String, var password: String):
    Serializable {

    fun isLoginValid(): Boolean {
        return (this.userName == "user" && this.password == "password")
    }
}