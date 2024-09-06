package com.chronelab.roomdatabase.database.repository

import com.chronelab.roomdatabase.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepositoryInterface {
    // Get a Flow of all users
    fun getAllUsers(): Flow<List<User>>

    // Get a Flow of a user by their ID
    fun getUserById(id: Int): Flow<User?>

    // Insert a new user
    suspend fun insertUser(user: User)

    // Delete a user
    suspend fun deleteUser(user: User)

    // Update an existing user
    suspend fun updateUser(user: User)

    // Get a Flow of a user by their username
    fun getUserByUsername(username: String): Flow<User?>
}
