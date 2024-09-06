package com.chronelab.roomdatabase.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chronelab.roomdatabase.database.dao.CommentDao
import com.chronelab.roomdatabase.database.dao.PostDao
import com.chronelab.roomdatabase.database.dao.UserDao
import com.chronelab.roomdatabase.model.User
import com.chronelab.roomdatabase.model.Post
import com.chronelab.roomdatabase.model.Comment


@Database(entities = [User::class, Post::class, Comment::class], version = 2, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    companion object {
        @Volatile
        private var instance: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, NoteDatabase::class.java, "note_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
