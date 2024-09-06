package com.chronelab.roomdatabase.activity // defines the package name for the activity

import android.content.Intent // imports the Intent class for launching other activities
import android.os.Bundle // imports the Bundle class for managing activity state
import android.util.Log // imports the Log class for logging messages
import androidx.activity.ComponentActivity // imports the base class for activities that use Compose
import androidx.activity.compose.setContent // imports the function to set the content of the activity
import androidx.compose.foundation.layout.* // imports layout-related functions from Jetpack Compose
import androidx.compose.foundation.lazy.LazyColumn // imports LazyColumn for displaying lists in Compose
import androidx.compose.foundation.lazy.items // imports the items function to display a list of items
import androidx.compose.material.icons.Icons // imports the Icons class for material icons
import androidx.compose.material.icons.filled.* // imports filled material icons
import androidx.compose.material3.* // imports Material 3 components for UI
import androidx.compose.runtime.* // imports runtime functions for state management in Compose
import androidx.compose.ui.Modifier // imports Modifier class for modifying UI elements
import androidx.compose.ui.unit.dp // imports dp (density-independent pixels) unit for sizing
import androidx.lifecycle.ViewModelProvider // imports ViewModelProvider for creating ViewModels
import com.chronelab.roomdatabase.database.NoteDatabase.Companion.getDatabase // imports the function to get the database instance
import com.chronelab.roomdatabase.database.repository.UserRepository // imports the UserRepository class for data operations
import com.chronelab.roomdatabase.model.User // imports the User data model
import com.chronelab.roomdatabase.ui.theme.RoomDatabaseTheme // imports the theme for the app
import com.chronelab.roomdatabase.viewmodel.UserViewModel // imports the UserViewModel class for managing UI-related data
import com.chronelab.roomdatabase.viewmodel.UserViewModelFactory // imports the factory class for creating UserViewModel instances
import kotlinx.coroutines.launch // imports launch function for coroutines

class AllStudentsActivity : ComponentActivity() { // defines the AllStudentsActivity class extending ComponentActivity
    private lateinit var userViewModel: UserViewModel // declares a variable for UserViewModel
    private var selectedStudent by mutableStateOf<User?>(null) // declares a variable for the currently selected student

    @OptIn(ExperimentalMaterial3Api::class) // annotates the usage of experimental API in Material 3
    override fun onCreate(savedInstanceState: Bundle?) { // overrides the onCreate method to initialize the activity
        super.onCreate(savedInstanceState) // calls the superclass method

        // Initialize the database and repository
        val database = getDatabase(this) // initializes the database instance
        val userRepository = UserRepository(database.userDao()) // creates a UserRepository with the userDao from the database
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java) // creates the UserViewModel

        setContent { // sets the content for the activity using Jetpack Compose
            RoomDatabaseTheme { // applies the RoomDatabaseTheme
                var students by remember { mutableStateOf(listOf<User>()) } // defines a state for storing students
                val coroutineScope = rememberCoroutineScope() // creates a coroutine scope for launching coroutines
                val showCreateDialog = remember { mutableStateOf(false) } // defines a state for showing the create dialog
                val showUpdateDialog = remember { mutableStateOf(false) } // defines a state for showing the update dialog
                val showDeleteConfirmationDialog = remember { mutableStateOf(false) } // defines a state for showing the delete confirmation dialog

                LaunchedEffect(Unit) { // launches a coroutine when the composable is first loaded
                    userViewModel.allUsers.collect { userList -> // collects all users from the UserViewModel
                        students = userList.filter { !it.isAdmin } // updates the state with the list of non-admin students
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize() // sets the modifier to fill the maximum size of the parent
                ) {

                    Scaffold(
                        modifier = Modifier.fillMaxSize(), // sets the modifier to fill the maximum size of the parent
                        topBar = {
                            TopAppBar(
                                title = { Text("All Students") }, // sets the title of the app bar
                                navigationIcon = {
                                    IconButton(onClick = { // defines the navigation icon button
                                        val intent = Intent(this@AllStudentsActivity, HomeActivity::class.java) // creates an intent to start HomeActivity
                                        startActivity(intent) // starts HomeActivity
                                        finish() // finishes the current activity
                                    }) {
                                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back") // sets the back arrow icon
                                    }
                                },
                                actions = {
                                    IconButton(onClick = { showCreateDialog.value = true }) { // defines the add student button
                                        Icon(Icons.Filled.Add, contentDescription = "Add Student") // sets the add icon
                                    }
                                }
                            )
                        }
                    ) { paddingValues -> // provides padding values for the content area
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize() // sets the modifier to fill the maximum size of the parent
                                .padding(paddingValues) // applies padding values from the Scaffold
                        ) {
                            items(students) { student -> // iterates over each student in the list
                                StudentItem(
                                    user = student, // passes the student to the StudentItem composable
                                    onUpdateClick = {
                                        selectedStudent = student // sets the selected student for updating
                                        showUpdateDialog.value = true // shows the update dialog
                                    },
                                    onDeleteClick = {
                                        selectedStudent = student // sets the selected student for deletion
                                        showDeleteConfirmationDialog.value = true // shows the delete confirmation dialog
                                    }
                                )
                            }
                        }
                    }

                    // Create Student Dialog
                    if (showCreateDialog.value) { // checks if the create dialog should be shown
                        StudentDialog(
                            onDismiss = { showCreateDialog.value = false }, // handles dialog dismissal
                            onSubmit = { user -> // handles user submission
                                coroutineScope.launch {
                                    userViewModel.registerUser(user, {
                                        showCreateDialog.value = false // closes the dialog on success
                                    }, {
                                        // Handle failure
                                        Log.e("AllStudentsActivity", "Failed to register user") // logs an error message on failure
                                    })
                                }
                            }
                        )
                    }

                    // Update Student Dialog
                    if (showUpdateDialog.value && selectedStudent != null) { // checks if the update dialog should be shown
                        StudentDialog(
                            initialUser = selectedStudent, // passes the selected student for initial values
                            onDismiss = { showUpdateDialog.value = false }, // handles dialog dismissal
                            onSubmit = { user -> // handles user submission
                                coroutineScope.launch {
                                    userViewModel.updateUser(user, {
                                        showUpdateDialog.value = false // closes the dialog on success
                                        selectedStudent = null // clears the selected student
                                    }, {
                                        // Handle failure
                                        Log.e("AllStudentsActivity", "Failed to update user") // logs an error message on failure
                                    })
                                }
                            }
                        )
                    }

                    // Delete Confirmation Dialog
                    if (showDeleteConfirmationDialog.value && selectedStudent != null) { // checks if the delete confirmation dialog should be shown
                        AlertDialog(
                            onDismissRequest = { showDeleteConfirmationDialog.value = false }, // handles dialog dismissal
                            title = { Text("Confirm Delete") }, // sets the title of the alert dialog
                            text = { Text("Are you sure you want to delete ${selectedStudent!!.firstName} ${selectedStudent!!.lastName}?") }, // sets the content of the alert dialog
                            confirmButton = {
                                TextButton(onClick = {
                                    coroutineScope.launch {
                                        userViewModel.deleteUser(selectedStudent!!) // deletes the selected student
                                        showDeleteConfirmationDialog.value = false // closes the dialog
                                        selectedStudent = null // clears the selected student
                                    }
                                }) {
                                    Text("Delete") // sets the text for the delete button
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    showDeleteConfirmationDialog.value = false // closes the dialog without action
                                    selectedStudent = null // clears the selected student
                                }) {
                                    Text("Cancel") // sets the text for the cancel button
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun StudentItem(user: User, onUpdateClick: () -> Unit, onDeleteClick: () -> Unit) { // defines a composable for displaying student items
        Column(
            modifier = Modifier
                .fillMaxWidth() // sets the modifier to fill the maximum width of the parent
                .padding(16.dp) // adds padding of 16 dp
        ) {
            Text(text = "${user.firstName} ${user.lastName}", style = MaterialTheme.typography.bodyLarge) // displays the student's name
            Text(text = "Username: ${user.userName}", style = MaterialTheme.typography.bodyMedium) // displays the student's username
            Row(
                modifier = Modifier
                    .fillMaxWidth() // sets the modifier to fill the maximum width of the parent
                    .padding(top = 8.dp), // adds top padding of 8 dp
                horizontalArrangement = Arrangement.SpaceBetween // arranges icons with space between
            ) {
                IconButton(onClick = onUpdateClick) { // defines the update icon button
                    Icon(Icons.Filled.Edit, contentDescription = "Edit") // sets the edit icon
                }
                IconButton(onClick = onDeleteClick) { // defines the delete icon button
                    Icon(Icons.Filled.Delete, contentDescription = "Delete") // sets the delete icon
                }
            }
        }
    }
}
