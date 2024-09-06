package com.chronelab.roomdatabase.database

import android.content.Context
import com.chronelab.roomdatabase.database.repository.PostRepository
import com.chronelab.roomdatabase.database.repository.PostRepositoryInterface
import com.chronelab.roomdatabase.database.repository.UserRepository
import com.chronelab.roomdatabase.database.repository.UserRepositoryInterface

interface DatabaseContainer {
    val userRepositoryInterface: UserRepositoryInterface
    val postRepositoryInterface: PostRepositoryInterface
}

class DatabaseDataContainer(private val context: Context) : DatabaseContainer {

    private val database by lazy { AppDatabase.getDatabase(context) }

    override val userRepositoryInterface: UserRepositoryInterface by lazy {
        UserRepository(database.userDao())
    }

    override val postRepositoryInterface: PostRepositoryInterface by lazy {
        PostRepository(database.postDao())
    }
}
