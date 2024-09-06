package com.chronelab.roomdatabase.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.chronelab.roomdatabase.model.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comment WHERE postId = :postId")
    fun getCommentsForPost(postId: Int): Flow<List<Comment>>

    @Query("SELECT * FROM comment WHERE postId = :postId")
    fun getCommentsByPostId(postId: Int): Flow<List<Comment>>

    @Insert
    suspend fun insert(comment: Comment)

    @Update
    suspend fun update(comment: Comment)

    @Delete
    suspend fun delete(comment: Comment)
}
