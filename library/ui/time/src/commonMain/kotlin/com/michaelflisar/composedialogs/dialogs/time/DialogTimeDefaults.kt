package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

@Stable
object DialogTimeDefaults {
    /**
     * time setup
     *
     * @param is24Hours if true 24h mode is enabled otherwise 12h mode will be used
     */
    @Composable
    fun setup(is24Hours: Boolean = is24HourFormat()) =
        DialogTime.Setup(is24Hours)
}