package com.chronelab.roomdatabase.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.chronelab.roomdatabase.model.Post

@Composable
fun ViewHome(
    posts: List<Post>,
    onAddPost: () -> Unit,
    onEditPost: (Post) -> Unit,
    onDeletePost: (Post) -> Unit
) {
    Column {

        LazyColumn {
            items(posts) { post ->
                Text(text = post.title) // Assuming `Post` has a `title` field
                Button(onClick = { onEditPost(post) }) {
                    Text("Edit")
                }
                Button(onClick = { onDeletePost(post) }) {
                    Text("Delete")
                }
            }
        }
    }
}
