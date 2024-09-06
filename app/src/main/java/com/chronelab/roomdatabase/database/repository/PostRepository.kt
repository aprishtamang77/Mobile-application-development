package com.chronelab.roomdatabase.database.repository

import com.chronelab.roomdatabase.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepositoryInterface {
    fun getAllPosts(): Flow<List<Post>>
    fun getPostById(id: Int): Flow<Post?>
    suspend fun insertPost(post: Post)
    suspend fun deletePost(post: Post)
    suspend fun updatePost(post: Post)
}
