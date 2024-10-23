package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.specialOptions

actual fun DialogDefaults.defaultTimeDialogSpecialOptions(): SpecialOptions {
    return specialOptionsTimeDialog()
}
@Composable
actual fun is24HourFormat(): Boolean {
    return true
}