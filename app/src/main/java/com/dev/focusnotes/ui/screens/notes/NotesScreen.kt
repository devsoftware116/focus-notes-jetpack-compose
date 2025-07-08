package com.dev.focusnotes.ui.screens.notes


import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.focusnotes.R
import com.dev.focusnotes.domain.model.Note
import com.dev.focusnotes.ui.util.DateUtils
import com.dev.focusnotes.ui.util.LoadingContent
import com.dev.focusnotes.ui.util.NotesTopAppBar

@Composable
fun NotesScreen(
    @StringRes userMessage: Int,
    onAddNote: () -> Unit,
    onNoteClick: (Note) -> Unit,
    onUserMessageDisplayed: () -> Unit,
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            NotesTopAppBar(
                openDrawer = openDrawer,
                onRefresh = viewModel::refresh,
                onShowAllNotes = { viewModel.setFiltering(NotesFilterType.ALL_NOTES) },
                onShowStarredNotes = { viewModel.setFiltering(NotesFilterType.STARRED_NOTES) },
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(onClick = onAddNote) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.add_note))
            }
        }) { paddingValues ->
        NotesContent(
            loading = uiState.isLoading,
            notes = uiState.items,
            onNoteClick = onNoteClick,
            onToggleStar = { viewModel.toggleStar(it) },
            onRefresh = viewModel::refresh,
            modifier = Modifier.padding(paddingValues)
        )

        uiState.userMessage?.let { message ->
            val snackbarText = stringResource(message)
            LaunchedEffect(snackbarHostState, viewModel, message, snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.snackbarMessageShown()
            }
        }

        val currentOnUserMessageDisplayed by rememberUpdatedState(onUserMessageDisplayed)

        LaunchedEffect(userMessage) {
            if (userMessage != 0) {
                viewModel.showEditResultMessage(userMessage)
                currentOnUserMessageDisplayed()
            }
        }
    }
}

@Composable
fun NotesContent(
    loading: Boolean,
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onToggleStar: (Note) -> Unit,
    onRefresh: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LoadingContent(
        loading = loading,
        empty = notes.isEmpty() && !loading,
        emptyContent = { NotesEmptyContent(modifier) },
        onRefresh = onRefresh,
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(id = R.dimen.list_item_padding))
        ) {
            items(notes) { note ->
                NoteItemCard(
                    note = note, onNoteClick = onNoteClick, onToggleStar = onToggleStar
                )
            }
        }
    }
}

@Composable
fun NoteItemCard(
    note: Note, onNoteClick: (Note) -> Unit, onToggleStar: (Note) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onNoteClick(note) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        ListItem(headlineContent = {
            Text(
                text = note.title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }, supportingContent = {
            Text(
                text = note.content ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }, trailingContent = {
            IconButton(onClick = {
                onToggleStar(note)
            }) {
                Icon(
                    imageVector = if (note.isStarred) Icons.Default.Star else Icons.Filled.Star,
                    contentDescription = if (note.isStarred) "Unstar" else "Star",
                    tint = if (note.isStarred) Color(0xFFFFD700) else LocalContentColor.current
                )
            }
        }, overlineContent = {
            Text(
                text = DateUtils.formatDateTime(note.createdTime),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        })
    }
}

@Composable
fun NotesEmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_no_fill),
            contentDescription = stringResource(R.string.no_notes_image_content_description),
            modifier = Modifier.size(96.dp)
        )
        Text(stringResource(id = R.string.no_notes_all))
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    val sampleNotes = listOf(
        Note(id = "1", title = "Sample Note 1", content = "This is a preview of the first note."),
        Note(id = "2", title = "Sample Note 2", content = "This is another sample note.")
    )

    NotesContent(
        loading = false,
        notes = sampleNotes,
        onNoteClick = {},
        onToggleStar = {},
        modifier = Modifier.padding(16.dp)
    )
}
