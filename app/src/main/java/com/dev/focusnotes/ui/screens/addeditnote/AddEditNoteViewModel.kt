package com.dev.focusnotes.ui.screens.addeditnote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.focusnotes.NoteDestinations
import com.dev.focusnotes.R
import com.dev.focusnotes.data.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditNoteUiState(
    val title: String = "",
    val content: String = "",
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val isStarred: Boolean = false
)

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: String? = savedStateHandle[NoteDestinations.NOTE_ID_ARG]

    private val _uiState = MutableStateFlow(AddEditNoteUiState())
    val uiState: StateFlow<AddEditNoteUiState> = _uiState.asStateFlow()

    init {
        if (noteId != null) {
            loadNote(noteId)
        }
    }

    fun saveNote() {
        if (uiState.value.title.isBlank()) {
            _uiState.update {
                it.copy(userMessage = R.string.empty_note_message)
            }
            return
        }

        if (noteId == null) {
            createNewNote()
        } else {
            updateNote()
        }
    }

    fun updateTitle(newTitle: String) {
        _uiState.update {
            it.copy(title = newTitle)
        }
    }

    fun updateContent(newContent: String) {
        _uiState.update {
            it.copy(content = newContent)
        }
    }

    fun snackbarMessageShown() {
        _uiState.update {
            it.copy(userMessage = null)
        }
    }

    //private
    fun createNewNote() = viewModelScope.launch {
        notesRepository.createNote(uiState.value.title, uiState.value.content)
        _uiState.update {
            it.copy(isStarred = true)
        }
    }

    private fun updateNote() {
        if (noteId == null) {
            throw IllegalStateException("updateNote() was called but noteId is null")
        }

        viewModelScope.launch {
            notesRepository.updateNote(
                noteId,
                title = uiState.value.title,
                content = uiState.value.content,
                isStarred = uiState.value.isStarred // Assuming isNoteSaved indicates if the note is starred
            )
            _uiState.update {
                it.copy(isStarred = true)
            }
        }
    }

    private fun loadNote(noteId: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            val note = notesRepository.getNote(noteId)
            if (note != null) {
                _uiState.update {
                    it.copy(
                        title = note.title,
                        content = note.content,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(isLoading = false, userMessage = R.string.note_not_found)
                }
            }
        }
    }
}
