package com.chronelab.roomdatabase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chronelab.roomdatabase.database.repository.UserRepository
import com.chronelab.roomdatabase.model.User
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.SharingStarted

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    // StateFlow for observing all users
    val allUsers: StateFlow<List<User>> = userRepository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Function to insert a new user
    private fun insertUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }

    // Function to update an existing user
    fun updateUser(user: User, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.updateUser(user)
                onSuccess()
            } catch (e: Exception) {
                onFailure()
            }
        }
    }

    // Function to register a new user with success and failure callbacks
    fun registerUser(user: User, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch {
            try {
                val existingUser = userRepository.getUserByUsername(user.userName).firstOrNull()
                if (existingUser == null) {
                    // User does not exist, so insert a new user
                    insertUser(user)
                    onSuccess()
                } else {
                    // User exists, invoke failure callback
                    println("User already exists with username: ${user.userName}")
                    onFailure()
                }
            } catch (e: Exception) {
                // Handle any other exceptions that might occur
                println("Error occurred during registration: ${e.message}")
                onFailure()
            }
        }
    }

    // Function to delete a user
    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
        }
    }
}

class UserViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
