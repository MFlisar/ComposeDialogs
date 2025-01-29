package com.michaelflisar.composedialogs.core

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

internal actual val useBottomsheetWorkaround: Boolean = true

@Composable
actual fun isLandscape() = false

@Composable
actual fun stringOk() = "OK"

@Composable
actual fun DialogDefaults.defaultDialogStyle(): ComposeDialogStyle = TODO()

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // empty
}

@Composable
actual fun DialogContentScrollableColumn(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        content()
    }
}

@Composable
actual fun DialogContentScrollableLazyColumn(
    modifier: Modifier,
    state: LazyListState,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(end = 4.dp),
        state = state
    ) {
        content()
    }
}

@Composable
actual fun updateStatusbarColor(darkStatusBar: Boolean) {}