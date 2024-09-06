package com.chronelab.roomdatabase.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.chronelab.roomdatabase.model.Comment
import com.chronelab.roomdatabase.model.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getAllPosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :id")
    fun getPostById(id: Int): Flow<Post?>

    @Query("SELECT * FROM posts WHERE isDeleted = 0")
    fun getActivePosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE status = 'deleted'")
    fun getDeletedPosts(): Flow<List<Post>>

    @Query("SELECT * FROM comment WHERE postId = :postId")  // Ensure table name matches
    fun getCommentsForPost(postId: Int): Flow<List<Comment>>

    @Query("UPDATE posts SET isDeleted = 1 WHERE id = :postId")
    suspend fun softDelete(postId: Int)

    @Insert
    suspend fun insert(post: Post)

    @Update
    suspend fun update(post: Post)

    @Delete
    suspend fun delete(post: Post)


}

