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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI
import com.michaelflisar.composedialogs.dialogs.date.classes.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.date.classes.DateViewState
import com.michaelflisar.composedialogs.dialogs.date.composables.CalendarHeader
import com.michaelflisar.composedialogs.dialogs.date.composables.CalendarMonth
import com.michaelflisar.composedialogs.dialogs.date.composables.CalendarSelectListMonth
import com.michaelflisar.composedialogs.dialogs.date.composables.CalendarSelectListYear
import com.michaelflisar.composedialogs.dialogs.date.composables.CalendarSelectionHeader
import com.michaelflisar.composedialogs.dialogs.date.composables.CalendarTodayButton
import com.michaelflisar.composedialogs.dialogs.date.utils.DateUtil
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

/**
 * Displays the date picker independently of a dialog container.
 *
 * @param date the selected date state
 * @param dateRange the supported date range
 * @param setup the date picker setup
 * @param title title content used in the landscape layout
 * @param icon icon content used in the landscape layout
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogDateContent(
    date: MutableState<LocalDate>,
    dateRange: DialogDate.Range = DialogDateDefaults.dateRange(),
    setup: DialogDate.Setup = DialogDateDefaults.setup(),
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null
) {
    val windowSize = LocalWindowInfo.current.containerSize
    val landscape = windowSize.width > windowSize.height
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
        derivedStateOf { CalendarPageData(pagerState.currentPage, dateRange) }
    }
    val viewState = remember { mutableStateOf(DateViewState.Calendar) }

    BackHandler(enabled = viewState.value != DateViewState.Calendar, onBack = {
        viewState.value = DateViewState.Calendar
    })

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
                    ComposeDialogsUI.provider.ProvideTextStyle(
                        style = ComposeDialogsUI.provider.textStyle(ComposeDialogsUI.TextRole.HeadlineSmall)
                    ) {
                        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            title()
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                CalendarSelectionHeader(date, setup, pagerState, currentPage, viewState)
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
            Column(modifier = modifier.animateContentSize()) {
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
    } else {
        Column(modifier = Modifier.animateContentSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CalendarSelectionHeader(date, setup, pagerState, currentPage, viewState)
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
