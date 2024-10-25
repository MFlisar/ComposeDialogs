package com.michaelflisar.composedialogs.core

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.WindowPosition

data class DialogStyleDesktopOtions(
    val dialogTitle: String,
    val position: WindowPosition,
    val width: Dp,
    val height: Dp
)