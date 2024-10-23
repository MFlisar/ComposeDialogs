package com.michaelflisar.composedialogs.core

import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.style.DialogStyle

/**
 * the setup of a dialog that shows as a normal dialog popup
 */
@Composable
fun DialogDefaults.styleDialog(): ComposeDialogStyle {
    return DialogStyle()
}