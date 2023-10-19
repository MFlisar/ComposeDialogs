package com.michaelflisar.composedialogs.dialogs.datetime

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.*
import java.time.LocalTime
import java.util.*

/**
 * Shows a dialog with a time input
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param time the current [DialogTimeState]
 * @param setup the [DialogTimeSetup]
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogTime(
    state: DialogState,
    // Custom - Required
    time: MutableState<LocalTime>,
    // Custom - Optional
    setup: DialogTimeSetup = DialogTimeSetup(),
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    //if (!setup.seconds) {
    //    time.seconds.value = 0
    //}

    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        val state = rememberTimePickerState(
            time.value.hour,
            time.value.minute,
            setup.is24Hours
        )
        LaunchedEffect(state.hour, state.minute) {
            time.value = LocalTime.of(state.hour, state.minute)
        }
        TimeInput(state = state, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

/**
 * time setup
 *
 * @param is24Hours if true 24h mode is enabled otherwise 12h mode will be used
 */
class DialogTimeSetup(
    //val seconds: Boolean = false,
    val is24Hours: Boolean = true
)

/**
 * convenient function for [DialogTime]
 *
 * @param initialHour the initial hour
 * @param initialMinute the initial minute
 *
 * @return a state holding the current time values
 */
@Composable
fun rememberDialogTime(
    time: LocalTime = LocalTime.now()
): MutableState<LocalTime> {
    return rememberSaveable { mutableStateOf(time) }
}