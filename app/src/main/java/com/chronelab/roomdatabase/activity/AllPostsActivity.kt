package com.chronelab.roomdatabase.activity // defines the package name for the activity

import android.os.Bundle // imports the Bundle class for managing activity state
import androidx.activity.ComponentActivity // imports the base class for activities that use Compose
import androidx.activity.compose.setContent // imports the function to set the content of the activity
import androidx.compose.foundation.layout.* // imports layout-related functions from Jetpack Compose
import androidx.compose.foundation.lazy.LazyColumn // imports LazyColumn for displaying lists in Compose
import androidx.compose.foundation.lazy.items // imports the items function to display a list of items
import androidx.compose.material3.* // imports Material 3 components for UI
import androidx.compose.runtime.* // imports runtime functions for state management in Compose
import androidx.compose.ui.Modifier // imports Modifier class for modifying UI elements
import androidx.compose.ui.unit.dp // imports dp (density-independent pixels) unit for sizing
import com.chronelab.roomdatabase.database.NoteDatabase.Companion.getDatabase // imports the function to get the database instance
import com.chronelab.roomdatabase.database.dao.PostDao // imports the Data Access Object for Post entity
import com.chronelab.roomdatabase.model.Post // imports the Post data model
import kotlinx.coroutines.launch // imports launch function for coroutines

class AllPostsActivity : ComponentActivity() { // defines the AllPostsActivity class extending ComponentActivity
    private lateinit var postDao: PostDao // declares a variable for PostDao

    @OptIn(ExperimentalMaterial3Api::class) // annotates the usage of experimental API in Material 3
    override fun onCreate(savedInstanceState: Bundle?) { // overrides the onCreate method to initialize the activity
        super.onCreate(savedInstanceState) // calls the superclass method
        val db = getDatabase(this) // initializes the database instance
        postDao = db.postDao() // gets the PostDao from the database

        setContent { // sets the content for the activity using Jetpack Compose
            val coroutineScope = rememberCoroutineScope() // creates a coroutine scope for launching coroutines
            var posts by remember { mutableStateOf<List<Post>>(emptyList()) } // defines a state for storing posts

            // Load active posts from the database
            LaunchedEffect(Unit) { // launches a coroutine when the composable is first loaded
                postDao.getActivePosts().collect { postList -> // collects active posts from the database
                    posts = postList // updates the state with the new list of posts
                }
            }

            Scaffold( // provides a basic layout structure with an app bar and content area
                modifier = Modifier.fillMaxSize(), // sets the modifier to fill the maximum size of the parent
                topBar = { // defines the top bar of the Scaffold
                    TopAppBar(
                        title = { Text("All Posts") } // sets the title of the app bar
                    )
                }
            ) { paddingValues -> // provides padding values for the content area
                Column(
                    modifier = Modifier
                        .fillMaxSize() // sets the modifier to fill the maximum size of the parent
                        .padding(paddingValues) // applies padding values from the Scaffold
                        .padding(16.dp) // adds additional padding of 16 dp
                ) {
                    // Display the list of posts
                    LazyColumn { // defines a lazy column to display a list of items
                        items(posts) { post -> // iterates over each post in the list
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 4.dp) // adds vertical padding of 4 dp
                                    .fillMaxWidth() // sets the modifier to fill the maximum width of the parent
                            ) {
                                Text(
                                    text = post.title, // displays the title of the post
                                    style = MaterialTheme.typography.titleMedium // applies the medium title style
                                )
                                Spacer(modifier = Modifier.height(4.dp)) // adds a spacer with a height of 4 dp
                                Text(
                                    text = post.content, // displays the content of the post
                                    style = MaterialTheme.typography.bodyMedium // applies the medium body style
                                )

                                // Delete button
                                Button(
                                    onClick = { // defines the click action for the button
                                        coroutineScope.launch { // launches a coroutine in the coroutine scope
                                            // Update the post to set isDeleted = true
                                            postDao.update(post.copy(isDeleted = true)) // marks the post as deleted
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error // sets the button color to error color
                                    ),
                                    modifier = Modifier.padding(top = 8.dp) // adds top padding of 8 dp
                                ) {
                                    Text("Delete Post") // displays the text on the button
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
