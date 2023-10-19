package com.michaelflisar.composedialogs.dialogs.datetime.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.datetime.DialogDateRange
import com.michaelflisar.composedialogs.dialogs.datetime.DialogDateSetup
import com.michaelflisar.composedialogs.dialogs.datetime.classes.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.datetime.classes.DateViewState
import com.michaelflisar.composedialogs.dialogs.datetime.utils.DateUtil
import kotlinx.coroutines.launch
import java.time.Month
import java.time.Year

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CalendarSelectListYear(
    pageData: State<CalendarPageData>,
    setup: DialogDateSetup,
    dateRange: DialogDateRange,
    pagerState: PagerState,
    viewState: MutableState<DateViewState>
) {
    val scope = rememberCoroutineScope()
    CalendarSelectList(
        value = pageData.value.year,
        range = dateRange.years,
        itemFormatter = {
            setup.formatterYearSelectorList(Year.of(it))
        }
    ) {
        val offsetPages = (it - dateRange.years.first) * 12 + pageData.value.month - 1
        scope.launch { pagerState.scrollToPage(offsetPages) }
        viewState.value = DateViewState.Calendar
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CalendarSelectListMonth(
    pageData: State<CalendarPageData>,
    setup: DialogDateSetup,
    pagerState: PagerState,
    viewState: MutableState<DateViewState>
) {
    val scope = rememberCoroutineScope()
    CalendarSelectList(
        value = pageData.value.month,
        range = (1..12),
        itemFormatter = {
            setup.formatterMonthSelectorList(Month.of(it))
        }
    ) {
        val offsetPages = it - pageData.value.month
        scope.launch { pagerState.scrollToPage(pagerState.currentPage + offsetPages) }
        viewState.value = DateViewState.Calendar
    }
}

@Composable
private fun CalendarSelectList(
    value: Int,
    range: IntRange,
    itemFormatter: (value: Int) -> String,
    onClick: (value: Int) -> Unit
) {
    val state = rememberLazyListState(
        value - range.first
    )
    LazyColumn(state = state) {
        range.forEach {
            item {
                Row(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable {
                            onClick(it)
                        }
                        .minimumInteractiveComponentSize()
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.alpha(if (it == value) 1f else 0f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        itemFormatter(it),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}