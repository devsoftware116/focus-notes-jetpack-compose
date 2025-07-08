package com.dev.focusnotes.ui.util

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.focusnotes.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopAppBar(
    openDrawer: () -> Unit,
    onRefresh: () -> Unit,
    onShowAllNotes: () -> Unit,
    onShowStarredNotes: () -> Unit,
) {
    val colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
    )

    CenterAlignedTopAppBar(
        title = {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
    }, navigationIcon = {
        IconButton(
            onClick = openDrawer, modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = stringResource(R.string.open_drawer),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }, actions = {
        IconButton(
            onClick = onRefresh, modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = stringResource(R.string.refresh_notes),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        FilterNotesMenu(
            onShowAllNotes = onShowAllNotes,
            onShowStarredNotes = onShowStarredNotes,

            )
    }, colors = colors, modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp)
    )
}

@Composable
private fun FilterNotesMenu(
    onShowAllNotes: () -> Unit,
    onShowStarredNotes: () -> Unit,

    ) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { expanded = true }, modifier = Modifier.padding(4.dp)
        ) {
            Icon(
                // Filter icon
                painterResource(id = R.drawable.ic_filter_list),
                contentDescription = stringResource(R.string.filter_notes),

                )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(200.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            DropdownMenuItem(text = {
                Text(
                    text = stringResource(R.string.all_notes),
                    style = MaterialTheme.typography.bodyLarge
                )
            }, onClick = {
                onShowAllNotes()
                expanded = false
            })
            DropdownMenuItem(text = {
                Text(
                    text = stringResource(R.string.starred_notes),
                    style = MaterialTheme.typography.bodyLarge
                )
            }, onClick = {
                onShowStarredNotes()
                expanded = false
            })
        }
    }
}

@Composable
private fun TopAppBarDropdownMenu(
    iconContent: @Composable () -> Unit, content: @Composable ColumnScope.(() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
        IconButton(onClick = { expanded = !expanded }) {
            iconContent()
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            content { expanded = false }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteTopAppBar(@StringRes title: Int, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(title)) }, navigationIcon = {
        IconButton(onClick = onBack) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.menu_back)
            )
        }
    }, modifier = Modifier.fillMaxWidth()
    )
}


// Preview
@Preview(showBackground = true)
@Composable
fun NotesTopAppBarPreview() {
    NotesTopAppBar(openDrawer = {}, onRefresh = {}, onShowAllNotes = {}, onShowStarredNotes = {})
}
