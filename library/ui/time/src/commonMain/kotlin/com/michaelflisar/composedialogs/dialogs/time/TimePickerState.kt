package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composedialogs.dialogs.time.utils.TimeUtil
import kotlinx.datetime.LocalTime

private val TimePickerTimeSaver = Saver<MutableState<LocalTime>, Int>(
    save = { it.value.toSecondOfDay() },
    restore = { mutableStateOf(LocalTime.fromSecondOfDay(it)) }
)

/** Remembers a saveable time value for a time picker. */
@Composable
fun rememberTimePickerTime(
    time: LocalTime = TimeUtil.now().time
): MutableState<LocalTime> = rememberSaveable(saver = TimePickerTimeSaver) {
    mutableStateOf(time)
}
