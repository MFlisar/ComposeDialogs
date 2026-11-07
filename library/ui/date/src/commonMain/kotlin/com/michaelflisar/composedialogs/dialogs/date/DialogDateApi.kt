package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composedialogs.dialogs.date.utils.DateUtil
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

@Stable
object DialogDate {

    @Immutable
    class Setup internal constructor(
        val buttonToday: (@Composable (enabled: Boolean, onClick: () -> Unit) -> Unit)?,
        val firstDayOfWeek: DayOfWeek,
        val formatterWeekDayLabel: @Composable (date: DayOfWeek) -> String,
        val formatterSelectedDate: @Composable (date: LocalDate) -> String,
        val formatterSelectedMonth: @Composable (month: Month) -> String,
        val formatterSelectedYear: @Composable (year: Int) -> String,
        val formatterMonthSelectorList: @Composable (month: Month) -> String,
        val formatterYearSelectorList: @Composable (year: Int) -> String,
        val dateCellHeight: Dp,
        val showNextPreviousMonthButtons: Boolean,
        val showNextPreviousYearButtons: Boolean
    )

    @Immutable
    class Range internal constructor(val years: IntRange)

    val LocalDateSaver = Saver<MutableState<LocalDate>, Long>(
        save = { it.value.toEpochDays() },
        restore = { mutableStateOf(LocalDate.fromEpochDays(it)) }
    )
}

@Composable
fun rememberDialogDate(date: LocalDate = DateUtil.today()): MutableState<LocalDate> =
    rememberSaveable(saver = DialogDate.LocalDateSaver) { mutableStateOf(date) }
