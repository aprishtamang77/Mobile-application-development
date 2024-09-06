package com.chronelab.roomdatabase.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Post::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postId: Int,
    val content: String
)
