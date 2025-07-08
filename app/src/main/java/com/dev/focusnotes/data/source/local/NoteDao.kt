package com.dev.focusnotes.data.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the notes table.
 */
@Dao
interface NoteDao {

    /**
     * Observes list of notes.
     *
     * @return all notes.
     */
    @Query("SELECT * FROM note")
    fun observeAll(): Flow<List<LocalNote>>

    /**
     * Observes a single note.
     *
     * @param noteId the note id.
     * @return the note with noteId.
     */
    @Query("SELECT * FROM note WHERE id = :noteId")
    fun observeById(noteId: String): Flow<LocalNote>

    /**
     * Select all notes from the notes table.
     *
     * @return all notes.
     */
    @Query("SELECT * FROM note")
    suspend fun getAll(): List<LocalNote>

    /**
     * Select a note by id.
     *
     * @param noteId the note id.
     * @return the note with noteId.
     */
    @Query("SELECT * FROM note WHERE id = :noteId")
    suspend fun getById(noteId: String): LocalNote?

    /**
     * Insert or update a note in the database. If a note already exists, replace it.
     *
     * @param note the note to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(note: LocalNote)

    /**
     * Insert or update notes in the database. If a note already exists, replace it.
     *
     * @param notes the notes to be inserted or updated.
     */
    @Upsert
    suspend fun upsertAll(notes: List<LocalNote>)

    /**
     * Delete a note by id.
     *
     * @return the number of notes deleted. This should always be 1.
     */
    @Query("DELETE FROM note WHERE id = :noteId")
    suspend fun deleteById(noteId: String): Int

    /**
     * Delete all notes.
     */
    @Query("DELETE FROM note")
    suspend fun deleteAll()
}
