package com.chronelab.roomdatabase.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val desc: String
) {
}