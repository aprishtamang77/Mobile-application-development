package com.chronelab.roomdatabase

import android.app.Application
import com.chronelab.roomdatabase.model.User
import com.chronelab.roomdatabase.database.DatabaseDataContainer

class RoomApp: Application(){
    lateinit var dbContainer: DatabaseDataContainer
    var user:User? = null

    override fun onCreate() {
        super.onCreate()
        dbContainer = DatabaseDataContainer(this)
    }

    fun logout() {
        user = null
    }
    fun login(loggedUser: User?) {
        user = loggedUser
    }

    fun loggedUser(): User? {
        return user
    }
}