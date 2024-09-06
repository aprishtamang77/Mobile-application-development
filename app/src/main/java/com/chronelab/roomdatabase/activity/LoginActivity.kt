package com.chronelab.roomdatabase.activity

import android.content.Intent // Imports Intent class for navigating between activities
import android.os.Bundle // Imports Bundle class for managing activity state
import android.util.Log // Imports Log class for logging messages
import android.widget.Toast // Imports Toast class for displaying messages
import androidx.activity.ComponentActivity // Imports ComponentActivity for basic activity features
import androidx.activity.compose.setContent // Imports function to set content using Jetpack Compose
import androidx.activity.enableEdgeToEdge // Imports function to enable edge-to-edge display
import androidx.compose.foundation.Image // Imports Image composable for displaying images
import androidx.compose.foundation.clickable // Imports clickable modifier for making elements clickable
import androidx.compose.foundation.layout.* // Imports layout-related functions from Jetpack Compose
import androidx.compose.material3.Scaffold // Imports Scaffold component for creating layout structure
import androidx.compose.material3.Text // Imports Text component for displaying text
import androidx.compose.ui.Alignment // Imports Alignment class for alignment options in layout
import androidx.compose.ui.Modifier // Imports Modifier class for modifying UI elements
import androidx.compose.ui.graphics.Color // Imports Color class for setting colors
import androidx.compose.ui.layout.ContentScale // Imports ContentScale for scaling images
import androidx.compose.ui.res.painterResource // Imports painterResource for loading image resources
import androidx.compose.ui.unit.dp // Imports dp (density-independent pixels) unit for sizing
import androidx.lifecycle.lifecycleScope // Imports lifecycleScope for managing lifecycle-aware coroutines
import com.chronelab.roomdatabase.RoomApp // Imports RoomApp class for application-wide functionality
import com.chronelab.roomdatabase.model.User // Imports User model class
import com.chronelab.roomdatabase.ui.theme.RoomDatabaseTheme // Imports theme for the app
import com.chronelab.roomdatabase.ui.view.ViewLogin // Imports ViewLogin composable for the login form
import kotlinx.coroutines.flow.firstOrNull // Imports function for handling Flow operations
import kotlinx.coroutines.launch // Imports launch function for starting coroutines
import com.chronelab.roomdatabase.R // Imports the R class for accessing resources
import com.chronelab.roomdatabase.database.NoteDatabase.Companion.getDatabase // Imports getDatabase function for accessing the database

class LoginActivity : ComponentActivity() { // Defines LoginActivity class extending ComponentActivity

    companion object {
        private val TAG = LoginActivity::class.java.simpleName // Sets the log tag for LoginActivity
        private const val ADMIN_USERNAME = "admin" // Defines constant for admin username
        private const val ADMIN_PASSWORD = "admin123" // Defines constant for admin password
    }

    override fun onCreate(savedInstanceState: Bundle?) { // Overrides the onCreate method to initialize the activity
        super.onCreate(savedInstanceState) // Calls the superclass method
        enableEdgeToEdge() // Enables edge-to-edge display

        setContent { // Sets the content of the activity using Jetpack Compose
            RoomDatabaseTheme { // Applies the RoomDatabaseTheme
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize()) { // Container for positioning elements
                        // Background Image
                        Image(
                            painter = painterResource(id = R.drawable.login_background), // Loads background image
                            contentDescription = null, // Sets content description for accessibility
                            contentScale = ContentScale.Crop, // Scales image to fill the container
                            modifier = Modifier.fillMaxSize() // Fills the maximum size of the container
                        )

                        // Login Form
                        Column(
                            modifier = Modifier
                                .fillMaxSize() // Fills the maximum size of the container
                                .padding(16.dp), // Adds padding around the column
                            horizontalAlignment = Alignment.CenterHorizontally, // Centers content horizontally
                            verticalArrangement = Arrangement.Center // Centers content vertically
                        ) {
                            // Login Button
                            ViewLogin(
                                onLoginClick = { userName, password -> // Callback for login button click
                                    btnLoginAction(userName, password) // Handles login action
                                },
                                onSignUpClick = { // Callback for sign up button click
                                    val intent = Intent(this@LoginActivity, SignUpActivity::class.java) // Creates intent to start SignUpActivity
                                    startActivity(intent) // Starts SignUpActivity
                                }
                            )
                        }

                        // Sign Up Prompt
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter) // Aligns column at the bottom center
                                .padding(bottom = 32.dp), // Adds bottom padding
                            horizontalAlignment = Alignment.CenterHorizontally // Centers content horizontally
                        ) {
                            Text(
                                text = "Don't have an account? Sign Up", // Text for the sign-up prompt
                                color = Color.Blue, // Sets text color
                                modifier = Modifier
                                    .clickable { // Makes text clickable
                                        val intent = Intent(this@LoginActivity, SignUpActivity::class.java) // Creates intent to start SignUpActivity
                                        startActivity(intent) // Starts SignUpActivity
                                    }
                                    .padding(8.dp) // Adds padding around the text
                            )
                        }
                    }
                }
            }
        }
    }

    private fun btnLoginAction(userName: String, password: String) { // Handles login action
        if (userName.isBlank() || password.isBlank()) { // Checks if username or password is empty
            showToast("Username or password cannot be empty") // Shows error message
            Log.i(TAG, "Empty username or password.") // Logs the error
            return
        }

        if (userName == ADMIN_USERNAME && password == ADMIN_PASSWORD) { // Checks if credentials match admin credentials
            val adminUser = User(
                firstName = "Admin", // Sets admin user details
                lastName = "User",
                userName = ADMIN_USERNAME,
                password = ADMIN_PASSWORD,
                emailAddress = "admin@example.com", // Adds a valid email address
                isAdmin = true
            )
            Log.i(TAG, "Admin logged in successfully!") // Logs success
            val roomApp = application as RoomApp // Casts the application to RoomApp
            roomApp.login(adminUser) // Logs in the admin user
            navigateToHome() // Navigates to HomeActivity
            return
        }

        val database = getDatabase(this) // Gets the database instance
        val userDao = database.userDao() // Gets the user DAO

        lifecycleScope.launch { // Launches a coroutine
            try {
                val user = userDao.getUserByUsername(userName).firstOrNull() // Fetches user by username

                if (user == null) { // Checks if user exists
                    showToast("User not found") // Shows error message
                    Log.i(TAG, "User not found for username: $userName") // Logs error
                } else if (user.password == password) { // Checks if password matches
                    Log.i(TAG, "User logged in successfully!") // Logs success
                    val roomApp = application as RoomApp // Casts the application to RoomApp
                    roomApp.login(user) // Logs in the user

                    // Navigate based on user type
                    if (user.isAdmin) {
                        navigateToHome() // Navigates to HomeActivity for admin
                    } else {
                        navigateToStudentActivity() // Navigates to StudentActivity for regular users
                    }
                } else {
                    showToast("Invalid password") // Shows error message
                    Log.i(TAG, "Invalid password for user: $userName") // Logs error
                }
            } catch (e: Exception) {
                showToast("An error occurred. Please try again.") // Shows error message
                Log.e(TAG, "Error during login process", e) // Logs exception
            }
        }
    }

    private fun showToast(message: String) { // Shows a Toast message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show() // Displays the Toast
    }

    private fun navigateToHome() { // Navigates to HomeActivity
        val intent = Intent(this@LoginActivity, HomeActivity::class.java) // Creates intent to start HomeActivity
        startActivity(intent) // Starts HomeActivity
        finish() // Finishes LoginActivity
    }

    private fun navigateToStudentActivity() { // Navigates to StudentActivity
        val intent = Intent(this@LoginActivity, StudentActivity::class.java) // Creates intent to start StudentActivity
        startActivity(intent) // Starts StudentActivity
        finish() // Finishes LoginActivity
    }
}
