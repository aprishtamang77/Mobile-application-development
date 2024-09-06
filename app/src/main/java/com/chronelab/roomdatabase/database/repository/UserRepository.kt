package com.chronelab.roomdatabase.database.repository

import com.chronelab.roomdatabase.database.dao.UserDao
import com.chronelab.roomdatabase.model.User
import kotlinx.coroutines.flow.Flow

// If you have a UserRepositoryInterface, make sure it's imported and used here
// import com.chronelab.roomdatabase.database.repository.UserRepositoryInterface

class UserRepository(private val userDao: UserDao) : UserRepositoryInterface {

    // Get all users as a Flow
    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }

    // Get a user by ID as a Flow
    override fun getUserById(id: Int): Flow<User?> {
        return userDao.getUserById(id)
    }

    // Insert a new user
    override suspend fun insertUser(user: User) {
        userDao.insertUser(user) // Ensure this method is defined in UserDao
    }

    // Delete a user
    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user) // Ensure this method is defined in UserDao
    }

    // Update a user
    override suspend fun updateUser(user: User) {
        userDao.updateUser(user) // Ensure this method is defined in UserDao
    }

    // Get a user by username as a Flow
    override fun getUserByUsername(username: String): Flow<User?> {
        println("Fetching user with username: $username")
        return userDao.getUserByUsername(username)
    }
}
