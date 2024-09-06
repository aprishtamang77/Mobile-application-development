package com.chronelab.roomdatabase.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,  // Add this line
    val firstName: String,
    val lastName: String,
    val userName: String,
    val emailAddress: String, // Make sure this field is present
    val password: String,
    val isAdmin: Boolean = false
) : Serializable