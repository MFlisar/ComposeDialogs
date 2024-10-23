package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.SpecialOptions

expect fun DialogDefaults.defaultTimeDialogSpecialOptions(): SpecialOptions

@Composable
expect fun is24HourFormat(): Boolean