package com.chronelab.roomdatabase.database.repository

import com.chronelab.roomdatabase.database.dao.PostDao
import com.chronelab.roomdatabase.model.Post
import kotlinx.coroutines.flow.Flow

class PostRepository(private val postDao: PostDao) : PostRepositoryInterface {

    override fun getAllPosts(): Flow<List<Post>> {
        return postDao.getAllPosts()
    }

    override fun getPostById(id: Int): Flow<Post?> {
        return postDao.getPostById(id)
    }

    override suspend fun insertPost(post: Post) {
        postDao.insert(post)
    }

    override suspend fun deletePost(post: Post) {
        postDao.delete(post)
    }

    override suspend fun updatePost(post: Post) {
        postDao.update(post)
    }
}
