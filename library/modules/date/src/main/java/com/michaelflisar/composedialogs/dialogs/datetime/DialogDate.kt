package com.michaelflisar.composedialogs.dialogs.datetime

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogStyle
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.dialogs.datetime.classes.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.datetime.classes.DateViewState
import com.michaelflisar.composedialogs.dialogs.datetime.composables.CalendarHeader
import com.michaelflisar.composedialogs.dialogs.datetime.composables.CalendarMonth
import com.michaelflisar.composedialogs.dialogs.datetime.composables.CalendarSelectListMonth
import com.michaelflisar.composedialogs.dialogs.datetime.composables.CalendarSelectListYear
import com.michaelflisar.composedialogs.dialogs.datetime.composables.CalendarSelectionHeader
import com.michaelflisar.composedialogs.dialogs.datetime.composables.CalendarTodayButton
import com.michaelflisar.composedialogs.dialogs.datetime.utils.DateUtil
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

/**
 * Shows a dialog with an input field
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param date the state ([DialogDateState]) for the date dialog
 * @param dateRange the supported [DialogDateRange]
 * @param setup the [DialogDateSetup]
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogDate(
    state: DialogState,
    // Custom - Required
    date: MutableState<LocalDate>,
    // Custom - Optional
    dateRange: DialogDateRange = DialogDateRange(),
    setup: DialogDateSetup = DialogDateSetup(),
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    val landscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
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
        val currentPage = remember(date.value.year, date.value.monthValue) {
            derivedStateOf {
                val offsetInYears = date.value.year - dateRange.years.first
                val offsetMonths = date.value.monthValue - 1
                offsetInYears * 12 + offsetMonths
            }
        }
        val today by remember {
            derivedStateOf { DateUtil.today() }
        }
        val todayPage by remember(pages, today) {
            derivedStateOf {
                val offsetInYears = today.year - dateRange.years.first
                val offsetMonths = today.monthValue - 1
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
                        currentPage
                    )
                    if (todayPage != null && setup.buttonToday != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        CalendarTodayButton(setup.buttonToday, date, todayPage!!, today, pagerState)
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
                        currentPage
                    )
                    if (todayPage != null && setup.buttonToday != null) {
                        Spacer(modifier = Modifier.weight(1f))
                        CalendarTodayButton(setup.buttonToday, date, todayPage!!, today, pagerState)
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

/**
 * date picker setup
 *
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
 */
class DialogDateSetup(
    val buttonToday: (@Composable (enabled: Boolean, onClick: () -> Unit) -> Unit)? = { enabled, onClick ->
        OutlinedButton(
            onClick = onClick,
            enabled = enabled
        ) {
            Text(text = "Today")
        }
    },
    val firstDayOfWeek: DayOfWeek = DayOfWeek.MONDAY,
    val formatterWeekDayLabel: (date: DayOfWeek) -> String = { it.getDisplayName(TextStyle.SHORT, Locale.getDefault()) },
    val formatterSelectedDate: (date: LocalDate) -> String = { it.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) },
    val formatterSelectedMonth: (month: Month) -> String = { it.getDisplayName(TextStyle.SHORT, Locale.getDefault()) },
    val formatterSelectedYear: (year: Year) -> String = { it.toString() },
    val formatterMonthSelectorList: (month: Month) -> String = { it.getDisplayName(TextStyle.FULL, Locale.getDefault()) },
    val formatterYearSelectorList: (year: Year) -> String = { it.toString() },
    val dateCellHeight: Dp = 48.dp,
    val showNextPreviousMonthButtons: Boolean = true,
    val showNextPreviousYearButtons: Boolean = true
)

/**
 * range for the date picker
 *
 * @param years the range that will be supported by the date picker
 */
class DialogDateRange(
    val years: IntRange = 1900..2100
)

/**
 * convenient function for [DialogDate]
 *
 * @param initialDate the initial date
 *
 * @return a state holding the current date value
 */
@Composable
fun rememberDialogDate(
    date: LocalDate = LocalDate.now()
): MutableState<LocalDate> {
    return rememberSaveable {
        mutableStateOf(date)
    }
}