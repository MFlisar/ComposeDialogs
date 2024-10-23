package com.michaelflisar.composedialogs.dialogs.number

import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions

expect fun DialogDefaults.defaultNumberDialogSpecialOptions(): SpecialOptions

internal expect val DialogStyleModifier: Modifier