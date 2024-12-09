package com.michaelflisar.composedialogs.dialogs.number

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogStyleDesktopOtions
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.style.DialogStyleDefaults
import com.michaelflisar.composedialogs.core.style.DialogStyleDesktop

@Composable
fun DialogDefaults.styleWindowsNumberDialog(
    dialogTitle: String,
    position: WindowPosition = WindowPosition(Alignment.Center),
    width: Dp = 400.dp,
    height: Dp = 200.dp,
    // Style
    iconColor: Color = DialogStyleDefaults.iconColor,
    titleColor: Color = DialogStyleDefaults.titleColor,
    contentColor: Color = DialogStyleDefaults.contentColor
): ComposeDialogStyle {
    return DialogStyleDesktop(
        desktopOptions = DialogStyleDesktopOtions(dialogTitle, position, width, height),
        iconColor = iconColor,
        titleColor = titleColor,
        contentColor = contentColor
    )
}