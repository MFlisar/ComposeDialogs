package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogStyleDesktopOtions
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.style.DialogStyleDesktop

@Composable
fun DialogDefaults.styleWindowsTimeDialog(
    dialogTitle: String,
    position: WindowPosition = WindowPosition(Alignment.Center),
    width: Dp = 300.dp,
    height: Dp = 260.dp
): ComposeDialogStyle {
    return DialogStyleDesktop(DialogStyleDesktopOtions(dialogTitle, position, width, height))
}