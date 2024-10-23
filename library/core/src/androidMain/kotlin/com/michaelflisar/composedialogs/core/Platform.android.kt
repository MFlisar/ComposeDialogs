package com.michaelflisar.composedialogs.core

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
actual fun isLandscape(): Boolean =
    LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

@Composable
actual fun stringOk() = stringResource(android.R.string.ok)

@Composable
actual fun DialogDefaults.defaultDialogStyle() = styleDialog()

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    androidx.activity.compose.BackHandler(enabled, onBack)
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

actual fun DialogDefaults.specialOptions() : SpecialOptions {
    return SpecialOptions()
}

/**
 * the special options for a dialog
 *
 * @param dialogIntrinsicWidthMin if true, the dialog width will use [IntrinsicSize.Min]
 */
actual class SpecialOptions(
    val dialogIntrinsicWidthMin: Boolean = false
)