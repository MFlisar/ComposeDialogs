package com.michaelflisar.composedialogs.dialogs.datetime

import androidx.compose.foundation.layout.*
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
import com.michaelflisar.composedialogs.core.*
import java.util.*

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
            time.minutes.value,
            setup.is24Hours
        )
        LaunchedEffect(state.hour, state.minute) {
            time.hour.value = state.hour
            time.minutes.value = state.minute
        }
        TimeInput(state = state, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

class DialogTimeState(
    val hour: MutableState<Int>,
    val minutes: MutableState<Int>,
    //val seconds: MutableState<Int>
) {
    override fun toString() =
        "${"%02d".format(hour.value)}:${"%02d".format(minutes.value)}"//:${"%02d".format(seconds.value)}"
}

class DialogTimeSetup(
    //val seconds: Boolean = false,
    val is24Hours: Boolean = true
)

@Composable
fun rememberDialogTimeState(
    initialHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
    initialMinutes: Int = Calendar.getInstance().get(Calendar.MINUTE),
    //initialSeconds: Int = Calendar.getInstance().get(Calendar.SECOND)
): DialogTimeState {
    val hour = rememberSaveable { mutableIntStateOf(initialHour) }
    val minutes = rememberSaveable { mutableIntStateOf(initialMinutes) }
    //val seconds = rememberSaveable { mutableStateOf(initialSeconds) }
    return DialogTimeState(hour, minutes)//, seconds)
}