package com.michaelflisar.demo.demos

import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.ComposeDialogStyle

@Composable
actual fun DemoListAppSelector(
    style: ComposeDialogStyle,
    icon: @Composable (() -> Unit)?,
    showInfo: (String) -> Unit,
) {
    // empty - only implemented for Android
}