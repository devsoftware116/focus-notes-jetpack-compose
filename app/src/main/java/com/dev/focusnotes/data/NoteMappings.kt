package com.dev.focusnotes.data

import com.dev.focusnotes.data.source.local.LocalNote
import com.dev.focusnotes.data.source.network.NetworkNote
import com.dev.focusnotes.domain.model.Note
import java.time.Instant
import java.time.ZoneId

/**
 * Data model mapping extension functions.
 *
 * - Note: External model used across domain/UI layers
 * - NetworkNote: Model from the remote source
 * - LocalNote: Model for the Room database
 */

// External to Local
fun Note.toLocal() = LocalNote(
    id = id, title = title, content = content, isStarred = isStarred, createdTime = createdTime
)

fun List<Note>.toLocal() = map(Note::toLocal)

// Local to External
fun LocalNote.toExternal() = Note(
    id = id, title = title, content = content, isStarred = isStarred, createdTime = createdTime
)

@JvmName("localToExternal")
fun List<LocalNote>.toExternal() = map(LocalNote::toExternal)

fun NetworkNote.toLocal() = LocalNote(
    id = id,
    title = title,
    content = content,
    isStarred = isStarred,
    createdTime = Instant.ofEpochMilli(createdTime.toLong()).atZone(ZoneId.systemDefault())
        .toLocalDateTime()
)

@JvmName("networkToLocal")
fun List<NetworkNote>.toLocal() = map(NetworkNote::toLocal)

// Local to Network
fun LocalNote.toNetwork() = NetworkNote(
    id = id, title = title, content = content,
    //status = if (isStarred) NoteStatus.STARRED else NoteStatus.UNSTARRED,
    isStarred = isStarred, createdTime = createdTime.toString() // ISO 8601 format
)

fun List<LocalNote>.toNetwork() = map(LocalNote::toNetwork)

// External to Network
fun Note.toNetwork() = toLocal().toNetwork()

@JvmName("externalToNetwork")
fun List<Note>.toNetwork() = map(Note::toNetwork)

// Network to External
fun NetworkNote.toExternal() = toLocal().toExternal()

@JvmName("networkToExternal")
fun List<NetworkNote>.toExternal() = map(NetworkNote::toExternal)
