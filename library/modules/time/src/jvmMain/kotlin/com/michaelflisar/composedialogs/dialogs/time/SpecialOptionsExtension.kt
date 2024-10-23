package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions

fun DialogDefaults.specialOptionsTimeDialog(
    position: WindowPosition = WindowPosition(Alignment.Center),
    width: Dp = 300.dp,
    height: Dp = 260.dp
) = SpecialOptions(position, width, height)