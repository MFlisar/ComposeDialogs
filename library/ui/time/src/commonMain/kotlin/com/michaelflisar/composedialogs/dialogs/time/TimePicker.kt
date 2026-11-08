package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime

@Composable
fun TimePicker(
    time: MutableState<LocalTime>,
    is24Hours: Boolean,
    minuteStep: Int,
    hourPickType: TimePickerInputType,
    minutePickType: TimePickerInputType,
    texts: TimePickerTexts,
    uiProvider: TimeUIProvider,
    modifier: Modifier = Modifier,
    onValidationChanged: (Boolean) -> Unit = {}
) {
    require(minuteStep in 1..60) { "minuteStep must be between 1 and 60" }

    val ui = uiProvider
    val displayedHour = displayHour(time.value.hour, is24Hours)
    val period = period(time.value.hour)
    val hourText = remember { mutableStateOf(displayedHour.toString().padStart(2, '0')) }
    val minuteText = remember { mutableStateOf(time.value.minute.toString().padStart(2, '0')) }
    val hourFocused = remember { mutableStateOf(false) }
    val minuteFocused = remember { mutableStateOf(false) }
    val hourEdited = remember { mutableStateOf(false) }
    val minuteEdited = remember { mutableStateOf(false) }
    val hourInputValid = !hourEdited.value || hourText.value.toIntOrNull()?.let {
        it in (if (is24Hours) 0..23 else 1..12)
    } == true
    val minuteInputValid = !minuteEdited.value || minuteText.value.toIntOrNull()?.let {
        it in 0..59 && it % minuteStep == 0
    } == true
    val isValid = (hourPickType != TimePickerInputType.Input || hourInputValid) &&
        (minutePickType != TimePickerInputType.Input || minuteInputValid)
    val hourError = if (hourPickType == TimePickerInputType.Input && !hourInputValid) {
        texts.invalidHour
    } else {
        null
    }
    val minuteError = if (minutePickType == TimePickerInputType.Input && !minuteInputValid) {
        texts.invalidMinute
    } else {
        null
    }
    val hours = remember(is24Hours) {
        if (is24Hours) (0..23).toList() else (1..12).toList()
    }
    val minutes = remember(minuteStep) {
        (0 until 60 step minuteStep).toList()
    }

    LaunchedEffect(displayedHour, is24Hours) {
        if (!hourFocused.value && !hourEdited.value) {
            hourText.value = displayedHour.toString().padStart(2, '0')
        }
    }
    LaunchedEffect(time.value.minute) {
        if (!minuteFocused.value && !minuteEdited.value) {
            minuteText.value = time.value.minute.toString().padStart(2, '0')
        }
    }
    LaunchedEffect(isValid, onValidationChanged) {
        onValidationChanged(isValid)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ui.LabeledField(
            label = texts.hour,
            modifier = Modifier.weight(1f),
            errorMessage = hourError
        ) {
            when (hourPickType) {
                TimePickerInputType.Input -> {
                    ui.NumberInput(
                        modifier = Modifier.fillMaxWidth(),
                        value = hourText.value,
                        onValueChange = { input ->
                            if (input.isNumericInput()) {
                                hourText.value = input
                                hourEdited.value = true
                                val value = input.toIntOrNull()
                                val maxHour = if (is24Hours) 23 else 12
                                val minHour = if (is24Hours) 0 else 1
                                if (value != null && value in minHour..maxHour) {
                                    time.value = time.value.withDisplayedHour(value, is24Hours)
                                }
                            }
                        },
                        title = texts.hour,
                        onFocusChanged = { focused ->
                            hourFocused.value = focused
                            if (!focused) {
                                if (hourInputValid) {
                                    hourEdited.value = false
                                    hourText.value = displayHour(
                                        time.value.hour,
                                        is24Hours
                                    ).toString().padStart(2, '0')
                                }
                            }
                        }
                    )
                }

                TimePickerInputType.Dropdown -> {
                    ui.NumberDropdown(
                        modifier = Modifier.fillMaxWidth(),
                        title = texts.hour,
                        items = hours,
                        selected = displayedHour,
                        selectedText = displayedHour.toString().padStart(2, '0'),
                        itemText = { it.toString().padStart(2, '0') },
                        onItemSelected = { time.value = time.value.withDisplayedHour(it, is24Hours) }
                    )
                }
            }
        }

        ui.LabeledField(
            label = texts.minute,
            modifier = Modifier.weight(1f),
            errorMessage = minuteError
        ) {
            when (minutePickType) {
                TimePickerInputType.Input -> {
                    ui.NumberInput(
                        modifier = Modifier.fillMaxWidth(),
                        value = minuteText.value,
                        onValueChange = { input ->
                            if (input.isNumericInput()) {
                                minuteText.value = input
                                minuteEdited.value = true
                                val value = input.toIntOrNull()
                                if (value != null && value in 0..59 && value % minuteStep == 0) {
                                    time.value = LocalTime(time.value.hour, value)
                                }
                            }
                        },
                        title = texts.minute,
                        onFocusChanged = { focused ->
                            minuteFocused.value = focused
                            if (!focused) {
                                if (minuteInputValid) {
                                    minuteEdited.value = false
                                    minuteText.value = time.value.minute.toString().padStart(2, '0')
                                }
                            }
                        }
                    )
                }

                TimePickerInputType.Dropdown -> {
                    ui.NumberDropdown(
                        modifier = Modifier.fillMaxWidth(),
                        title = texts.minute,
                        items = minutes,
                        selected = time.value.minute,
                        selectedText = time.value.minute.toString().padStart(2, '0'),
                        itemText = { it.toString().padStart(2, '0') },
                        onItemSelected = { time.value = LocalTime(time.value.hour, it) }
                    )
                }
            }
        }

        if (!is24Hours) {
            ui.PeriodSelector(
                modifier = Modifier
                    .width(64.dp)
                    .fillMaxHeight(),
                selected = period,
                amText = texts.am,
                pmText = texts.pm,
                onItemSelected = { time.value = time.value.withPeriod(it) }
            )
        }
    }
}

private fun String.isNumericInput() = length <= 2 && all { it in '0'..'9' }

private fun displayHour(hour: Int, is24Hours: Boolean): Int =
    if (is24Hours) hour else (hour % 12).let { if (it == 0) 12 else it }

private fun LocalTime.withDisplayedHour(hour: Int, is24Hours: Boolean): LocalTime {
    val actualHour = if (is24Hours) {
        hour
    } else {
        (hour % 12) + if (this.hour >= 12) 12 else 0
    }
    return LocalTime(actualHour, minute)
}

private fun period(hour: Int) =
    if (hour < 12) TimePickerPeriod.AM else TimePickerPeriod.PM

private fun LocalTime.withPeriod(period: TimePickerPeriod): LocalTime {
    val hourIn12HourClock = hour % 12
    val actualHour = hourIn12HourClock + if (period == TimePickerPeriod.PM) 12 else 0
    return LocalTime(actualHour, minute)
}
