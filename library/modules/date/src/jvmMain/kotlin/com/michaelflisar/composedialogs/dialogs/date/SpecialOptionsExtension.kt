package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions

fun DialogDefaults.specialOptionsDateDialog(
    position: WindowPosition = WindowPosition(Alignment.Center),
    width: Dp = 600.dp,
    height: Dp = 600.dp
) = SpecialOptions(position, width, height)