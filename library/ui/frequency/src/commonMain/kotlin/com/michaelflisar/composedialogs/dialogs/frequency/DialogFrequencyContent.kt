package com.michaelflisar.composedialogs.dialogs.frequency

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI
import com.michaelflisar.composedialogs.dialogs.frequency.classes.Frequency
import com.michaelflisar.composedialogs.dialogs.frequency.composables.DayOfMonth
import com.michaelflisar.composedialogs.dialogs.frequency.composables.Dropdown
import com.michaelflisar.composedialogs.dialogs.frequency.composables.NumericInput
import com.michaelflisar.composedialogs.dialogs.frequency.composables.Time
import com.michaelflisar.composedialogs.dialogs.time.rememberDialogTime
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

/**
 * Displays the frequency editor and keeps [frequency] synchronized with its controls.
 */
@Composable
fun DialogFrequencyContent(
    frequency: MutableState<Frequency>,
    texts: DialogFrequency.Texts = DialogFrequencyDefaults.texts(),
    supportedTypes: List<Frequency.Type> = Frequency.Type.entries,
    firstDayOffset: DayOfWeek = DayOfWeek.MONDAY
) {
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
            Frequency.Type.Daily -> Frequency.Daily(interval = interval, time = selectedTime.value)
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

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            ComposeDialogsUI.provider.Text(
                text = texts.every,
                role = ComposeDialogsUI.TextRole.Body,
                modifier = Modifier
            )
            NumericInput(
                modifier = Modifier.width(128.dp),
                title = texts.interval,
                value = selectedInterval,
            )
            Dropdown<Frequency.Type>(
                modifier = Modifier.wrapContentWidth(),
                items = supportedTypes,
                mapper = { item, _ -> texts.nameOfType(item, selectedInterval.value ?: 1) },
                selected = selectedType,
                title = texts.type
            )
        }

        when (selectedType.value) {
            Frequency.Type.Daily -> Time(selectedTime, texts)
            Frequency.Type.Weekly -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                com.michaelflisar.composedialogs.dialogs.frequency.composables.DayOfWeek(
                    selectedDayOfWeek,
                    firstDayOffset,
                    texts,
                    Modifier.weight(1f)
                )
                Time(selectedTime, texts, Modifier.weight(1f))
            }
            Frequency.Type.Monthly -> Row(
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
            Frequency.Type.Yearly -> Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                com.michaelflisar.composedialogs.dialogs.frequency.composables.Month(
                    selectedMonthOfYear,
                    texts,
                    Modifier.weight(1f)
                )
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
