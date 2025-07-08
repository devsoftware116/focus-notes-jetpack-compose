package com.dev.focusnotes.data.source.network

interface NetworkDataSource {

    suspend fun getNotes(): List<NetworkNote>
    suspend fun saveNotes(notes: List<NetworkNote>)
}