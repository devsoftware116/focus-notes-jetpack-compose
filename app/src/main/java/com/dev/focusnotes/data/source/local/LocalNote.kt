package com.dev.focusnotes.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Internal model used to represent a note stored locally in a Room database.
 * See ModelMappingExt.kt for mapping functions used to convert this model to other models.
 */
@Entity(tableName = "note")
data class LocalNote(
    @PrimaryKey val id: String,
    var title: String,
    var content: String,
    var createdTime: LocalDateTime = LocalDateTime.now(),
    var isStarred: Boolean = false // ✅ keep this in sync with domain model
)
