package com.chronelab.roomdatabase.database.repository

import com.chronelab.roomdatabase.database.dao.NoteDao
import com.chronelab.roomdatabase.database.entity.Note
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val noteDao: NoteDao): NotesRepositoryInterface {

    override fun getAllNotesStream(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    override fun getNoteStream(id: Int): Flow<Note?> {
        return noteDao.getNote(id)
    }

    override  suspend fun insertNote(note: Note) {

        return noteDao.insert(note)
    }

    override suspend fun deleteNote(note: Note) {
        return noteDao.delete(note)
    }

    override suspend fun updateNote(note: Note) {
        return noteDao.delete(note)
    }
}