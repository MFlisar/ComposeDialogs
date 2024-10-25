package com.michaelflisar.composedialogs.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.style.DialogStyleDesktop

@Composable
fun DialogDefaults.styleWindowsDialog(
    dialogTitle: String,
    position: WindowPosition = WindowPosition(Alignment.Center),
    width: Dp = 800.dp,
    height: Dp = 600.dp
): ComposeDialogStyle {
    return DialogStyleDesktop(DialogStyleDesktopOtions(dialogTitle, position, width, height))
}