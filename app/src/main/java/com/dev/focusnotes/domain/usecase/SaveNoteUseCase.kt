package com.dev.focusnotes.domain.usecase

import com.dev.focusnotes.data.repository.NotesRepository
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val repository: NotesRepository
) {

    suspend operator fun invoke(
        noteId: String?,
        title: String,
        content: String
    ): String {
        return if (noteId == null) {
            repository.createNote(title, content)
        } else {
            repository.updateNote(
                noteId = noteId,
                title = title,
                content = content,
                isStarred = true
            )
            noteId
        }
    }
}
