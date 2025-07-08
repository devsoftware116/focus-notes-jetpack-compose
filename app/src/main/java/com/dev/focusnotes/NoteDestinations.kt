// NoteDestinations.kt
package com.dev.focusnotes

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object NoteDestinationsArgs {
    const val NOTE_ID_ARG = "noteId"
    const val TITLE_ARG = "title"
    const val USER_MESSAGE_ARG = "userMessage"
}


object NoteDestinations {
    const val NOTES_LIST_ROUTE = "notes_list"
    const val ADD_EDIT_NOTE_ROUTE = "add_edit_note"
    const val TRASH_ROUTE = "trash"
    const val NOTE_ID_ARG = "noteId"
    const val TITLE_ARG = "title"

    // Full route pattern with arguments
    const val ADD_EDIT_NOTE_ROUTE_WITH_ARGS =
        "$ADD_EDIT_NOTE_ROUTE?$TITLE_ARG={$TITLE_ARG}&$NOTE_ID_ARG={$NOTE_ID_ARG}"

    // Helper to build route for navigation
    fun addEditNoteRoute(title: Int, noteId: String? = null): String {
        return buildString {
            append("$ADD_EDIT_NOTE_ROUTE?$TITLE_ARG=$title")
            if (noteId != null) append("&$NOTE_ID_ARG=$noteId")
        }
    }
}

class NoteNavigationActions(private val navController: NavHostController) {

    fun navigateToAddEditNote(titleRes: Int, noteId: String?) {
        val route = NoteDestinations.addEditNoteRoute(titleRes, noteId)
        navController.navigate(route)
    }

    fun navigateToNoteDetail(noteId: String) {
        navController.navigate("${NoteDestinations.NOTES_LIST_ROUTE}/$noteId")
    }

    fun navigateToNotesList(message: Int = 0) {
        navController.navigate(NoteDestinations.NOTES_LIST_ROUTE)
    }

    fun navigateToTrash() {
        navController.navigate(NoteDestinations.TRASH_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
                saveState = false
            }
        }
    }

    fun navigateToSettings() {
        // TODO: Implement navigation to settings
    }

    fun navigateToAbout() {
        // TODO: Implement navigation to about
    }

    fun navigateToHelp() {
        // TODO: Implement navigation to help
    }
}


