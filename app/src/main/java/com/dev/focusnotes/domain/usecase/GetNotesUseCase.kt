package com.dev.focusnotes.domain.usecase

import com.dev.focusnotes.data.repository.NotesRepository
import com.dev.focusnotes.domain.model.Note

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NotesRepository
) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getNotesStream()
            .distinctUntilChanged()
            .map { notes ->
                notes.sortedByDescending { it.createdTime }
            }
    }
}