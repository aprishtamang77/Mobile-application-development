package com.chronelab.roomdatabase.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chronelab.roomdatabase.database.entity.Note
import com.chronelab.roomdatabase.ui.theme.RoomDatabaseTheme

@Composable
fun ViewHome(notes: List<Note>,
             rightBtnAction: () -> Unit,
             leftBtnAction: () -> Unit,
             onEdit: (Note) -> Unit,
             onDelete: (Note) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
    ) {
        HomeHeader(title = "Home", leftButtonAction = leftBtnAction, rightButtonAction = rightBtnAction)
        NotesList(notes = notes, onEdit = onEdit , onDelete = onDelete)
    }
}

@Composable
fun NotesList(
    notes: List<Note>,
    onEdit: (Note) -> Unit,
    onDelete: (Note) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(notes) { note ->
            NoteItem(note = note, onEdit = onEdit, onDelete = onDelete)
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onEdit: (Note) -> Unit,
    onDelete: (Note) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleMedium, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = note.desc)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { onEdit(note) }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { onDelete(note) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ViewHomePreview() {
    RoomDatabaseTheme {
        ViewHome(notes = listOf<Note>(), leftBtnAction = {}, rightBtnAction = {}, onDelete = { note: Note ->  }, onEdit = { note: Note ->  })
    }
}
