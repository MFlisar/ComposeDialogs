package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.date.resources.Res
import com.michaelflisar.composedialogs.date.resources.composedialogs_date_today
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource

@Stable
object DialogDateDefaults {

    /**
     * @param buttonToday the optional content of the today button that is displayed at top next to the selected date
     * @param firstDayOfWeek the first day of the week (use [kotlinx.datetime.DayOfWeek.MONDAY] to [kotlinx.datetime.DayOfWeek.SUNDAY])
     * @param formatterWeekDayLabel the date format for the weekday names of the calendar
     * @param formatterSelectedDate the date format for the text that represents the currently selected date
     * @param formatterSelectedMonth the date format for the current month text
     * @param formatterSelectedYear the date format for the current year text
     * @param formatterMonthSelectorList the date format for the list in which you can select a month
     * @param formatterYearSelectorList the date format for the list in which you can select a year
     * @param dateCellHeight the height of cell representing a single day
     * @param showNextPreviousMonthButtons if true, the decrease/increase buttons next to the select month are shown
     * @param showNextPreviousYearButtons if true, the decrease/increase buttons next to the select year are shown
     *
     * @return a [DialogDate.Setup]
     *
     */
    @Composable
    fun setup(
        buttonToday: (@Composable () -> Unit)? = {
            Text(text = stringResource(Res.string.composedialogs_date_today))
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
    ) = DialogDate.Setup(
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

    /**
     * @param years the range that will be supported by the date picker
     *
     * @return a [DialogDate.Range]
     */
    fun dateRange(
        years: IntRange = 1900..2100
    ) = DialogDate.Range(years)
}