package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarHeader
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarMonth
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarSelectListMonth
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarSelectListYear
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarSelectionHeader
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarTodayButton
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.DatePickerViewState
import com.michaelflisar.composedialogs.dialogs.date.utils.DateUtil
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import kotlinx.coroutines.launch

/**
 * Renders a complete date picker using the caller-supplied [DateUIProvider].
 *
 * [config] controls formatting and behavior as well as the supported year range.
 */
@Composable
fun DatePicker(
    uiProvider: DateUIProvider,
    date: MutableState<LocalDate>,
    config: DatePickerConfig,
    landscape: Boolean
) {
    DatePickerMain(
        uiProvider = uiProvider,
        date = date,
        config = config,
        landscape = landscape
    ) { selectedDateEnabled, onSelectedDateClick, todayEnabled, onTodayClick ->
        DatePickerButtons(
            uiProvider = uiProvider,
            date = date,
            config = config,
            landscape = landscape,
            selectedDateEnabled = selectedDateEnabled,
            onSelectedDateClick = onSelectedDateClick,
            todayEnabled = todayEnabled,
            onTodayClick = onTodayClick
        )
    }
}

/**
 * Renders the calendar and exposes navigation actions to [controls].
 *
 * The pager and view-mode state stay internal; callers only share [date] and can
 * compose controls alongside dialog-specific content without a picker state holder.
 *
 * @param controls receives the selected-date button's enabled state and click action,
 * followed by the Today button's enabled state and click action
 */
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun DatePickerMain(
    uiProvider: DateUIProvider,
    date: MutableState<LocalDate>,
    config: DatePickerConfig,
    landscape: Boolean,
    controls: @Composable (
        selectedDateEnabled: Boolean,
        onSelectedDateClick: () -> Unit,
        todayEnabled: Boolean,
        onTodayClick: () -> Unit
    ) -> Unit
) {
    val pages = config.years.last - config.years.first + 1
    val monthPages = pages * 12
    val currentPage = remember(date.value.year, date.value.month.number, config.years) {
        derivedStateOf {
            val offsetInYears = date.value.year - config.years.first
            val offsetMonths = date.value.month.number - 1
            offsetInYears * 12 + offsetMonths
        }
    }
    val today by remember {
        derivedStateOf { DateUtil.today() }
    }
    val todayPage by remember(monthPages, today, config.years) {
        derivedStateOf {
            val offsetInYears = today.year - config.years.first
            val offsetMonths = today.month.number - 1
            val offset = offsetInYears * 12 + offsetMonths
            offset.takeIf { it in 0..<monthPages }
        }
    }
    val pagerState = rememberPagerState(
        initialPage = currentPage.value,
        initialPageOffsetFraction = 0f,
        pageCount = { monthPages }
    )
    val pageData = remember(pagerState.currentPage, config.years) {
        derivedStateOf {
            CalendarPageData(pagerState.currentPage, config.years)
        }
    }
    val viewState = remember {
        mutableStateOf(DatePickerViewState.Calendar)
    }
    val scope = rememberCoroutineScope()

    val selectedDateEnabled by remember(
        pagerState.currentPage,
        currentPage.value,
        viewState.value
    ) {
        derivedStateOf {
            pagerState.currentPage != currentPage.value &&
                viewState.value == DatePickerViewState.Calendar
        }
    }
    val hasTodayButton = config.buttonToday != null
    val todayEnabled by remember(
        pagerState.currentPage,
        date.value,
        todayPage,
        hasTodayButton
    ) {
        derivedStateOf {
            hasTodayButton && todayPage != null &&
                (pagerState.currentPage != todayPage || date.value != today)
        }
    }

    val onSelectedDateClick: () -> Unit = {
        scope.launch { pagerState.animateScrollToPage(currentPage.value) }
    }
    val onTodayClick: () -> Unit = {
        date.value = today
        if (viewState.value != DatePickerViewState.Calendar) {
            viewState.value = DatePickerViewState.Calendar
        }
        todayPage?.let { page ->
            scope.launch { pagerState.animateScrollToPage(page) }
        }
    }

    BackHandler(enabled = viewState.value != DatePickerViewState.Calendar) {
        viewState.value = DatePickerViewState.Calendar
    }

    val controlsContent: @Composable () -> Unit = {
        controls(
            selectedDateEnabled,
            onSelectedDateClick,
            todayEnabled,
            onTodayClick
        )
    }
    val calendarItems: @Composable () -> Unit = {
        CalendarHeader(pagerState, config, pageData, viewState, uiProvider)
        when (viewState.value) {
            DatePickerViewState.Calendar -> {
                HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) {
                    val page = CalendarPageData(it, config.years)
                    CalendarMonth(date, config, page.year, page.month, uiProvider)
                }
            }

            DatePickerViewState.SelectYear -> {
                CalendarSelectListYear(
                    pageData,
                    config,
                    pagerState,
                    viewState,
                    uiProvider
                )
            }

            DatePickerViewState.SelectMonth -> {
                CalendarSelectListMonth(pageData, config, pagerState, viewState, uiProvider)
            }
        }
    }

    if (landscape) {
        Row {
            controlsContent()
            Spacer(modifier = Modifier.width(8.dp))
            val modifier = if (viewState.value == DatePickerViewState.Calendar) {
                Modifier.verticalScroll(rememberScrollState())
            } else {
                Modifier
            }
            Column(modifier = modifier.animateContentSize()) {
                calendarItems()
            }
        }
    } else {
        Column(modifier = Modifier.animateContentSize()) {
            controlsContent()
            Spacer(modifier = Modifier.height(8.dp))
            calendarItems()
        }
    }
}
