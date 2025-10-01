package com.michaelflisar.composedialogs.dialogs.frequency

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.dialogs.frequency.classes.Frequency
import com.michaelflisar.composedialogs.dialogs.frequency.classes.FrequencyStateSaver
import com.michaelflisar.composedialogs.dialogs.frequency.composables.Dropdown
import com.michaelflisar.composedialogs.dialogs.frequency.composables.InputButton
import com.michaelflisar.composedialogs.dialogs.frequency.composables.NumericInput
import com.michaelflisar.composedialogs.dialogs.time.DialogTime
import com.michaelflisar.composedialogs.dialogs.time.rememberDialogTime
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

/* --8<-- [start: constructor] */
/**
 * Shows a frequency dialog
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param frequency the frequency state of the dialog
 * @param texts the texts ([DialogFrequency.Texts]) that are used inside this dialog - use [DialogFrequencyDefaults.texts] to provide your own data
 * @param supportedTypes the supported frequency types - default is all types
 * @param firstDayOffset the first day of the week - default is [DayOfWeek.MONDAY]
 */
@Composable
fun DialogFrequency(
    state: DialogState,
    // Custom - Required
    frequency: MutableState<Frequency>,
    // Custom - Optional
    texts: DialogFrequency.Texts = DialogFrequencyDefaults.texts(),
    supportedTypes: List<Frequency.Type> = Frequency.Type.entries,
    firstDayOffset: DayOfWeek = DayOfWeek.MONDAY,
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
/* --8<-- [end: constructor] */
{
    if (state.visible) {

        val selectedType = rememberSaveable { mutableStateOf(frequency.value.type) }
        val selectedTime = rememberDialogTime(frequency.value.time)
        val selectedDayOfWeek = rememberSaveable {
            mutableStateOf(
                when (val f = frequency.value) {
                    is Frequency.Weekly -> f.dayOfWeek
                    else -> DayOfWeek.MONDAY
                }
            )
        }
        val selectedDayOfMonth = rememberSaveable {
            mutableStateOf(
                when (val f = frequency.value) {
                    is Frequency.Monthly -> f.dayOfMonth
                    is Frequency.Yearly -> f.dayOfMonth
                    else -> 1
                }
            )
        }
        val selectedMonthOfYear = rememberSaveable {
            mutableStateOf(
                when (val f = frequency.value) {
                    is Frequency.Yearly -> f.month
                    else -> Month.JANUARY
                }
            )
        }
        val selectedInterval = rememberSaveable { mutableStateOf<Int?>(frequency.value.interval) }

        // update frequency if something changes
        LaunchedEffect(
            selectedType.value,
            selectedTime.value,
            selectedDayOfWeek.value,
            selectedDayOfMonth.value,
            selectedMonthOfYear.value,
            selectedInterval.value
        ) {
            val interval = selectedInterval.value ?: 1
            frequency.value = when (selectedType.value) {
                Frequency.Type.Daily -> Frequency.Daily(
                    interval = interval,
                    time = selectedTime.value
                )

                Frequency.Type.Weekly -> Frequency.Weekly(
                    interval = interval,
                    dayOfWeek = selectedDayOfWeek.value,
                    time = selectedTime.value
                )

                Frequency.Type.Monthly -> Frequency.Monthly(
                    interval = interval,
                    dayOfMonth = selectedDayOfMonth.value,
                    time = selectedTime.value
                )

                Frequency.Type.Yearly -> Frequency.Yearly(
                    interval = interval,
                    month = selectedMonthOfYear.value,
                    dayOfMonth = selectedDayOfMonth.value,
                    time = selectedTime.value
                )
            }
        }

        Dialog(
            state = state,
            title = title,
            icon = icon,
            style = style,
            buttons = buttons,
            options = options,
            onEvent = onEvent
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.animateContentSize()
            ) {
                // 1) Interval + Type
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(texts.every)
                    NumericInput(
                        modifier = Modifier.width(128.dp),
                        title = texts.interval,
                        value = selectedInterval,
                    )
                    Dropdown<Frequency.Type>(
                        modifier = Modifier.wrapContentWidth(),
                        items = supportedTypes,
                        mapper = { item, dropdown ->
                            texts.nameOfType(
                                item,
                                selectedInterval.value ?: 1
                            )
                        },
                        selected = selectedType,
                        title = texts.type
                    )
                }

                // 2) Depending on type, show other inputs
                when (selectedType.value) {
                    Frequency.Type.Daily -> {
                        Time(selectedTime, texts)
                    }

                    Frequency.Type.Weekly -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DayOfWeek(selectedDayOfWeek, firstDayOffset, texts, Modifier.weight(1f))
                            Time(selectedTime, texts, Modifier.weight(1f))
                        }
                    }

                    Frequency.Type.Monthly -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DayOfMonth(
                                selectedMonthOfYear.value,
                                selectedDayOfMonth,
                                texts,
                                Modifier.weight(1f)
                            )
                            Time(selectedTime, texts, Modifier.weight(1f))
                        }
                    }

                    Frequency.Type.Yearly -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Month(selectedMonthOfYear, texts, Modifier.weight(1f))
                            DayOfMonth(
                                selectedMonthOfYear.value,
                                selectedDayOfMonth,
                                texts,
                                Modifier.weight(1f)
                            )
                            Time(selectedTime, texts, Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Month(
    selectedMonthOfYear: MutableState<Month>,
    texts: DialogFrequency.Texts,
    modifier: Modifier = Modifier
) {
    Dropdown<Month>(
        modifier = modifier,
        items = Month.entries,
        mapper = { item, dropdown -> texts.nameOfMonth(item) },
        selected = selectedMonthOfYear,
        title = texts.monthOfYear
    )
}

@Composable
private fun DayOfMonth(
    selectedMonth: Month,
    selectedDayOfMonth: MutableState<Int>,
    texts: DialogFrequency.Texts,
    modifier: Modifier = Modifier
) {
    val items = remember(selectedMonth) {
        when (selectedMonth) {
            Month.FEBRUARY -> (1..29).toList()
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> (1..30).toList()
            else -> (1..31).toList()
        }
    }
    LaunchedEffect(items.size) {
        if (selectedDayOfMonth.value > items.last()) {
            selectedDayOfMonth.value = items.last()
        }
    }
    Dropdown<Int>(
        modifier = modifier,
        items = items,
        mapper = { item, dropdown -> item.toString() },
        selected = selectedDayOfMonth,
        title = texts.dayOfMonth
    )
}

@Composable
private fun DayOfWeek(
    selectedDayOfWeek: MutableState<DayOfWeek>,
    firstDayOffset: DayOfWeek,
    texts: DialogFrequency.Texts,
    modifier: Modifier = Modifier
) {
    val items by remember {
        derivedStateOf {
            val days = DayOfWeek.entries.toMutableList()
            while (days.first() != firstDayOffset) {
                val first = days.drop(1)
                days += first
            }
            days
        }
    }
    Dropdown<DayOfWeek>(
        modifier = modifier,
        items = items,
        mapper = { item, dropdown -> texts.nameOfDayOfWeek(item) },
        selected = selectedDayOfWeek,
        title = texts.dayOfWeek
    )
}

@Composable
private fun Time(
    selectedTime: MutableState<kotlinx.datetime.LocalTime>,
    texts: DialogFrequency.Texts,
    modifier: Modifier = Modifier
) {
    val showDialogTime = rememberDialogState()
    InputButton(
        modifier = modifier,
        title = texts.time,
        value = selectedTime.value.toString(),
        onClick = {
            showDialogTime.show()
        }
    )
    if (showDialogTime.visible) {
        DialogTime(
            state = showDialogTime,
            time = selectedTime,
            title = { Text(texts.time) }
        )
    }
}

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

/**
 * convenient function for [DialogFrequency]
 *
 * @param frequency the frequency state of the dialog
 *
 * @return a state holding the current frequency value
 */
@Composable
fun rememberDialogFrequency(
    frequency: Frequency
): MutableState<Frequency> {
    return rememberSaveable(saver = FrequencyStateSaver) { mutableStateOf(frequency) }
}

object DialogFrequencyDefaults {


    @Composable
    fun texts(
        interval: String = "Interval",
        type: String = "Type",
        every: String = "Every",
        monthOfYear: String = "Month of Year",
        dayOfMonth: String = "Day of Month",
        dayOfWeek: String = "Day of Week",
        time: String = "Time",
        nameOfType: (day: Frequency.Type, interval: Int) -> String = { type, interval ->
            val singular = when (type) {
                Frequency.Type.Daily -> "day"
                Frequency.Type.Weekly -> "week"
                Frequency.Type.Monthly -> "month"
                Frequency.Type.Yearly -> "year"
            }
            val plural = singular + "s"
            if (interval == 1) singular else plural
        },
        nameOfDayOfWeek: (day: DayOfWeek) -> String = { it.name.lowercase().replaceFirstChar { it.uppercase() } },
        nameOfMonth: (month: Month) -> String = { it.name.lowercase().replaceFirstChar { it.uppercase() }  },
    ): DialogFrequency.Texts {
        return DialogFrequency.Texts(
            interval = interval,
            type = type,
            every = every,
            monthOfYear = monthOfYear,
            dayOfMonth = dayOfMonth,
            dayOfWeek = dayOfWeek,
            time = time,
            nameOfType = nameOfType,
            nameOfDayOfWeek = nameOfDayOfWeek,
            nameOfMonth = nameOfMonth
        )
    }
}