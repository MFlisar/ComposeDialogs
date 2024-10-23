package com.michaelflisar.composedialogs.dialogs.number

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions

actual fun DialogDefaults.defaultNumberDialogSpecialOptions(): SpecialOptions {
    return specialOptionsNumberDialog()
}

internal actual val DialogStyleModifier: Modifier = Modifier.fillMaxWidth()