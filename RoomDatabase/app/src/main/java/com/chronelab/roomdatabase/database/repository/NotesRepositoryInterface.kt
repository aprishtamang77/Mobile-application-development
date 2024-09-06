package com.chronelab.roomdatabase.database.repository

import com.chronelab.roomdatabase.database.entity.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepositoryInterface {
    fun getAllNotesStream(): Flow<List<Note>>

    fun getNoteStream(id: Int): Flow<Note?>

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun updateNote(note: Note)

}