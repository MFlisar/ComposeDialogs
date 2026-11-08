package com.michaelflisar.composedialogs.dialogs.frequency

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.frequency.ui.DayOfMonth
import com.michaelflisar.composedialogs.dialogs.frequency.ui.DayOfWeek
import com.michaelflisar.composedialogs.dialogs.frequency.ui.Month
import com.michaelflisar.composedialogs.dialogs.frequency.ui.NumericInput
import com.michaelflisar.composedialogs.dialogs.time.TimePickerDefaults
import com.michaelflisar.composedialogs.dialogs.time.TimePickerSetup
import kotlinx.datetime.DayOfWeek as DateDayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month as DateMonth

enum class FrequencyPickerType {
    Daily,
    Weekly,
    Monthly,
    Yearly
}

/**
 * Renders a frequency picker using the caller-supplied [FrequencyUIProvider].
 *
 * Texts, supported types, and selection state are supplied by the caller; the UI provider renders
 * the controls, including the time input.
 */
@Composable
fun FrequencyPicker(
    uiProvider: FrequencyUIProvider,
    texts: FrequencyPickerTexts,
    supportedTypes: List<FrequencyPickerType>,
    firstDayOffset: DateDayOfWeek,
    selectedType: MutableState<FrequencyPickerType>,
    selectedTime: MutableState<LocalTime>,
    selectedDayOfWeek: MutableState<DateDayOfWeek>,
    selectedDayOfMonth: MutableState<Int>,
    selectedMonthOfYear: MutableState<DateMonth>,
    selectedInterval: MutableState<Int?>,
    intervalInputType: FrequencyIntervalInputType = FrequencyIntervalInputType.Input,
    timeSetup: TimePickerSetup = TimePickerDefaults.setup()
) {
    val ui = uiProvider
    val interval = selectedInterval.value
    val intervalError = if (interval == null || interval <= 0) {
        texts.invalidInterval
    } else {
        null
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
                                value = selectedInterval
                            )
                        }

                        is FrequencyIntervalInputType.Dropdown -> {
                            ui.IntervalDropdown(
                                modifier = Modifier.fillMaxWidth(),
                                title = texts.interval,
                                min = inputType.min,
                                max = inputType.max,
                                selected = selectedInterval.value ?: inputType.min,
                                onItemSelected = { selectedInterval.value = it }
                            )
                        }
                    }
                }
                FrequencyField(uiProvider, texts.type, Modifier.weight(1f)) {
                    ui.FrequencyTypeDropdown(
                        modifier = Modifier.fillMaxWidth(),
                        items = supportedTypes,
                        itemText = { texts.nameOfType(it, selectedInterval.value ?: 1) },
                        selected = selectedType.value,
                        title = texts.type,
                        onItemSelected = { selectedType.value = it }
                    )
                }
            }
            when (selectedType.value) {
                FrequencyPickerType.Daily -> {
                    FrequencyField(uiProvider, texts.time, Modifier.fillMaxWidth()) {
                        ui.TimeInput(
                            selectedTime = selectedTime,
                            title = texts.time,
                            setup = timeSetup,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                FrequencyPickerType.Weekly -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FrequencyField(uiProvider, texts.dayOfWeek, Modifier.weight(1f)) {
                            DayOfWeek(
                                uiProvider,
                                selectedDayOfWeek,
                                firstDayOffset,
                                texts.dayOfWeek,
                                texts.nameOfDayOfWeek,
                                Modifier.fillMaxWidth()
                            )
                        }
                        FrequencyField(uiProvider, texts.time, Modifier.weight(1f)) {
                            ui.TimeInput(
                                selectedTime = selectedTime,
                                title = texts.time,
                                setup = timeSetup,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                FrequencyPickerType.Monthly -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FrequencyField(uiProvider, texts.dayOfMonth, Modifier.weight(1f)) {
                            DayOfMonth(
                                uiProvider,
                                selectedMonthOfYear.value,
                                selectedDayOfMonth,
                                texts.dayOfMonth,
                                Modifier.fillMaxWidth()
                            )
                        }
                        FrequencyField(uiProvider, texts.time, Modifier.weight(1f)) {
                            ui.TimeInput(
                                selectedTime = selectedTime,
                                title = texts.time,
                                setup = timeSetup,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                FrequencyPickerType.Yearly -> {
                    if (wideLayout) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FrequencyField(uiProvider, texts.monthOfYear, Modifier.weight(1f)) {
                                Month(
                                    uiProvider,
                                    selectedMonthOfYear,
                                    texts.monthOfYear,
                                    texts.nameOfMonth,
                                    Modifier.fillMaxWidth()
                                )
                            }
                            FrequencyField(uiProvider, texts.dayOfMonth, Modifier.weight(1f)) {
                                DayOfMonth(
                                    uiProvider,
                                    selectedMonthOfYear.value,
                                    selectedDayOfMonth,
                                    texts.dayOfMonth,
                                    Modifier.fillMaxWidth()
                                )
                            }
                            FrequencyField(uiProvider, texts.time, Modifier.weight(1f)) {
                                ui.TimeInput(
                                    selectedTime = selectedTime,
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
                                        uiProvider,
                                        selectedMonthOfYear,
                                        texts.monthOfYear,
                                        texts.nameOfMonth,
                                        Modifier.fillMaxWidth()
                                    )
                                }
                                FrequencyField(uiProvider, texts.dayOfMonth, Modifier.weight(1f)) {
                                    DayOfMonth(
                                        uiProvider,
                                        selectedMonthOfYear.value,
                                        selectedDayOfMonth,
                                        texts.dayOfMonth,
                                        Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            FrequencyField(uiProvider, texts.time, Modifier.fillMaxWidth()) {
                                ui.TimeInput(
                                    selectedTime = selectedTime,
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
