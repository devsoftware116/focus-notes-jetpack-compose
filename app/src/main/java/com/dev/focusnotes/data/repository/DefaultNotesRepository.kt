package com.dev.focusnotes.data.repository


import com.dev.focusnotes.data.source.local.NoteDao
import com.dev.focusnotes.data.source.network.NetworkDataSource
import com.dev.focusnotes.data.toExternal
import com.dev.focusnotes.data.toLocal
import com.dev.focusnotes.data.toNetwork
import com.dev.focusnotes.di.ApplicationScope
import com.dev.focusnotes.di.DefaultDispatcher
import com.dev.focusnotes.domain.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultNotesRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: NoteDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope
) : NotesRepository {

    override suspend fun createNote(title: String, content: String): String {
        val noteId = withContext(dispatcher) { UUID.randomUUID().toString() }
        val note = Note(
            id = noteId,
            title = title,
            content = content,
            isStarred = false,
        )
        localDataSource.upsert(note.toLocal())
        saveNotesToNetwork()
        return noteId
    }

    override fun getNotesStream(): Flow<List<Note>> {
        return localDataSource.observeAll().map { notes ->
            notes.toExternal()
        }
    }

    override suspend fun getNote(noteId: String, forceUpdate: Boolean): Note? {
        if (forceUpdate) refresh()
        return localDataSource.getById(noteId)?.toExternal()
    }

    override suspend fun refresh() {
        println("Refreshing notes from network...")
        withContext(dispatcher) {
            val remoteNotes = networkDataSource.getNotes()
            localDataSource.deleteAll()
            localDataSource.upsertAll(remoteNotes.toLocal())
        }
    }

    override suspend fun updateNote(noteId: String, title: String, content: String, isStarred: Boolean) {
        val note = getNote(noteId)?.copy(title = title, content = content, isStarred = isStarred)
            ?: throw Exception("Note (id $noteId) not found")
        println("Updating note: $note")
        println("Updating note in local data source...${note.toLocal()}")
        localDataSource.upsert(note.toLocal())
        saveNotesToNetwork()
    }

    private fun saveNotesToNetwork() {
        scope.launch {
            try {
                val localNotes = localDataSource.getAll()
                val networkNotes = withContext(dispatcher) {
                    localNotes.toNetwork()
                }
                networkDataSource.saveNotes(networkNotes)
            } catch (e: Exception) {
                // Log or expose error status if needed
            }
        }
    }
}
