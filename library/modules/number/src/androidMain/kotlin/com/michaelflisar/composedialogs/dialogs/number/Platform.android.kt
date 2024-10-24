package com.michaelflisar.composedialogs.dialogs.number

import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.specialOptions

actual fun DialogDefaults.defaultNumberDialogSpecialOptions(): SpecialOptions {
    return specialOptions()
}

internal actual val DialogStyleModifier: Modifier = Modifier