package com.chronelab.roomdatabase.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.chronelab.roomdatabase.RoomApp
import com.chronelab.roomdatabase.database.entity.Note
import com.chronelab.roomdatabase.ui.theme.RoomDatabaseTheme
import com.chronelab.roomdatabase.ui.view.ViewAddNote
import kotlinx.coroutines.launch

class NoteAddActivity : ComponentActivity() {
    companion object {
        val TAG = NoteAddActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RoomDatabaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val coroutineScope = rememberCoroutineScope()
                    ViewAddNote(
                        btnSaveNoteAction = { title, desc ->
                            Log.i(TAG, title)
                            Log.i(TAG, desc)
                            coroutineScope.launch {
                                val note = Note(title = title, desc = desc)
                                saveNote(note)
                            }
                            finish()
                        },
                        leftButtonAction = {
                            finish()
                        },
                        rightButtonAction = {
                            val intent = Intent(this, LoginActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // This will clear all activities above LoginActivity
                            }
                            startActivity(intent)
                        })
                }
            }
        }
    }

    private suspend fun saveNote(newNote: Note) {
        val roomApp = application as RoomApp
        roomApp.dbContainer.notesRepositoryInterface.insertNote(newNote)
    }
}
