package com.chronelab.roomdatabase

import android.app.Application
import com.chronelab.roomdatabase.database.DatabaseDataContainer
import com.chronelab.roomdatabase.model.User

class RoomApp : Application() {
    lateinit var dbContainer: DatabaseDataContainer
    private var user: User? = null

    override fun onCreate() {
        super.onCreate()
        dbContainer = DatabaseDataContainer(this)
    }

    // Function to log in a user
    fun login(loggedUser: User?) {
        user = loggedUser
    }

    // Function to get the currently logged in user
    fun loggedUser(): User? {
        return user
    }

    // Function to log out the current user
    fun logout() {
        user = null
    }
}
