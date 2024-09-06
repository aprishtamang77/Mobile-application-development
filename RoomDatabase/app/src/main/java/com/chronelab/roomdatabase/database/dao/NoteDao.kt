package com.chronelab.roomdatabase.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chronelab.roomdatabase.database.entity.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * from note WHERE id = :id")
    fun getNote(id: Int): Flow<Note>

    @Query("SELECT * from note ORDER BY title ASC")
    fun getAllNotes(): Flow<List<Note>>
}