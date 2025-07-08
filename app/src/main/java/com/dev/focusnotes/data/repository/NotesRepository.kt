package com.dev.focusnotes.data.repository

import com.dev.focusnotes.domain.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Interface to the data layer for Note operations.
 */
interface NotesRepository {

    fun getNotesStream(): Flow<List<Note>>

    suspend fun createNote(title: String, content: String): String

    suspend fun refresh()

    suspend fun getNote(noteId: String, forceUpdate: Boolean = false): Note?

    suspend fun updateNote(noteId: String, title: String, content: String,isStarred: Boolean)


}
