package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.BackHandler
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.isLandscape
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.dialogs.date.classes.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.date.classes.DateViewState
import com.michaelflisar.composedialogs.dialogs.date.composables.CalendarHeader
import com.michaelflisar.composedialogs.dialogs.date.composables.CalendarMonth
import com.michaelflisar.composedialogs.dialogs.date.composables.CalendarSelectListMonth
import com.michaelflisar.composedialogs.dialogs.date.composables.CalendarSelectListYear
import com.michaelflisar.composedialogs.dialogs.date.composables.CalendarSelectionHeader
import com.michaelflisar.composedialogs.dialogs.date.composables.CalendarTodayButton
import com.michaelflisar.composedialogs.dialogs.date.resources.Res
import com.michaelflisar.composedialogs.dialogs.date.resources.composedialogs_date_today
import com.michaelflisar.composedialogs.dialogs.date.utils.DateUtil
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource

/**
 * Shows a dialog with an input field
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param date the selected date state
 * @param dateRange the supported [DialogDate.Range] - use [DialogDateDefaults.dateRange] to provide your own data
 * @param setup the [DialogDate.Setup] - use [DialogDateDefaults.setup] to provide your own data
 */
@Composable
fun DialogDate(
    state: BaseDialogState,
    // Custom - Required
    date: MutableState<LocalDate>,
    // Custom - Optional
    dateRange: DialogDate.Range = DialogDateDefaults.dateRange(),
    setup: DialogDate.Setup = DialogDateDefaults.setup(),
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    val landscape = isLandscape()
    Dialog(
        state,
        title?.takeIf { !landscape },
        icon?.takeIf { !landscape },
        style,
        buttons,
        options,
        onEvent = onEvent
    ) {
        val pages = (dateRange.years.last - dateRange.years.first + 1) * 12
        val currentPage = remember(date.value.year, date.value.monthNumber) {
            derivedStateOf {
                val offsetInYears = date.value.year - dateRange.years.first
                val offsetMonths = date.value.monthNumber - 1
                offsetInYears * 12 + offsetMonths
            }
        }
        val today by remember {
            derivedStateOf { DateUtil.today() }
        }
        val todayPage by remember(pages, today) {
            derivedStateOf {
                val offsetInYears = today.year - dateRange.years.first
                val offsetMonths = today.monthNumber - 1
                val offset = offsetInYears * 12 + offsetMonths
                offset.takeIf { it in 0..<pages }
            }
        }
        val pagerState = rememberPagerState(
            initialPage = currentPage.value,
            initialPageOffsetFraction = 0f,
            pageCount = { pages }
        )

        val pageData = remember(pagerState.currentPage) {
            derivedStateOf {
                CalendarPageData(pagerState.currentPage, dateRange)
            }
        }

        val viewState = remember {
            mutableStateOf(DateViewState.Calendar)
        }

        BackHandler(enabled = viewState.value != DateViewState.Calendar, onBack = {
            viewState.value = DateViewState.Calendar
        })

        if (landscape) {
            Row {
                Column {
                    // custom icon and title placement in case of landscape mode!
                    if (icon != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        ) {
                            icon()
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    if (title != null) {
                        CompositionLocalProvider(
                            LocalTextStyle provides MaterialTheme.typography.headlineSmall
                        ) {
                            Box(
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                title()
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    CalendarSelectionHeader(
                        date,
                        setup,
                        pagerState,
                        currentPage,
                        viewState
                    )
                    if (todayPage != null && setup.buttonToday != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        CalendarTodayButton(
                            setup.buttonToday,
                            date,
                            todayPage!!,
                            today,
                            pagerState,
                            viewState
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                val modifier = when (viewState.value) {
                    DateViewState.Calendar -> Modifier.verticalScroll(rememberScrollState())
                    DateViewState.SelectYear -> Modifier
                    DateViewState.SelectMonth -> Modifier
                }
                Column(
                    modifier = modifier
                ) {
                    CalendarHeader(pagerState, dateRange, setup, pageData, viewState)
                    when (viewState.value) {
                        DateViewState.Calendar -> {
                            HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) {
                                val pageData = CalendarPageData(it, dateRange)
                                CalendarMonth(date, setup, pageData.year, pageData.month)
                            }
                        }

                        DateViewState.SelectYear -> {
                            CalendarSelectListYear(
                                pageData,
                                setup,
                                dateRange,
                                pagerState,
                                viewState
                            )
                        }

                        DateViewState.SelectMonth -> {
                            CalendarSelectListMonth(pageData, setup, pagerState, viewState)
                        }
                    }
                }
            }
        } else {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CalendarSelectionHeader(
                        date,
                        setup,
                        pagerState,
                        currentPage,
                        viewState
                    )
                    if (todayPage != null && setup.buttonToday != null) {
                        Spacer(modifier = Modifier.weight(1f))
                        CalendarTodayButton(
                            setup.buttonToday,
                            date,
                            todayPage!!,
                            today,
                            pagerState,
                            viewState
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                CalendarHeader(pagerState, dateRange, setup, pageData, viewState)
                when (viewState.value) {
                    DateViewState.Calendar -> {
                        HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) {
                            val pageData = CalendarPageData(it, dateRange)
                            CalendarMonth(date, setup, pageData.year, pageData.month)
                        }
                    }

                    DateViewState.SelectYear -> {
                        CalendarSelectListYear(pageData, setup, dateRange, pagerState, viewState)
                    }

                    DateViewState.SelectMonth -> {
                        CalendarSelectListMonth(pageData, setup, pagerState, viewState)
                    }
                }
            }
        }
    }
}

@Stable
object DialogDate {

    /**
     * see [DialogDateDefaults.setup]
     *
     */
    @Immutable
    class Setup internal constructor(
        val buttonToday: (@Composable (enabled: Boolean, onClick: () -> Unit) -> Unit)?,
        val firstDayOfWeek: DayOfWeek,
        val formatterWeekDayLabel: (date: DayOfWeek) -> String,
        val formatterSelectedDate: (date: LocalDate) -> String,
        val formatterSelectedMonth: (month: Month) -> String,
        val formatterSelectedYear: (year: Int) -> String,
        val formatterMonthSelectorList: (month: Month) -> String,
        val formatterYearSelectorList: (year: Int) -> String,
        val dateCellHeight: Dp,
        val showNextPreviousMonthButtons: Boolean,
        val showNextPreviousYearButtons: Boolean
    )

    /**
     * see [DialogDateDefaults.dateRange]
     *
     */
    @Immutable
    class Range internal constructor(
        val years: IntRange
    )

    val LocalDateSaver = Saver<MutableState<LocalDate>, Int>(
        save = { it.value.toEpochDays() },
        restore = { mutableStateOf(LocalDate.fromEpochDays(it)) }
    )
}

/**
 * @param date the initial date
 *
 * @return a state holding the current date value
 */
@Composable
fun rememberDialogDate(
    date: LocalDate = DateUtil.today()
): MutableState<LocalDate> {
    return rememberSaveable(
        saver = DialogDate.LocalDateSaver
    ) {
        mutableStateOf(date)
    }
}

@Stable
object DialogDateDefaults {

    /**
     * @param buttonToday the optional today button that is displayed at top next to the selected date
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
     *
     * @return a [DialogDateSetup]
     *
     */
    @Composable
    fun setup(
        buttonToday: (@Composable (enabled: Boolean, onClick: () -> Unit) -> Unit)? = { enabled, onClick ->
            OutlinedButton(
                onClick = onClick,
                enabled = enabled
            ) {
                Text(text = stringResource(Res.string.composedialogs_date_today))
            }
        },
        firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
        formatterWeekDayLabel: (dayOfWeek: DayOfWeek) -> String = {
            defaultFormatterWeekDayLabel(it)
        },
        formatterSelectedDate: (date: LocalDate) -> String = {
            defaultFormatterSelectedDate(it)
        },
        formatterSelectedMonth: (month: Month) -> String = {
            defaultFormatterSelectedMonth(it)

        },
        formatterSelectedYear: (year: Int) -> String = { it.toString() },
        formatterMonthSelectorList: (month: Month) -> String = {
            defaultFormatterSelectedMonthInSelectorList(it)
        },
        formatterYearSelectorList: (year: Int) -> String = { it.toString() },
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
     * @return a [DialogDateRange]
     */
    fun dateRange(
        years: IntRange = 1900..2100
    ) = DialogDate.Range(years)
}