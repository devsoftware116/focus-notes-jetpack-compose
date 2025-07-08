package com.dev.focusnotes.data.source.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class FakeNetworkDataSource @Inject constructor() : NetworkDataSource {

    private val accessMutex = Mutex()
    private var notes = listOf(
        NetworkNote(
            id = "1",
            title = "Sample Note 1",
            content = "This is the content of sample note 1.",
            createdTime = "2023-10-01T12:00:00Z",
            isStarred = false
        ),
        NetworkNote(
            id = "2",
            title = "Sample Note 2",
            content = "This is the content of sample note 2.",
            createdTime = "2023-10-02T12:00:00Z",
            isStarred = false
        ),
        NetworkNote(
            id = "3",
            title = "Sample Note 3",
            content = "This is the content of sample note 3.",
            createdTime = "2023-10-03T12:00:00Z",
            isStarred = false
        )
    )

    override suspend fun getNotes(): List<NetworkNote> = accessMutex.withLock {
        delay(LATENCY)
        return notes
    }

    override suspend fun saveNotes(notes: List<NetworkNote>) {
        accessMutex.withLock {
            delay(LATENCY)
            this.notes = notes
        }
    }
}

private const val LATENCY = 2000L