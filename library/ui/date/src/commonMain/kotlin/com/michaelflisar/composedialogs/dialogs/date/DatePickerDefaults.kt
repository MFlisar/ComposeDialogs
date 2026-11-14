package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.date.resources.Res
import com.michaelflisar.composedialogs.date.resources.composedialogs_date_today
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource

@Immutable
class DatePickerTexts internal constructor(
    val today: String
)

@Stable
object DatePickerDefaults {

    @Composable
    fun texts(
        today: String = stringResource(Res.string.composedialogs_date_today)
    ) = DatePickerTexts(today = today)

    /**
     * @param buttonToday the optional content of the today button that is displayed at top next to the selected date
     * @param firstDayOfWeek the first day of the week (use [DayOfWeek.MONDAY] to [DayOfWeek.SUNDAY])
     * @param formatterWeekDayLabel the date format for the weekday names of the calendar
     * @param formatterSelectedDate the date format for the text that represents the currently selected date
     * @param formatterSelectedMonth the date format for the current month text
     * @param formatterSelectedYear the date format for the current year text
     * @param formatterMonthSelectorList the date format for the list in which you can select a month
     * @param formatterYearSelectorList the date format for the list in which you can select a year
     * @param dateCellHeight the height of cell representing a single day
     * @param showNextPreviousMonthButtons if true, the decrease/increase buttons next to the select month are shown
     * @param showNextPreviousYearButtons if true, the decrease/increase buttons next to the select year are shown
     * @param years the range of years supported by the date picker
     *
     * @return a [DatePickerConfig]
     */
    @Composable
    fun config(
        years: IntRange = 1900..2100,
        buttonToday: (@Composable () -> Unit)? = {
            BasicText(text = texts().today)
        },
        firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
        formatterWeekDayLabel: @Composable (dayOfWeek: DayOfWeek) -> String = {
            defaultFormatterWeekDayLabel(it)
        },
        formatterSelectedDate: @Composable (date: LocalDate) -> String = {
            defaultFormatterSelectedDate(it)
        },
        formatterSelectedMonth: @Composable (month: Month) -> String = {
            defaultFormatterSelectedMonth(it)
        },
        formatterSelectedYear: @Composable (year: Int) -> String = { it.toString() },
        formatterMonthSelectorList: @Composable (month: Month) -> String = {
            defaultFormatterSelectedMonthInSelectorList(it)
        },
        formatterYearSelectorList: @Composable (year: Int) -> String = { it.toString() },
        dateCellHeight: Dp = 48.dp,
        showNextPreviousMonthButtons: Boolean = true,
        showNextPreviousYearButtons: Boolean = true
    ) = DatePickerConfig(
        years,
        buttonToday,
        firstDayOfWeek,
        formatterWeekDayLabel,
        formatterSelectedDate,
        formatterSelectedMonth,
        formatterSelectedYear,
        formatterMonthSelectorList,
        formatterYearSelectorList,
        dateCellHeight,
        showNextPreviousMonthButtons,
        showNextPreviousYearButtons
    )
}
