package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.specialOptions

@Composable
actual fun is24HourFormat(): Boolean {
    return false
}

actual fun DialogDefaults.defaultTimeDialogSpecialOptions(): SpecialOptions {
    return specialOptions()
}