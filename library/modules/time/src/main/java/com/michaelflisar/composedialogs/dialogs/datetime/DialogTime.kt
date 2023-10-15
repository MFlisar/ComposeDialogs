package com.michaelflisar.composedialogs.dialogs.datetime

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.michaelflisar.composedialogs.core.*
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
    time: DialogTimeState,
    // Custom - Optional
    setup: DialogTimeSetup = DialogTimeSetup(),
    // Base Dialog - Optional
    title: String = "",
    titleStyle: DialogTitleStyle = DialogDefaults.titleStyle(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    //if (!setup.seconds) {
    //    time.seconds.value = 0
    //}

    Dialog(state, title, titleStyle, icon, style, buttons, options, onEvent = onEvent) {
        val state = rememberTimePickerState(
            time.hour.value,
            time.minute.value,
            setup.is24Hours
        )
        LaunchedEffect(state.hour, state.minute) {
            time.hour.value = state.hour
            time.minute.value = state.minute
        }
        TimeInput(state = state, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

/**
 * time state
 *
 * @param hour the current state of the hour
 * @param minutes the current state of the minute
 */
class DialogTimeState(
    val hour: MutableState<Int>,
    val minute: MutableState<Int>,
    //val seconds: MutableState<Int>
) {
    override fun toString() =
        "${"%02d".format(hour.value)}:${"%02d".format(minute.value)}"//:${"%02d".format(seconds.value)}"
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
fun rememberDialogTimeState(
    initialHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    initialMinute: Int = Calendar.getInstance().get(Calendar.MINUTE),
    //initialSeconds: Int = Calendar.getInstance().get(Calendar.SECOND)
): DialogTimeState {
    val hour = rememberSaveable { mutableIntStateOf(initialHour) }
    val minute = rememberSaveable { mutableIntStateOf(initialMinute) }
    //val seconds = rememberSaveable { mutableStateOf(initialSeconds) }
    return DialogTimeState(hour, minute)//, seconds)
}