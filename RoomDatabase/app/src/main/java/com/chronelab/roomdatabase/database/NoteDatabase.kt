package com.chronelab.roomdatabase.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chronelab.roomdatabase.database.dao.CategoryDao
import com.chronelab.roomdatabase.database.dao.NoteDao
import com.chronelab.roomdatabase.database.entity.Category
import com.chronelab.roomdatabase.database.entity.Note

@Database(entities = [Note::class, Category::class], version = 2, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun categoryDao(): CategoryDao


    companion object {
        @Volatile
        private var Instance: NoteDatabase? = null


        fun getDatabase(context: Context): NoteDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, NoteDatabase::class.java, "note_database")
                    .build()
                    .also { Instance = it }
            }
        }


    }
}