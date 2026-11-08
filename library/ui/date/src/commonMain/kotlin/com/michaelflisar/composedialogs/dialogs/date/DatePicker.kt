package com.michaelflisar.composedialogs.dialogs.date

import androidx.compose.animation.animateContentSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.date.ui.internal.DatePickerViewState
import com.michaelflisar.composedialogs.dialogs.date.ui.DatePickerRange
import com.michaelflisar.composedialogs.dialogs.date.ui.DatePickerSetup
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarHeader
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarMonth
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarSelectListMonth
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarSelectListYear
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarSelectionHeader
import com.michaelflisar.composedialogs.dialogs.date.ui.CalendarTodayButton
import com.michaelflisar.composedialogs.dialogs.date.utils.DateUtil
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

/**
 * Renders a date picker using the caller-supplied [DateUIProvider].
 *
 * [DatePickerSetup] controls its formatting and behavior, while [DatePickerRange] limits selectable years.
 */
@Composable
fun DatePicker(
    uiProvider: DateUIProvider,
    date: MutableState<LocalDate>,
    dateRange: DatePickerRange,
    setup: DatePickerSetup,
    landscape: Boolean,
    title: (@Composable () -> Unit)?,
    icon: (@Composable () -> Unit)?
) {
    DatePickerContent(uiProvider, date, dateRange, setup, landscape, title, icon)
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun DatePickerContent(
    uiProvider: DateUIProvider,
    date: MutableState<LocalDate>,
    dateRange: DatePickerRange,
    setup: DatePickerSetup,
    landscape: Boolean,
    title: (@Composable () -> Unit)?,
    icon: (@Composable () -> Unit)?
) {
    val pages = (dateRange.years.last - dateRange.years.first + 1) * 12
    val currentPage = remember(date.value.year, date.value.month.number) {
        derivedStateOf {
            val offsetInYears = date.value.year - dateRange.years.first
            val offsetMonths = date.value.month.number - 1
            offsetInYears * 12 + offsetMonths
        }
    }
    val today by remember {
        derivedStateOf { DateUtil.today() }
    }
    val todayPage by remember(pages, today) {
        derivedStateOf {
            val offsetInYears = today.year - dateRange.years.first
            val offsetMonths = today.month.number - 1
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
        mutableStateOf(DatePickerViewState.Calendar)
    }

    BackHandler(enabled = viewState.value != DatePickerViewState.Calendar) {
        viewState.value = DatePickerViewState.Calendar
    }

    if (landscape) {
        Row {
            Column {
                if (icon != null) {
                    Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        icon()
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (title != null) {
                    uiProvider.DialogTitle {
                        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
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
                    viewState,
                    uiProvider
                )
                if (todayPage != null && setup.buttonToday != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    CalendarTodayButton(
                        setup.buttonToday,
                        date,
                        todayPage!!,
                        today,
                        pagerState,
                        viewState,
                        uiProvider
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            val modifier = when (viewState.value) {
                DatePickerViewState.Calendar -> Modifier.verticalScroll(rememberScrollState())
                DatePickerViewState.SelectYear,
                DatePickerViewState.SelectMonth -> Modifier
            }
            Column(modifier = modifier.animateContentSize()) {
                CalendarHeader(pagerState, dateRange, setup, pageData, viewState, uiProvider)
                when (viewState.value) {
                    DatePickerViewState.Calendar -> {
                        HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) {
                            val page = CalendarPageData(it, dateRange)
                            CalendarMonth(date, setup, page.year, page.month, uiProvider)
                        }
                    }

                    DatePickerViewState.SelectYear -> {
                        CalendarSelectListYear(
                            pageData,
                            setup,
                            dateRange,
                            pagerState,
                            viewState,
                            uiProvider
                        )
                    }

                    DatePickerViewState.SelectMonth -> {
                        CalendarSelectListMonth(pageData, setup, pagerState, viewState, uiProvider)
                    }
                }
            }
        }
    } else {
        Column(modifier = Modifier.animateContentSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CalendarSelectionHeader(
                    date,
                    setup,
                    pagerState,
                    currentPage,
                    viewState,
                    uiProvider
                )
                if (todayPage != null && setup.buttonToday != null) {
                    Spacer(modifier = Modifier.weight(1f))
                    CalendarTodayButton(
                        setup.buttonToday,
                        date,
                        todayPage!!,
                        today,
                        pagerState,
                        viewState,
                        uiProvider
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            CalendarHeader(pagerState, dateRange, setup, pageData, viewState, uiProvider)
            when (viewState.value) {
                DatePickerViewState.Calendar -> {
                    HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) {
                        val page = CalendarPageData(it, dateRange)
                        CalendarMonth(date, setup, page.year, page.month, uiProvider)
                    }
                }

                DatePickerViewState.SelectYear -> {
                    CalendarSelectListYear(
                        pageData,
                        setup,
                        dateRange,
                        pagerState,
                        viewState,
                        uiProvider
                    )
                }

                DatePickerViewState.SelectMonth -> {
                    CalendarSelectListMonth(pageData, setup, pagerState, viewState, uiProvider)
                }
            }
        }
    }
}
