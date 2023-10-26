package com.michaelflisar.composedialogs.dialogs.time

import android.text.format.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
 * @param time the selected time
 * @param setup the [DialogTime.Setup] - use [DialogTimeDefaults.setup] to provide your own data
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogTime(
    state: DialogState,
    // Custom - Required
    time: MutableState<LocalTime>,
    // Custom - Optional
    setup: DialogTime.Setup = DialogTimeDefaults.setup(),
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

@Stable
object DialogTime {

    /**
     * time setup
     *
     * check out [DialogTimeDefaults.setup]
     */
    class Setup internal constructor(
        //val seconds: Boolean = false,
        val is24Hours: Boolean
    )
}

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

@Stable
object DialogTimeDefaults {
    /**
     * time setup
     *
     * @param is24Hours if true 24h mode is enabled otherwise 12h mode will be used
     */
    @Composable
    fun setup(is24Hours: Boolean = DateFormat.is24HourFormat(LocalContext.current)) =
        DialogTime.Setup(is24Hours)
}