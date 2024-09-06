package com.chronelab.roomdatabase.activity

import android.content.Intent // import for Intent
import android.os.Bundle // import for Bundle
import androidx.activity.ComponentActivity // import for ComponentActivity
import androidx.activity.compose.setContent // import for setContent function
import androidx.compose.foundation.layout.* // import for layout components
import androidx.compose.foundation.lazy.LazyColumn // import for LazyColumn
import androidx.compose.foundation.lazy.items // import for items in LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape // import for RoundedCornerShape
import androidx.compose.material.icons.Icons // import for Icons
import androidx.compose.material.icons.filled.ExitToApp // import for ExitToApp icon
import androidx.compose.material.icons.filled.MoreVert // import for MoreVert icon
import androidx.compose.material3.* // import for material3 components
import androidx.compose.runtime.* // import for Compose runtime components
import androidx.compose.ui.Alignment // import for Alignment
import androidx.compose.ui.Modifier // import for Modifier
import androidx.compose.ui.unit.dp // import for dp unit
import com.chronelab.roomdatabase.database.NoteDatabase.Companion.getDatabase // import for database
import com.chronelab.roomdatabase.database.dao.PostDao // import for PostDao
import com.chronelab.roomdatabase.database.dao.CommentDao // import for CommentDao
import com.chronelab.roomdatabase.model.Comment // import for Comment model
import com.chronelab.roomdatabase.model.Post // import for Post model
import kotlinx.coroutines.launch // import for coroutines launch function

class StudentActivity : ComponentActivity() {
    private lateinit var postDao: PostDao // declare postDao
    private lateinit var commentDao: CommentDao // declare commentDao

    @OptIn(ExperimentalMaterial3Api::class) // opt-in to experimental API
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // call superclass method
        val db = getDatabase(this) // get database instance
        postDao = db.postDao() // initialize postDao
        commentDao = db.commentDao() // initialize commentDao

        setContent { // set the content of the activity
            StudentScreen(
                postDao = postDao, // pass postDao to StudentScreen
                commentDao = commentDao, // pass commentDao to StudentScreen
                onLogout = { logout() } // pass logout function to StudentScreen
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class) // opt-in to experimental API
    @Composable
    fun StudentScreen(
        postDao: PostDao, // parameter for postDao
        commentDao: CommentDao, // parameter for commentDao
        onLogout: () -> Unit // parameter for logout function
    ) {
        val coroutineScope = rememberCoroutineScope() // create coroutine scope
        var activePosts by remember { mutableStateOf<List<Post>>(emptyList()) } // state for active posts
        var deletedPosts by remember { mutableStateOf<List<Post>>(emptyList()) } // state for deleted posts
        var showPostDialog by remember { mutableStateOf(false) } // state for post dialog visibility
        var postTitle by remember { mutableStateOf("") } // state for post title
        var postText by remember { mutableStateOf("") } // state for post text
        var selectedPostId by remember { mutableStateOf<Int?>(null) } // state for selected post id
        var showAddCommentDialog by remember { mutableStateOf(false) } // state for add comment dialog visibility
        var showViewCommentsDialog by remember { mutableStateOf(false) } // state for view comments dialog visibility
        var comments by remember { mutableStateOf<List<Comment>>(emptyList()) } // state for comments
        var commentContent by remember { mutableStateOf("") } // state for comment content
        var editingPost by remember { mutableStateOf<Post?>(null) } // state for editing post

        LaunchedEffect(Unit) { // launch effect when component is first launched
            postDao.getActivePosts().collect { activePosts = it } // collect active posts
            postDao.getDeletedPosts().collect { deletedPosts = it } // collect deleted posts
        }

        LaunchedEffect(selectedPostId) { // launch effect when selectedPostId changes
            selectedPostId?.let {
                commentDao.getCommentsByPostId(it).collect { comments = it } // collect comments for selected post
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(), // set modifier to fill max size
            topBar = {
                TopAppBar(
                    title = { Text("Student") }, // set title of the top app bar
                    actions = {
                        IconButton(onClick = onLogout) { // button for logout action
                            Icon(Icons.Filled.ExitToApp, contentDescription = "Logout") // icon for logout
                        }
                    }
                )
            }
        ) { paddingValues -> // padding values for content
            Column(
                modifier = Modifier
                    .fillMaxSize() // fill max size of column
                    .padding(paddingValues) // set padding values
                    .padding(16.dp), // additional padding
                horizontalAlignment = Alignment.CenterHorizontally // center content horizontally
            ) {
                if (deletedPosts.isNotEmpty()) { // check if there are deleted posts
                    DeletedPostsSection(deletedPosts = deletedPosts) // show deleted posts section
                }

                AddPostButton(showPostDialog = showPostDialog, onAddPostClicked = { showPostDialog = true }) // button to add post

                ActivePostsSection(
                    activePosts = activePosts, // pass active posts to section
                    commentDao = commentDao, // pass commentDao to section
                    onAddCommentClick = { postId -> // handle add comment click
                        selectedPostId = postId // set selected post id
                        showAddCommentDialog = true // show add comment dialog
                    },
                    onViewCommentsClick = { postId -> // handle view comments click
                        selectedPostId = postId // set selected post id
                        showViewCommentsDialog = true // show view comments dialog
                    },
                    onEditPost = { post -> // handle edit post action
                        editingPost = post // set editing post
                        postTitle = post.title // set post title
                        postText = post.content // set post text
                        showPostDialog = true // show post dialog
                    },
                    onDeletePost = { post -> // handle delete post action
                        coroutineScope.launch { // launch coroutine for delete action
                            postDao.delete(post) // delete post from database
                        }
                    }
                )

                if (showPostDialog) { // check if post dialog should be shown
                    AddPostDialog(
                        postTitle = postTitle, // pass post title to dialog
                        postText = postText, // pass post text to dialog
                        onPostTitleChange = { postTitle = it }, // handle post title change
                        onPostTextChange = { postText = it }, // handle post text change
                        onAddPost = {
                            coroutineScope.launch { // launch coroutine for add post action
                                if (postTitle.isNotBlank() && postText.isNotBlank()) { // check if title and text are not blank
                                    if (editingPost != null) { // check if editing post exists
                                        postDao.update(editingPost!!.copy(title = postTitle, content = postText)) // update post
                                        editingPost = null // clear editing post
                                    } else {
                                        postDao.insert(Post(title = postTitle, content = postText)) // insert new post
                                    }
                                    postTitle = "" // clear post title
                                    postText = "" // clear post text
                                    showPostDialog = false // hide post dialog
                                }
                            }
                        },
                        onDismiss = {
                            showPostDialog = false // hide post dialog
                            editingPost = null // clear editing post
                        }
                    )
                }

                if (showAddCommentDialog && selectedPostId != null) { // check if add comment dialog should be shown
                    AddCommentDialog(
                        commentContent = commentContent, // pass comment content to dialog
                        onCommentContentChange = { commentContent = it }, // handle comment content change
                        onAddComment = {
                            coroutineScope.launch { // launch coroutine for add comment action
                                if (commentContent.isNotBlank()) { // check if comment content is not blank
                                    commentDao.insert(Comment(postId = selectedPostId!!, content = commentContent)) // insert comment
                                    commentContent = "" // clear comment content
                                    showAddCommentDialog = false // hide add comment dialog
                                }
                            }
                        },
                        onDismiss = {
                            showAddCommentDialog = false // hide add comment dialog
                            commentContent = "" // clear comment content
                        }
                    )
                }

                if (showViewCommentsDialog && selectedPostId != null) { // check if view comments dialog should be shown
                    ViewCommentsDialog(
                        comments = comments, // pass comments to dialog
                        onDismiss = { showViewCommentsDialog = false } // handle dismiss action
                    )
                }
            }
        }
    }


    @Composable
    fun DeletedPostsSection(deletedPosts: List<Post>) { // composable function for deleted posts section
        Text(
            text = "Some of your posts have been deleted by the admin.", // message for deleted posts
            color = MaterialTheme.colorScheme.error, // set color to error
            modifier = Modifier.padding(8.dp) // set padding
        )
        LazyColumn {
            items(deletedPosts) { post -> // iterate over deleted posts
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), // set card color
                    shape = RoundedCornerShape(8.dp), // set card shape
                    modifier = Modifier.padding(8.dp) // set card padding
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth() // fill max width of column
                            .padding(16.dp) // set padding
                    ) {
                        Text(text = post.title, style = MaterialTheme.typography.titleMedium) // display post title
                        Spacer(modifier = Modifier.height(8.dp)) // space between title and content
                        Text(text = post.content, style = MaterialTheme.typography.bodyMedium) // display post content
                    }
                }
            }
        }
    }

    @Composable
    fun AddPostButton(showPostDialog: Boolean, onAddPostClicked: () -> Unit) { // composable function for add post button
        Button(
            onClick = onAddPostClicked, // handle button click
            modifier = Modifier
                .fillMaxWidth() // fill max width of button
                .padding(16.dp), // set padding
            shape = RoundedCornerShape(8.dp) // set button shape
        ) {
            Text("Add New Post") // button text
        }
    }

    @Composable
    fun ActivePostsSection(
        activePosts: List<Post>, // list of active posts
        commentDao: CommentDao, // commentDao for adding comments
        onAddCommentClick: (Int) -> Unit, // callback for add comment click
        onViewCommentsClick: (Int) -> Unit, // callback for view comments click
        onEditPost: (Post) -> Unit, // callback for edit post
        onDeletePost: (Post) -> Unit // callback for delete post
    ) {
        LazyColumn {
            items(activePosts) { post -> // iterate over active posts
                Card(
                    modifier = Modifier
                        .padding(8.dp) // set padding
                        .fillMaxWidth(), // fill max width of card
                    shape = RoundedCornerShape(8.dp) // set card shape
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp) // set padding
                            .fillMaxWidth() // fill max width of column
                    ) {
                        Text(text = post.title, style = MaterialTheme.typography.titleMedium) // display post title
                        Spacer(modifier = Modifier.height(8.dp)) // space between title and content
                        Text(text = post.content, style = MaterialTheme.typography.bodyMedium) // display post content
                        Spacer(modifier = Modifier.height(8.dp)) // space between content and actions
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween, // arrange actions with space between
                            modifier = Modifier.fillMaxWidth() // fill max width of row
                        ) {
                            IconButton(onClick = { onAddCommentClick(post.id) }) { // button for add comment action
                                Icon(Icons.Filled.MoreVert, contentDescription = "Add Comment") // icon for add comment
                            }
                            IconButton(onClick = { onViewCommentsClick(post.id) }) { // button for view comments action
                                Icon(Icons.Filled.MoreVert, contentDescription = "View Comments") // icon for view comments
                            }
                            IconButton(onClick = { onEditPost(post) }) { // button for edit post action
                                Icon(Icons.Filled.MoreVert, contentDescription = "Edit Post") // icon for edit post
                            }
                            IconButton(onClick = { onDeletePost(post) }) { // button for delete post action
                                Icon(Icons.Filled.MoreVert, contentDescription = "Delete Post") // icon for delete post
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AddPostDialog(
        postTitle: String, // current post title
        postText: String, // current post text
        onPostTitleChange: (String) -> Unit, // callback for title change
        onPostTextChange: (String) -> Unit, // callback for text change
        onAddPost: () -> Unit, // callback for add post
        onDismiss: () -> Unit // callback for dismiss dialog
    ) {
        AlertDialog(
            onDismissRequest = onDismiss, // handle dialog dismiss
            title = { Text("Add/Edit Post") }, // dialog title
            text = {
                Column {
                    TextField(
                        value = postTitle, // set value for title
                        onValueChange = onPostTitleChange, // handle title change
                        label = { Text("Post Title") }, // label for title field
                        modifier = Modifier.fillMaxWidth() // set width of title field
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // space between title and content fields
                    TextField(
                        value = postText, // set value for text
                        onValueChange = onPostTextChange, // handle text change
                        label = { Text("Post Content") }, // label for content field
                        modifier = Modifier.fillMaxWidth() // set width of content field
                    )
                }
            },
            confirmButton = {
                Button(onClick = onAddPost) { // button for adding post
                    Text("Add Post") // button text
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) { // button for dismissing dialog
                    Text("Cancel") // button text
                }
            }
        )
    }

    @Composable
    fun AddCommentDialog(
        commentContent: String, // current comment content
        onCommentContentChange: (String) -> Unit, // callback for content change
        onAddComment: () -> Unit, // callback for add comment
        onDismiss: () -> Unit // callback for dismiss dialog
    ) {
        AlertDialog(
            onDismissRequest = onDismiss, // handle dialog dismiss
            title = { Text("Add Comment") }, // dialog title
            text = {
                TextField(
                    value = commentContent, // set value for comment content
                    onValueChange = onCommentContentChange, // handle content change
                    label = { Text("Comment") }, // label for content field
                    modifier = Modifier.fillMaxWidth() // set width of content field
                )
            },
            confirmButton = {
                Button(onClick = onAddComment) { // button for adding comment
                    Text("Add Comment") // button text
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) { // button for dismissing dialog
                    Text("Cancel") // button text
                }
            }
        )
    }

    @Composable
    fun ViewCommentsDialog(
        comments: List<Comment>, // list of comments
        onDismiss: () -> Unit // callback for dismiss dialog
    ) {
        AlertDialog(
            onDismissRequest = onDismiss, // handle dialog dismiss
            title = { Text("Comments") }, // dialog title
            text = {
                LazyColumn {
                    items(comments) { comment -> // iterate over comments
                        Card(
                            modifier = Modifier
                                .padding(8.dp) // set padding
                                .fillMaxWidth(), // fill max width of card
                            shape = RoundedCornerShape(8.dp) // set card shape
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp) // set padding
                                    .fillMaxWidth() // fill max width of column
                            ) {
                                Text(text = comment.content, style = MaterialTheme.typography.bodyMedium) // display comment content
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = onDismiss) { // button for dismissing dialog
                    Text("Close") // button text
                }
            }
        )
    }

    private fun logout() { // method to handle logout
        // clear user session and navigate to login screen
        startActivity(Intent(this, LoginActivity::class.java)) // start login activity
        finish() // finish current activity
    }
}