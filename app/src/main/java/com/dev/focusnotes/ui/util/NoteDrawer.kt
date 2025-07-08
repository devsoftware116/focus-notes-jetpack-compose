package com.dev.focusnotes.ui.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.focusnotes.NoteDestinations
import com.dev.focusnotes.NoteNavigationActions
import com.dev.focusnotes.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppModalDrawer(
    drawerState: DrawerState,
    currentRoute: String,
    navActions: NoteNavigationActions,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                navigateToNotes = { navActions.navigateToNotesList() },
                navigateToTrash = { navActions.navigateToTrash() },
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        },
        content = content
    )
}

@Composable
private fun AppDrawer(
    currentRoute: String,
    navigateToNotes: () -> Unit,
    navigateToTrash: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(modifier = modifier.fillMaxSize()) {
            DrawerHeader()
            DrawerButton(
                painter = painterResource(id = R.drawable.ic_list), // Provide your icon
                label = stringResource(id = R.string.notes_title),
                isSelected = currentRoute == NoteDestinations.NOTES_LIST_ROUTE,
                action = {
                    navigateToNotes()
                    closeDrawer()
                }
            )
            DrawerButton(
                painter = painterResource(id = R.drawable.ic_filter_list), // Provide your icon
                label = stringResource(id = R.string.archive_title),
                isSelected = currentRoute == NoteDestinations.TRASH_ROUTE,
                action = {
                    navigateToTrash()
                    closeDrawer()
                }
            )
        }
    }
}

@Composable
private fun DrawerHeader(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .height(180.dp)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_no_fill), // Provide your logo
            contentDescription = stringResource(id = R.string.notes_header_image_content_description),
            modifier = Modifier.width(100.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun DrawerButton(
    painter: Painter,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tintColor = if (isSelected) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }

    TextButton(
        onClick = action,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                tint = tintColor
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = tintColor
            )
        }
    }
}

@Preview
@Composable
fun AppDrawerPreview() {
    MaterialTheme {
        AppDrawer(
            currentRoute = NoteDestinations.NOTES_LIST_ROUTE,
            navigateToNotes = {},
            navigateToTrash = {},
            closeDrawer = {}
        )
    }
}
