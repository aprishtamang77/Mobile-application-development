package com.chronelab.roomdatabase.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chronelab.roomdatabase.database.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * from category WHERE id = :id")
    fun getCategory(id: Int): Flow<Category>

    @Query("SELECT * from category ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * from category WHERE name = :name limit 1")
    fun getCategory(name: String): Flow<Category>
}