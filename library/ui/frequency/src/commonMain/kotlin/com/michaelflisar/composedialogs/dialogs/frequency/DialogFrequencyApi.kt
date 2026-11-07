package com.michaelflisar.composedialogs.dialogs.frequency

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composedialogs.dialogs.frequency.classes.Frequency
import com.michaelflisar.composedialogs.dialogs.frequency.classes.FrequencyStateSaver
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

object DialogFrequency {

    @Immutable
    class Texts(
        val interval: String,
        val type: String,
        val every: String,
        val monthOfYear: String,
        val dayOfMonth: String,
        val dayOfWeek: String,
        val time: String,
        val nameOfType: (day: Frequency.Type, interval: Int) -> String,
        val nameOfDayOfWeek: (day: DayOfWeek) -> String,
        val nameOfMonth: (month: Month) -> String
    )
}

@Composable
fun rememberDialogFrequency(frequency: Frequency): MutableState<Frequency> =
    rememberSaveable(saver = FrequencyStateSaver) { mutableStateOf(frequency) }
