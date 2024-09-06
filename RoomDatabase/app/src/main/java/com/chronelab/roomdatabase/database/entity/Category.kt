package com.chronelab.roomdatabase.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val desc: String
) {
}