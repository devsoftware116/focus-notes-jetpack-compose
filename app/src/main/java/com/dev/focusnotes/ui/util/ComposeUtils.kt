package com.dev.focusnotes.ui.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Displays either an empty state or the main content with pull-to-refresh support.
 *
 * @param loading Whether the refresh indicator should be shown.
 * @param empty Whether the content is empty (e.g. no notes).
 * @param emptyContent UI to show when the content is empty.
 * @param onRefresh Called when user pulls to refresh.
 * @param modifier Modifier to apply to the entire layout.
 * @param content UI content to display when not empty.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingContent(
    loading: Boolean,
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        val refreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            isRefreshing = loading,
            onRefresh = onRefresh,
            state = refreshState,
            modifier = modifier.fillMaxSize(),
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = loading,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = refreshState
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}
