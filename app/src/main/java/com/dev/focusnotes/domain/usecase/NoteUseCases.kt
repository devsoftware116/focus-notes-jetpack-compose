package com.dev.focusnotes.domain.usecase

data class NoteUseCases(
    val getNotes: GetNotesUseCase,
    val getNoteById: GetNoteByIdUseCase,
    //val saveNote: SaveNoteUseCase,
)
