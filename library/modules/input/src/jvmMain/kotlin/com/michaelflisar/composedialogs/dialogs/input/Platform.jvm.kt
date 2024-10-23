package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.specialOptions

actual fun DialogDefaults.defaultInputDialogSpecialOptions(): SpecialOptions {
    return specialOptionsInputDialog()
}

internal actual val DialogStyleModifier: Modifier = Modifier.fillMaxWidth()