package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI
import kotlinx.datetime.LocalTime

/**
 * Displays the time input and updates [time] as the user edits it.
 *
 * @param time the selected time state
 * @param setup the time picker setup
 */
@Composable
fun DialogTimeContent(
    time: MutableState<LocalTime>,
    setup: DialogTime.Setup = DialogTimeDefaults.setup()
) {
    Column {
        ComposeDialogsUI.provider.TimeInput(
            hour = time.value.hour,
            minute = time.value.minute,
            is24Hour = setup.is24Hours,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { hour, minute ->
            time.value = LocalTime(hour, minute)
        }
    }
}
