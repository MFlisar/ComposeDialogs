package com.michaelflisar.composedialogs.core

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle

@Composable
expect fun stringOk() : String

@Composable
expect fun isLandscape(): Boolean

@Composable
expect fun DialogDefaults.defaultDialogStyle(): ComposeDialogStyle

@Composable
expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)

@Composable
expect fun DialogContentScrollableColumn(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit)

@Composable
expect fun DialogContentScrollableLazyColumn(modifier: Modifier = Modifier, state: LazyListState = rememberLazyListState(), content: LazyListScope.() -> Unit)

expect fun DialogDefaults.specialOptions() : SpecialOptions