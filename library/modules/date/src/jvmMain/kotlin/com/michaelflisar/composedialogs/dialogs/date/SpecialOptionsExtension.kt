package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogStyleDesktopOtions
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle2
import com.michaelflisar.composedialogs.core.style.DialogStyleDesktop

@Composable
fun DialogDefaults.styleWindowsDateDialog(
    dialogTitle: String,
    position: WindowPosition = WindowPosition(Alignment.Center),
    width: Dp = 600.dp,
    height: Dp = 600.dp
): ComposeDialogStyle2 {
    return DialogStyleDesktop(DialogStyleDesktopOtions(dialogTitle, position, width, height))
}