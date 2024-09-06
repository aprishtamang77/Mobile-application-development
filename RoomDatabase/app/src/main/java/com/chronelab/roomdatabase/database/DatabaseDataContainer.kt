package com.chronelab.roomdatabase.database

import android.content.Context
import com.chronelab.roomdatabase.database.repository.CategoriesRepository
import com.chronelab.roomdatabase.database.repository.CategoryiesRepositoryInterface
import com.chronelab.roomdatabase.database.repository.NotesRepository
import com.chronelab.roomdatabase.database.repository.NotesRepositoryInterface

interface DatabaseContainer {
    val notesRepositoryInterface : NotesRepositoryInterface
    val categoryiesRepositoryInterface: CategoryiesRepositoryInterface
}

class DatabaseDataContainer(private val context: Context) : DatabaseContainer {
    /**
     * Implementation for [ItemsRepositoryInterface]
     */

    override val notesRepositoryInterface: NotesRepositoryInterface by lazy {
        NotesRepository(NoteDatabase.getDatabase(context).noteDao())
    }

    override val categoryiesRepositoryInterface: CategoryiesRepositoryInterface by lazy {
        CategoriesRepository(NoteDatabase.getDatabase(context).categoryDao())
    }
}