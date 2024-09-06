package com.chronelab.roomdatabase.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chronelab.roomdatabase.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // Query to get all users
    @Query("SELECT * FROM users") // Updated table name to "user"
    fun getAllUsers(): Flow<List<User>>

    // Query to get a user by their ID
    @Query("SELECT * FROM users WHERE id = :id") // Updated table name to "user"
    fun getUserById(id: Int): Flow<User?>

    // Query to get a user by their username
    @Query("SELECT * FROM users WHERE userName = :username LIMIT 1") // Updated table name to "user"
    fun getUserByUsername(username: String): Flow<User?>

    // Insert a new user, ignoring conflicts
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    // Delete an existing user
    @Delete
    suspend fun deleteUser(user: User)

    // Update an existing user
    @Update
    suspend fun updateUser(user: User)
}
