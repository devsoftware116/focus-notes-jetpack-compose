package com.dev.focusnotes.data.source.network

import javax.inject.Inject

class RetrofitNetworkDataSource @Inject constructor(
    private val apiService: NotesApiService
) : NetworkDataSource {

    override suspend fun getNotes(): List<NetworkNote> {
        return apiService.getNotes()
    }

    override suspend fun saveNotes(notes: List<NetworkNote>) {
        notes.forEach {
            apiService.saveNote(it)
        }
    }
}
