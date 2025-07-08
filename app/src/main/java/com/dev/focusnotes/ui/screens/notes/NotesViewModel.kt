package com.dev.focusnotes.ui.screens.notes

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.focusnotes.ADD_EDIT_RESULT_OK
import com.dev.focusnotes.EDIT_RESULT_OK
import com.dev.focusnotes.R
import com.dev.focusnotes.data.repository.NotesRepository
import com.dev.focusnotes.domain.model.Note


import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import javax.inject.Inject
import kotlin.collections.filter

import com.dev.focusnotes.ui.screens.notes.NotesFilterType.ALL_NOTES

import com.dev.focusnotes.ui.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel


data class NotesUiState(
    val items: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val filteringUiInfo: FilteringUiInfo = FilteringUiInfo(),
    val userMessage: Int? = null
)

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepository: NotesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _savedFilterType =
        savedStateHandle.getStateFlow(NOTES_FILTER_SAVED_STATE_KEY, ALL_NOTES)

    private val _filterUiInfo = _savedFilterType.map { getFilterUiInfo(it) }.distinctUntilChanged()
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _isLoading = MutableStateFlow(false)

    private val _filteredNotesAsync = combine(
        notesRepository.getNotesStream(), _savedFilterType
    ) { notes, type ->
        filterNotes(notes, type)
    }
        .map { Async.Success(it) }
        .catch<Async<List<Note>>> {
            emit(Async.Error(R.string.loading_notes_error))
        }

    val uiState: StateFlow<NotesUiState> = combine(
        _filterUiInfo, _isLoading, _userMessage, _filteredNotesAsync
    ) { filterUiInfo, isLoading, userMessage, notesAsync ->
        when (notesAsync) {
            Async.Loading -> NotesUiState(isLoading = true)
            is Async.Error -> NotesUiState(userMessage = notesAsync.errorMessage)
            is Async.Success -> NotesUiState(
                items = notesAsync.data,
                filteringUiInfo = filterUiInfo,
                isLoading = isLoading,
                userMessage = userMessage
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotesUiState(isLoading = true)
    )

    fun setFiltering(requestType: NotesFilterType) {
        savedStateHandle[NOTES_FILTER_SAVED_STATE_KEY] = requestType
    }

    fun showEditResultMessage(result: Int) {
        when (result) {
            EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_saved_note_message)
            ADD_EDIT_RESULT_OK -> showSnackbarMessage(R.string.successfully_added_note_message)

        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

   fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            notesRepository.refresh()
            _isLoading.value = false
        }
    }

    private fun filterNotes(notes: List<Note>, filteringType: NotesFilterType): List<Note> {
        return when (filteringType) {
            ALL_NOTES -> notes
            NotesFilterType.STARRED_NOTES -> {
                notes.filter { it.isStarred }
            }
        }
    }

    private fun getFilterUiInfo(type: NotesFilterType): FilteringUiInfo {
        return when (type) {
            ALL_NOTES -> FilteringUiInfo(
                R.string.label_all_notes, R.string.no_notes_all, R.drawable.logo_no_fill
            )
            NotesFilterType.STARRED_NOTES -> FilteringUiInfo(
                R.string.label_starred_notes, R.string.no_notes_starred, R.drawable.logo_no_fill
            )
        }
    }

    fun toggleStar(note: Note) {
        viewModelScope.launch {
            val updatedNote = note.copy(isStarred = !note.isStarred)
            println("Toggling star for note: $updatedNote")
            notesRepository.updateNote(updatedNote.id, updatedNote.title, updatedNote.content, updatedNote.isStarred)
        }
    }

}

const val NOTES_FILTER_SAVED_STATE_KEY = "NOTES_FILTER_SAVED_STATE_KEY"

data class FilteringUiInfo(
    @StringRes val currentFilteringLabel: Int = R.string.label_all_notes,
    @StringRes val noTasksLabel: Int = R.string.no_notes_all,
    @DrawableRes val noTaskIconRes: Int = R.drawable.logo_no_fill
)
