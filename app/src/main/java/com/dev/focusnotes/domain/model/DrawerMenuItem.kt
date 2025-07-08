package com.dev.focusnotes.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerMenuItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val onClick: () -> Unit
)