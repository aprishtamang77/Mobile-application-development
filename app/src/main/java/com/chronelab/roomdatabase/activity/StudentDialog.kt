package com.chronelab.roomdatabase.activity // define the package name

import androidx.compose.foundation.layout.* // import layout components for Compose UI
import androidx.compose.material3.* // import material design components for Compose UI
import androidx.compose.runtime.* // import runtime components for Compose UI
import androidx.compose.ui.text.input.PasswordVisualTransformation // import password visual transformation for secure text entry
import com.chronelab.roomdatabase.model.User // import the User model class

@Composable
fun StudentDialog( // define a composable function named StudentDialog
    initialUser: User? = null, // optional parameter to pass an initial user object for updating
    onDismiss: () -> Unit, // callback to handle dialog dismissal
    onSubmit: (User) -> Unit // callback to handle form submission
) {
    var firstName by remember { mutableStateOf(initialUser?.firstName ?: "") } // state for the first name field, initialized with existing user data or empty string
    var lastName by remember { mutableStateOf(initialUser?.lastName ?: "") } // state for the last name field, initialized with existing user data or empty string
    var userName by remember { mutableStateOf(initialUser?.userName ?: "") } // state for the username field, initialized with existing user data or empty string
    var emailAddress by remember { mutableStateOf(initialUser?.emailAddress ?: "") } // state for the email address field, initialized with existing user data or empty string
    var password by remember { mutableStateOf(initialUser?.password ?: "") } // state for the password field, initialized with existing user data or empty string
    var isAdmin by remember { mutableStateOf(initialUser?.isAdmin ?: false) } // state for the admin checkbox, initialized with existing user data or false

    AlertDialog( // create an alert dialog
        onDismissRequest = onDismiss, // handle dialog dismissal
        title = { Text(if (initialUser == null) "Add Student" else "Update Student") }, // set the dialog title based on whether it's adding or updating
        text = { // dialog content
            Column { // vertical column layout for dialog content
                TextField( // text field for first name
                    value = firstName, // set the value of the text field
                    onValueChange = { firstName = it }, // update the state when the value changes
                    label = { Text("First Name") } // label for the text field
                )
                TextField( // text field for last name
                    value = lastName, // set the value of the text field
                    onValueChange = { lastName = it }, // update the state when the value changes
                    label = { Text("Last Name") } // label for the text field
                )
                TextField( // text field for username
                    value = userName, // set the value of the text field
                    onValueChange = { userName = it }, // update the state when the value changes
                    label = { Text("Username") } // label for the text field
                )
                TextField( // text field for email address
                    value = emailAddress, // set the value of the text field
                    onValueChange = { emailAddress = it }, // update the state when the value changes
                    label = { Text("Email Address") } // label for the text field
                )
                TextField( // text field for password
                    value = password, // set the value of the text field
                    onValueChange = { password = it }, // update the state when the value changes
                    label = { Text("Password") }, // label for the text field
                    visualTransformation = PasswordVisualTransformation() // obscure the text for password entry
                )
            }
        },
        confirmButton = { // button to confirm and submit the dialog
            TextButton(onClick = { // set the action for button click
                if (firstName.isNotEmpty() && lastName.isNotEmpty() && userName.isNotEmpty() && emailAddress.isNotEmpty() && password.isNotEmpty()) { // check if all fields are filled
                    val user = User( // create a new User object with the form data
                        id = initialUser?.id ?: 0, // use existing user ID or default to 0
                        firstName = firstName, // set the first name
                        lastName = lastName, // set the last name
                        userName = userName, // set the username
                        emailAddress = emailAddress, // set the email address
                        password = password, // set the password
                        isAdmin = isAdmin // set the admin status
                    )
                    onSubmit(user) // call the onSubmit callback with the new user data
                } else { // handle the case where fields are empty
                    // Handle empty fields, possibly show a snackbar or dialog
                    println("Error: Please fill in all fields.") // print error message to console
                }
            }) {
                Text("Submit") // button text
            }
        },
        dismissButton = { // button to cancel and dismiss the dialog
            TextButton(onClick = onDismiss) { // set the action for button click
                Text("Cancel") // button text
            }
        }
    )
}
