package com.michaelflisar.composedialogs.dialogs.frequency

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.frequency.classes.Frequency
import com.michaelflisar.composedialogs.dialogs.frequency.ui.DayOfMonth
import com.michaelflisar.composedialogs.dialogs.frequency.ui.DayOfWeek
import com.michaelflisar.composedialogs.dialogs.frequency.ui.Month
import com.michaelflisar.composedialogs.dialogs.frequency.ui.NumericInput
import com.michaelflisar.composedialogs.dialogs.time.TimePickerDefaults
import com.michaelflisar.composedialogs.dialogs.time.TimePickerSetup
import kotlinx.datetime.DayOfWeek as DateDayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month as DateMonth

/**
 * Renders a frequency picker using the caller-supplied [FrequencyUIProvider].
 *
 * The selected frequency is supplied as a single state; the UI provider renders the controls,
 * including the time input.
 *
 * @param frequency the state containing the frequency being edited
 * @param onIntervalValidityChanged called when the editable interval becomes valid or invalid
 */
@Composable
fun FrequencyPicker(
    uiProvider: FrequencyUIProvider,
    frequency: MutableState<Frequency>,
    supportedTypes: List<Frequency.Type>,
    firstDayOffset: DateDayOfWeek,
    texts: FrequencyPickerTexts,
    intervalInputType: FrequencyIntervalInputType = FrequencyIntervalInputType.Input,
    timeSetup: TimePickerSetup = TimePickerDefaults.setup(),
    onIntervalValidityChanged: (Boolean) -> Unit = {}
) {
    val ui = uiProvider
    val selectedFrequency = frequency.value
    val selectedInterval = remember(selectedFrequency.interval) {
        mutableStateOf<Int?>(selectedFrequency.interval)
    }
    val intervalIsValid = selectedInterval.value?.let { it > 0 } == true
    val intervalError = if (intervalIsValid) {
        null
    } else {
        texts.invalidInterval
    }

    LaunchedEffect(intervalIsValid) {
        onIntervalValidityChanged(intervalIsValid)
    }

    fun updateFrequency(update: (Frequency) -> Frequency) {
        frequency.value = update(frequency.value)
    }

    fun updateTime(time: LocalTime) {
        updateFrequency {
            when (it) {
                is Frequency.Daily -> it.copy(time = time)
                is Frequency.Weekly -> it.copy(time = time)
                is Frequency.Monthly -> it.copy(time = time)
                is Frequency.Yearly -> it.copy(time = time)
            }
        }
    }

    fun updateInterval(interval: Int?) {
        selectedInterval.value = interval
        if (interval != null && interval > 0) {
            updateFrequency {
                when (it) {
                    is Frequency.Daily -> it.copy(interval = interval)
                    is Frequency.Weekly -> it.copy(interval = interval)
                    is Frequency.Monthly -> it.copy(interval = interval)
                    is Frequency.Yearly -> it.copy(interval = interval)
                }
            }
        }
    }

    fun selectType(type: Frequency.Type) {
        val current = frequency.value
        val interval = current.interval
        val time = current.time
        frequency.value = when (type) {
            Frequency.Type.Daily -> Frequency.Daily(time = time, interval = interval)
            Frequency.Type.Weekly -> Frequency.Weekly(
                dayOfWeek = (current as? Frequency.Weekly)?.dayOfWeek ?: DateDayOfWeek.MONDAY,
                time = time,
                interval = interval
            )
            Frequency.Type.Monthly -> Frequency.Monthly(
                dayOfMonth = when (current) {
                    is Frequency.Monthly -> current.dayOfMonth
                    is Frequency.Yearly -> current.dayOfMonth
                    else -> 1
                },
                time = time,
                interval = interval
            )
            Frequency.Type.Yearly -> Frequency.Yearly(
                month = (current as? Frequency.Yearly)?.month ?: DateMonth.JANUARY,
                dayOfMonth = when (current) {
                    is Frequency.Monthly -> current.dayOfMonth
                    is Frequency.Yearly -> current.dayOfMonth
                    else -> 1
                },
                time = time,
                interval = interval
            )
        }
    }

    fun updateDayOfWeek(day: DateDayOfWeek) {
        updateFrequency { (it as Frequency.Weekly).copy(dayOfWeek = day) }
    }

    fun updateDayOfMonth(day: Int) {
        updateFrequency {
            when (it) {
                is Frequency.Monthly -> it.copy(dayOfMonth = day)
                is Frequency.Yearly -> it.copy(dayOfMonth = day)
                else -> it
            }
        }
    }

    fun updateMonth(month: DateMonth) {
        updateFrequency { (it as Frequency.Yearly).copy(month = month) }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val wideLayout = maxWidth >= 600.dp
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FrequencyField(
                    uiProvider,
                    texts.interval,
                    Modifier.weight(1f),
                    errorMessage = intervalError
                ) {
                    when (val inputType = intervalInputType) {
                        FrequencyIntervalInputType.Input -> {
                            NumericInput(
                                uiProvider = uiProvider,
                                modifier = Modifier.fillMaxWidth(),
                                title = texts.interval,
                                value = selectedInterval.value,
                                onValueChange = { updateInterval(it) }
                            )
                        }

                        is FrequencyIntervalInputType.Dropdown -> {
                            ui.IntervalDropdown(
                                modifier = Modifier.fillMaxWidth(),
                                title = texts.interval,
                                min = inputType.min,
                                max = inputType.max,
                                selected = selectedInterval.value ?: inputType.min,
                                onItemSelected = { updateInterval(it) }
                            )
                        }
                    }
                }
                FrequencyField(uiProvider, texts.type, Modifier.weight(1f)) {
                    ui.FrequencyTypeDropdown(
                        modifier = Modifier.fillMaxWidth(),
                        items = supportedTypes,
                        itemText = { texts.nameOfType(it, selectedInterval.value ?: 1) },
                        selected = selectedFrequency.type,
                        title = texts.type,
                        onItemSelected = { selectType(it) }
                    )
                }
            }
            when (selectedFrequency) {
                is Frequency.Daily -> {
                    FrequencyField(uiProvider, texts.time, Modifier.fillMaxWidth()) {
                        ui.TimeInput(
                            selectedTime = selectedFrequency.time,
                            onTimeSelected = { updateTime(it) },
                            title = texts.time,
                            setup = timeSetup,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is Frequency.Weekly -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FrequencyField(uiProvider, texts.dayOfWeek, Modifier.weight(1f)) {
                            DayOfWeek(
                                uiProvider = uiProvider,
                                selectedDayOfWeek = selectedFrequency.dayOfWeek,
                                firstDayOffset = firstDayOffset,
                                title = texts.dayOfWeek,
                                nameOfDay = texts.nameOfDayOfWeek,
                                modifier = Modifier.fillMaxWidth(),
                                onDaySelected = { updateDayOfWeek(it) }
                            )
                        }
                        FrequencyField(uiProvider, texts.time, Modifier.weight(1f)) {
                            ui.TimeInput(
                                selectedTime = selectedFrequency.time,
                                onTimeSelected = { updateTime(it) },
                                title = texts.time,
                                setup = timeSetup,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                is Frequency.Monthly -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FrequencyField(uiProvider, texts.dayOfMonth, Modifier.weight(1f)) {
                            DayOfMonth(
                                uiProvider = uiProvider,
                                selectedMonth = DateMonth.JANUARY,
                                selectedDayOfMonth = selectedFrequency.dayOfMonth,
                                title = texts.dayOfMonth,
                                modifier = Modifier.fillMaxWidth(),
                                onDaySelected = { updateDayOfMonth(it) }
                            )
                        }
                        FrequencyField(uiProvider, texts.time, Modifier.weight(1f)) {
                            ui.TimeInput(
                                selectedTime = selectedFrequency.time,
                                onTimeSelected = { updateTime(it) },
                                title = texts.time,
                                setup = timeSetup,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                is Frequency.Yearly -> {
                    if (wideLayout) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FrequencyField(uiProvider, texts.monthOfYear, Modifier.weight(1f)) {
                                Month(
                                    uiProvider = uiProvider,
                                    selectedMonthOfYear = selectedFrequency.month,
                                    title = texts.monthOfYear,
                                    nameOfMonth = texts.nameOfMonth,
                                    modifier = Modifier.fillMaxWidth(),
                                    onMonthSelected = { updateMonth(it) }
                                )
                            }
                            FrequencyField(uiProvider, texts.dayOfMonth, Modifier.weight(1f)) {
                                DayOfMonth(
                                    uiProvider = uiProvider,
                                    selectedMonth = selectedFrequency.month,
                                    selectedDayOfMonth = selectedFrequency.dayOfMonth,
                                    title = texts.dayOfMonth,
                                    modifier = Modifier.fillMaxWidth(),
                                    onDaySelected = { updateDayOfMonth(it) }
                                )
                            }
                            FrequencyField(uiProvider, texts.time, Modifier.weight(1f)) {
                                ui.TimeInput(
                                    selectedTime = selectedFrequency.time,
                                    onTimeSelected = { updateTime(it) },
                                    title = texts.time,
                                    setup = timeSetup,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                FrequencyField(uiProvider, texts.monthOfYear, Modifier.weight(1f)) {
                                    Month(
                                        uiProvider = uiProvider,
                                        selectedMonthOfYear = selectedFrequency.month,
                                        title = texts.monthOfYear,
                                        nameOfMonth = texts.nameOfMonth,
                                        modifier = Modifier.fillMaxWidth(),
                                        onMonthSelected = { updateMonth(it) }
                                    )
                                }
                                FrequencyField(uiProvider, texts.dayOfMonth, Modifier.weight(1f)) {
                                    DayOfMonth(
                                        uiProvider = uiProvider,
                                        selectedMonth = selectedFrequency.month,
                                        selectedDayOfMonth = selectedFrequency.dayOfMonth,
                                        title = texts.dayOfMonth,
                                        modifier = Modifier.fillMaxWidth(),
                                        onDaySelected = { updateDayOfMonth(it) }
                                    )
                                }
                            }
                            FrequencyField(uiProvider, texts.time, Modifier.fillMaxWidth()) {
                                ui.TimeInput(
                                    selectedTime = selectedFrequency.time,
                                    onTimeSelected = { updateTime(it) },
                                    title = texts.time,
                                    setup = timeSetup,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FrequencyField(
    uiProvider: FrequencyUIProvider,
    label: String,
    modifier: Modifier,
    errorMessage: String? = null,
    content: @Composable () -> Unit
) {
    uiProvider.LabeledField(
        label = label,
        modifier = modifier,
        errorMessage = errorMessage,
        content = content
    )
}
