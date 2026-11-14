package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.datetime.LocalDate
import com.michaelflisar.composedialogs.dialogs.date.utils.DateUtil

private val DatePickerDateSaver = Saver<MutableState<LocalDate>, Long>(
    save = { it.value.toEpochDays() },
    restore = { mutableStateOf(LocalDate.fromEpochDays(it)) }
)

/**
 * Remembers a saveable date value for a date picker.
 *
 * @param date the initial date
 * @return the selected date state
 */
@Composable
fun rememberDatePickerDate(
    date: LocalDate = DateUtil.today()
): MutableState<LocalDate> = rememberSaveable(saver = DatePickerDateSaver) {
    mutableStateOf(date)
}
