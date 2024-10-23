package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions

expect fun DialogDefaults.defaultInputDialogSpecialOptions(): SpecialOptions

internal expect val DialogStyleModifier: Modifier