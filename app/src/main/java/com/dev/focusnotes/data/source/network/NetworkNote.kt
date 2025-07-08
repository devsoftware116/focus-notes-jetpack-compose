package com.dev.focusnotes.data.source.network

/**
 * Internal model used to represent a note obtained from the network.
 * See NoteMappings.kt for mapping functions used to convert this model to other models.
 */
data class NetworkNote(
    val id: String,
    val title: String,
    val content: String,
    val createdTime: String, // ISO 8601 format
    //val status: NoteStatus = NoteStatus.UNSTARRED
    val isStarred: Boolean = false // true for starred, false for unstarred
)

enum class NoteStatus {
    STARRED,
    UNSTARRED
}
