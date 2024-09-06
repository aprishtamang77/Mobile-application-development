package com.chronelab.roomdatabase.database.repository

import com.chronelab.roomdatabase.database.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryiesRepositoryInterface {
    fun getCategory(name:String): Flow<Category?>

    fun getAllCategories(): Flow<List<Category>>

    fun getCategory(id: Int): Flow<Category?>

    suspend fun insertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun updateCategory(category: Category)
}