package com.chronelab.roomdatabase.activity // package declaration for the activity

import android.content.Intent // import for Intent to start other activities
import android.os.Bundle // import for Bundle used in activity lifecycle
import android.util.Log // import for logging messages
import android.widget.Toast // import for showing Toast messages
import androidx.activity.ComponentActivity // import for base activity class
import androidx.activity.compose.setContent // import for setting the content of the activity using Jetpack Compose
import androidx.activity.enableEdgeToEdge // import for enabling edge-to-edge layout
import androidx.compose.foundation.layout.* // import for layout components in Compose
import androidx.compose.material3.* // import for material components in Compose
import androidx.compose.runtime.* // import for state management in Compose
import androidx.compose.ui.Modifier // import for modifying layout attributes in Compose
import androidx.compose.ui.text.input.PasswordVisualTransformation // import for password field visual transformation
import androidx.compose.ui.unit.dp // import for dimension values
import androidx.lifecycle.ViewModelProvider // import for ViewModelProvider to provide ViewModel instances
import com.chronelab.roomdatabase.database.NoteDatabase.Companion.getDatabase // import for getting the Room database instance
import com.chronelab.roomdatabase.database.repository.UserRepository // import for UserRepository to manage user data
import com.chronelab.roomdatabase.model.User // import for User data model
import com.chronelab.roomdatabase.ui.theme.RoomDatabaseTheme // import for theme styling
import com.chronelab.roomdatabase.viewmodel.UserViewModel // import for UserViewModel to manage UI-related data
import com.chronelab.roomdatabase.viewmodel.UserViewModelFactory // import for ViewModelFactory to create UserViewModel instances

class SignUpActivity : ComponentActivity() { // SignUpActivity class extending ComponentActivity
    private lateinit var userViewModel: UserViewModel // late initialization of UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) { // onCreate method for initializing the activity
        super.onCreate(savedInstanceState) // call to the superclass method
        enableEdgeToEdge() // enable edge-to-edge layout for the activity

        try { // start of try block to handle potential errors
            // initialize the database and repository
            val database = getDatabase(this) // get the Room database instance
            val userRepository = UserRepository(database.userDao()) // create a UserRepository instance
            userViewModel = ViewModelProvider(this, UserViewModelFactory(userRepository)).get(UserViewModel::class.java) // initialize UserViewModel using ViewModelProvider

            setContent { // set the content view using Jetpack Compose
                RoomDatabaseTheme { // apply the custom theme
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> // use Scaffold to handle the layout
                        SignUpScreen { firstName, lastName, userName, emailAddress, password -> // SignUpScreen composable function with sign-up callback
                            val user = User( // create a User instance with provided data
                                firstName = firstName,
                                lastName = lastName,
                                userName = userName,
                                password = password,
                                emailAddress = emailAddress,
                                isAdmin = false // set isAdmin to false for regular users
                            )

                            userViewModel.registerUser(user, { // register the user
                                val intent = Intent(this@SignUpActivity, LoginActivity::class.java) // create an intent to start LoginActivity
                                startActivity(intent) // start LoginActivity
                                finish() // finish the current activity
                            }, {
                                Toast.makeText(this@SignUpActivity, "User already exists", Toast.LENGTH_SHORT).show() // show toast if user already exists
                                Log.w(TAG, "User already exists with username: $userName") // log warning message
                            })
                        }
                    }
                }
            }
        } catch (e: Exception) { // catch block for handling exceptions
            Log.e(TAG, "Error initializing SignUpActivity", e) // log error message
            Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_LONG).show() // show toast for error
        }
    }

    companion object { // companion object for defining constants
        private const val TAG = "SignUpActivity" // log tag for this activity
    }
}

@Composable
fun SignUpScreen(onSignUp: (String, String, String, String, String) -> Unit) { // composable function for the sign-up screen
    var firstName by remember { mutableStateOf("") } // state for first name
    var lastName by remember { mutableStateOf("") } // state for last name
    var userName by remember { mutableStateOf("") } // state for username
    var emailAddress by remember { mutableStateOf("") } // state for email address
    var password by remember { mutableStateOf("") } // state for password

    Column( // column layout to arrange elements vertically
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // set padding around the column
        verticalArrangement = Arrangement.Center // center elements vertically
    ) {
        TextField( // text field for first name
            value = firstName,
            onValueChange = { firstName = it }, // update first name state
            label = { Text("First Name") }, // label for the text field
            modifier = Modifier.fillMaxWidth() // fill width of the parent
        )

        Spacer(modifier = Modifier.height(8.dp)) // spacer for vertical spacing

        TextField( // text field for last name
            value = lastName,
            onValueChange = { lastName = it }, // update last name state
            label = { Text("Last Name") }, // label for the text field
            modifier = Modifier.fillMaxWidth() // fill width of the parent
        )

        Spacer(modifier = Modifier.height(8.dp)) // spacer for vertical spacing

        TextField( // text field for username
            value = userName,
            onValueChange = { userName = it }, // update username state
            label = { Text("Username") }, // label for the text field
            modifier = Modifier.fillMaxWidth() // fill width of the parent
        )

        Spacer(modifier = Modifier.height(8.dp)) // spacer for vertical spacing

        TextField( // text field for email address
            value = emailAddress,
            onValueChange = { emailAddress = it }, // update email address state
            label = { Text("Email Address") }, // label for the text field
            modifier = Modifier.fillMaxWidth() // fill width of the parent
        )

        Spacer(modifier = Modifier.height(8.dp)) // spacer for vertical spacing

        TextField( // text field for password
            value = password,
            onValueChange = { password = it }, // update password state
            label = { Text("Password") }, // label for the text field
            modifier = Modifier.fillMaxWidth(), // fill width of the parent
            visualTransformation = PasswordVisualTransformation() // mask password input
        )

        Spacer(modifier = Modifier.height(16.dp)) // spacer for vertical spacing

        Button(onClick = { // button for sign-up
            onSignUp(firstName, lastName, userName, emailAddress, password) // invoke sign-up callback
        }) {
            Text("Sign Up") // button text
        }
    }
}
