package com.chronelab.roomdatabase.activity // define the package name

import android.content.Intent // import the Intent class for starting activities
import android.net.Uri // import the Uri class for handling URIs
import android.os.Bundle // import the Bundle class for passing data between activities
import android.widget.MediaController // import the MediaController class for media controls
import android.widget.VideoView // import the VideoView class for displaying videos
import androidx.activity.ComponentActivity // import ComponentActivity for creating activities
import androidx.activity.compose.setContent // import setContent function for setting composable content
import androidx.compose.foundation.layout.* // import layout components for Compose UI
import androidx.compose.material3.* // import material design components for Compose UI
import androidx.compose.runtime.Composable // import Composable annotation for creating composable functions
import androidx.compose.ui.Alignment // import Alignment class for alignment options
import androidx.compose.ui.Modifier // import Modifier class for modifying UI components
import androidx.compose.ui.platform.LocalContext // import LocalContext for accessing the current context
import androidx.compose.ui.viewinterop.AndroidView // import AndroidView for integrating Android views with Compose
import androidx.compose.ui.unit.dp // import dp unit for defining dimensions
import com.chronelab.roomdatabase.R // import resources (like video file) from the resources package
import com.chronelab.roomdatabase.ui.theme.RoomDatabaseTheme // import the theme for the app

class VideoActivity : ComponentActivity() { // define VideoActivity class inheriting from ComponentActivity
    override fun onCreate(savedInstanceState: Bundle?) { // override onCreate method for activity initialization
        super.onCreate(savedInstanceState) // call the superclass onCreate method
        setContent { // set the content view using Compose
            RoomDatabaseTheme { // apply the app theme
                VideoScreen() // call the VideoScreen composable function to display content
            }
        }
    }
}

@Composable
fun VideoScreen() { // define a composable function for the video screen
    val context = LocalContext.current // get the current context from Compose

    Scaffold( // create a Scaffold layout component
        modifier = Modifier.fillMaxSize() // make the Scaffold fill the entire screen
    ) {
        Box(modifier = Modifier.fillMaxSize()) { // create a Box layout component to hold other components
            AndroidView( // integrate an Android view within Compose
                factory = { ctx -> // provide a factory function for creating the view
                    VideoView(ctx).apply { // create a VideoView instance
                        val mediaController = MediaController(ctx) // create a MediaController for video playback controls
                        mediaController.setAnchorView(this) // set the anchor view for the media controller
                        setMediaController(mediaController) // associate the media controller with the video view
                        // Ensure correct path to video file
                        setVideoURI(Uri.parse("android.resource://${context.packageName}/${R.raw.intro_video}")) // set the video URI to a resource file
                        requestFocus() // request focus for the video view
                        start() // start playing the video
                        // Handle completion
                        setOnCompletionListener { // set a listener for when the video completes
                            navigateToLogin(context) // navigate to the LoginActivity when the video ends
                        }
                    }
                },
                modifier = Modifier.fillMaxSize() // make the video view fill the entire screen
            )

            // Skip Button
            Button( // create a button
                onClick = { // set the action for button click
                    navigateToLogin(context) // navigate to LoginActivity when the button is clicked
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd) // align the button to the bottom end of the screen
                    .padding(16.dp) // add padding around the button
            ) {
                Text(text = "Skip") // set the button text to "Skip"
            }
        }
    }
}

fun navigateToLogin(context: android.content.Context) { // define a function to navigate to LoginActivity
    context.startActivity(Intent(context, LoginActivity::class.java)) // start LoginActivity
    (context as ComponentActivity).finish() // finish the current activity
}
