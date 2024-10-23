package com.michaelflisar.composedialogs.dialogs.time

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.specialOptions

@Composable
actual fun is24HourFormat(): Boolean {
    return DateFormat.is24HourFormat(LocalContext.current)
}

actual fun DialogDefaults.defaultTimeDialogSpecialOptions(): SpecialOptions {
    return specialOptions()
}