package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.DatePickerViewState
import com.michaelflisar.composedialogs.dialogs.date.utils.DateUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

/**
 * Holds the date picker navigation state shared by [DatePicker] and caller-composed controls.
 */
@Stable
class DatePickerState internal constructor(
    internal val date: MutableState<LocalDate>,
    internal val pagerState: PagerState,
    internal val viewState: MutableState<DatePickerViewState>,
    private val currentPage: State<Int>,
    private val today: State<LocalDate>,
    private val todayPage: State<Int?>,
    private val scope: CoroutineScope
) {
    private val selectedDateEnabledState = derivedStateOf {
        pagerState.currentPage != currentPage.value &&
            viewState.value == DatePickerViewState.Calendar
    }

    private val todayEnabledState = derivedStateOf {
        todayPage.value != null &&
            (pagerState.currentPage != todayPage.value || date.value != today.value)
    }

    /** Whether the selected-date control can return to the selected date's month. */
    val selectedDateEnabled: Boolean
        get() = selectedDateEnabledState.value

    /** Whether the Today control can navigate to and select today's date. */
    val todayEnabled: Boolean
        get() = todayEnabledState.value

    /** Returns the calendar to the month containing the selected date. */
    fun onSelectedDateClick() {
        scope.launch { pagerState.animateScrollToPage(currentPage.value) }
    }

    /** Selects today's date and returns the calendar to its month. */
    fun onTodayClick() {
        date.value = today.value
        viewState.value = DatePickerViewState.Calendar
        todayPage.value?.let { page ->
            scope.launch { pagerState.animateScrollToPage(page) }
        }
    }
}

/**
 * Creates state to share between [DatePicker] and controls composed by the caller.
 *
 * The caller remains responsible for deciding where to place the calendar and its controls.
 */
@Composable
fun rememberDatePickerState(
    date: MutableState<LocalDate>,
    config: DatePickerConfig
): DatePickerState {
    val monthPages = (config.years.last - config.years.first + 1) * 12
    val currentPage = remember(date.value.year, date.value.month.number, config.years) {
        derivedStateOf {
            val offsetInYears = date.value.year - config.years.first
            val offsetMonths = date.value.month.number - 1
            offsetInYears * 12 + offsetMonths
        }
    }
    val today = remember {
        derivedStateOf { DateUtil.today() }
    }
    val todayPage = remember(monthPages, today.value, config.years) {
        derivedStateOf {
            val offsetInYears = today.value.year - config.years.first
            val offsetMonths = today.value.month.number - 1
            val offset = offsetInYears * 12 + offsetMonths
            offset.takeIf { it in 0..<monthPages }
        }
    }
    val pagerState = rememberPagerState(
        initialPage = currentPage.value,
        initialPageOffsetFraction = 0f,
        pageCount = { monthPages }
    )
    val viewState = rememberSaveable {
        mutableStateOf(DatePickerViewState.Calendar)
    }
    val scope = rememberCoroutineScope()

    return remember(
        date,
        pagerState,
        viewState,
        currentPage,
        today,
        todayPage,
        scope
    ) {
        DatePickerState(
            date = date,
            pagerState = pagerState,
            viewState = viewState,
            currentPage = currentPage,
            today = today,
            todayPage = todayPage,
            scope = scope
        )
    }
}
