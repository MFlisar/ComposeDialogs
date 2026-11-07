package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composedialogs.dialogs.time.utils.TimeUtil
import kotlinx.datetime.LocalTime

@Stable
object DialogTime {

    class Setup internal constructor(val is24Hours: Boolean)

    val LocalTimeSaver = Saver<MutableState<LocalTime>, Int>(
        save = { it.value.toSecondOfDay() },
        restore = { mutableStateOf(LocalTime.fromSecondOfDay(it)) }
    )
}

@Composable
fun rememberDialogTime(time: LocalTime = TimeUtil.now().time): MutableState<LocalTime> =
    rememberSaveable(saver = DialogTime.LocalTimeSaver) { mutableStateOf(time) }
