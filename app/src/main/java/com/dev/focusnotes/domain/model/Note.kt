package com.dev.focusnotes.domain.model

import java.time.LocalDateTime

/**
 * Immutable model class for a Note.
 */
data class Note(
    val id: String,
    val title: String = "",
    val content: String = "",
    val isStarred: Boolean = false,
    val createdTime: LocalDateTime = LocalDateTime.now(),
) {
    val titleForList: String
        get() = if (title.isNotEmpty()) title else content

    val isUnpinned: Boolean
        get() = !isStarred

    val isEmpty: Boolean
        get() = title.isEmpty() || content.isEmpty()
}
