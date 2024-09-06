package com.chronelab.roomdatabase.activity // defines the package name for the activity

import android.content.Intent // imports the Intent class for launching other activities
import android.os.Bundle // imports the Bundle class for managing activity state
import android.util.Log // imports the Log class for logging messages
import androidx.activity.ComponentActivity // imports the base class for activities that use Compose
import androidx.activity.compose.setContent // imports the function to set the content of the activity
import androidx.compose.foundation.Image // imports Image composable for displaying images
import androidx.compose.foundation.layout.* // imports layout-related functions from Jetpack Compose
import androidx.compose.material.icons.Icons // imports the Icons class for material icons
import androidx.compose.material.icons.filled.ExitToApp // imports the ExitToApp icon
import androidx.compose.material3.* // imports Material 3 components for UI
import androidx.compose.runtime.* // imports runtime functions for state management in Compose
import androidx.compose.ui.Modifier // imports Modifier class for modifying UI elements
import androidx.compose.ui.layout.ContentScale // imports ContentScale for scaling images
import androidx.compose.ui.res.painterResource // imports painterResource for loading image resources
import androidx.compose.ui.unit.dp // imports dp (density-independent pixels) unit for sizing
import com.chronelab.roomdatabase.R // imports the R class for accessing resources
import com.chronelab.roomdatabase.RoomApp // imports the RoomApp class for application-wide functionality
import com.chronelab.roomdatabase.ui.theme.RoomDatabaseTheme // imports the theme for the app

class HomeActivity : ComponentActivity() { // defines the HomeActivity class extending ComponentActivity
    companion object {
        private val TAG = HomeActivity::class.java.simpleName // sets the log tag for HomeActivity
    }

    @OptIn(ExperimentalMaterial3Api::class) // annotates the usage of experimental API in Material 3
    override fun onCreate(savedInstanceState: Bundle?) { // overrides the onCreate method to initialize the activity
        super.onCreate(savedInstanceState) // calls the superclass method
        val roomApp = application as RoomApp // casts the application to RoomApp to access application-wide methods
        val loggedUser = roomApp.loggedUser() // retrieves the logged-in user from the RoomApp instance
        Log.i(TAG, "Logged User: $loggedUser") // logs the logged-in user information

        setContent { // sets the content for the activity using Jetpack Compose
            RoomDatabaseTheme { // applies the RoomDatabaseTheme
                Scaffold(
                    modifier = Modifier.fillMaxSize(), // sets the modifier to fill the maximum size of the parent
                    topBar = {
                        TopAppBar(
                            title = { Text("Admin") }, // sets the title of the app bar
                            actions = {
                                IconButton(onClick = { logout() }) { // defines the logout icon button
                                    Icon(Icons.Filled.ExitToApp, contentDescription = "Logout") // sets the logout icon
                                }
                            }
                        )
                    }
                ) { paddingValues -> // provides padding values for the content area
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Background Image
                        Image(
                            painter = painterResource(id = R.drawable.home_background), // loads the background image resource
                            contentDescription = null, // sets content description for accessibility
                            contentScale = ContentScale.Crop, // scales the image to crop to fit the container
                            modifier = Modifier.fillMaxSize() // sets the modifier to fill the maximum size of the parent
                        )

                        // Main Content
                        Column(
                            modifier = Modifier
                                .fillMaxSize() // sets the modifier to fill the maximum size of the parent
                                .padding(paddingValues) // applies padding values from the Scaffold
                                .padding(16.dp) // adds padding of 16 dp
                        ) {
                            // "All Students" button
                            Button(
                                onClick = { navigateToAllStudents() }, // defines the button click action
                                modifier = Modifier
                                    .padding(bottom = 16.dp) // adds bottom padding of 16 dp
                                    .fillMaxWidth() // sets the modifier to fill the maximum width of the parent
                            ) {
                                Text("All Students") // sets the button text
                            }

                            // "All Posts" button
                            Button(
                                onClick = { navigateToAllPosts() }, // defines the button click action
                                modifier = Modifier
                                    .padding(bottom = 16.dp) // adds bottom padding of 16 dp
                                    .fillMaxWidth() // sets the modifier to fill the maximum width of the parent
                            ) {
                                Text("All Posts") // sets the button text
                            }

                        }
                    }
                }
            }
        }
    }

    private fun logout() { // defines the logout function
        val roomApp = application as RoomApp // casts the application to RoomApp to access application-wide methods
        roomApp.logout() // logs out the user
        navigateToLogin() // navigates to the LoginActivity
    }

    private fun navigateToLogin() { // defines the function to navigate to the login screen
        val intent = Intent(this, LoginActivity::class.java) // creates an intent to start LoginActivity
        startActivity(intent) // starts LoginActivity
        finish() // finishes the current activity
    }

    private fun navigateToAllStudents() { // defines the function to navigate to the AllStudentsActivity
        val intent = Intent(this, AllStudentsActivity::class.java) // creates an intent to start AllStudentsActivity
        startActivity(intent) // starts AllStudentsActivity
    }

    private fun navigateToAllPosts() { // defines the function to navigate to the AllPostsActivity
        val intent = Intent(this, AllPostsActivity::class.java) // creates an intent to start AllPostsActivity
        startActivity(intent) // starts AllPostsActivity
    }
}
