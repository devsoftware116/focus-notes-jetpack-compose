package com.dev.focusnotes.domain.usecase

import com.dev.focusnotes.data.repository.NotesRepository
import com.dev.focusnotes.domain.model.Note

import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(
    private val repository: NotesRepository
) {

}
