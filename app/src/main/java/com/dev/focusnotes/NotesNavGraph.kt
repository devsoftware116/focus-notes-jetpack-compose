package com.dev.focusnotes

import android.app.Activity
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dev.focusnotes.ui.util.AppModalDrawer
import com.dev.focusnotes.ui.screens.addeditnote.AddEditNoteScreen

import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.getValue

import com.dev.focusnotes.NoteDestinationsArgs.USER_MESSAGE_ARG
import com.dev.focusnotes.ui.screens.notes.NotesScreen
//import com.dev.focusnotes.ui.screens.notes.NotesScreen

import kotlinx.coroutines.launch

@Composable
fun NotesNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = NoteDestinations.NOTES_LIST_ROUTE,
    navActions: NoteNavigationActions = remember(navController) {
        NoteNavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = NoteDestinations.NOTES_LIST_ROUTE,
            arguments = listOf(
                navArgument(USER_MESSAGE_ARG) {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { entry ->
            AppModalDrawer(
                drawerState = drawerState,
                currentRoute = currentRoute,
                navActions = navActions
            ) {

               NotesScreen(
                    userMessage = entry.arguments?.getInt(USER_MESSAGE_ARG)!!,
                    onUserMessageDisplayed = {
                        entry.arguments?.putInt(USER_MESSAGE_ARG, 0)
                    },
                    onAddNote = {
                        navActions.navigateToAddEditNote(R.string.add_note, null)
                    },
                    onNoteClick = { note ->
                        println("Note clicked: ${note.title}")
                        navActions.navigateToAddEditNote(R.string.edit_note, note.id)
                    },
                    openDrawer = {
                        coroutineScope.launch { drawerState.open() }
                    }
                )

            }
        }

        composable(
            route = NoteDestinations.ADD_EDIT_NOTE_ROUTE_WITH_ARGS,
            arguments = listOf(
                navArgument(NoteDestinations.TITLE_ARG) {
                    type = NavType.IntType
                    defaultValue = R.string.add_note
                },
                navArgument(NoteDestinations.NOTE_ID_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { entry ->
            val noteId = entry.arguments?.getString(NoteDestinations.NOTE_ID_ARG)
            AddEditNoteScreen(
                topBarTitle = entry.arguments?.getInt(NoteDestinations.TITLE_ARG)!!,
                onNoteUpdate = {
                    navActions.navigateToNotesList(
                        if (noteId == null) ADD_EDIT_RESULT_OK else EDIT_RESULT_OK
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

// Result codes
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 2
