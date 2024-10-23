package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.specialOptions

actual fun DialogDefaults.defaultInputDialogSpecialOptions(): SpecialOptions {
    return specialOptions()
}

internal actual val DialogStyleModifier: Modifier = Modifier