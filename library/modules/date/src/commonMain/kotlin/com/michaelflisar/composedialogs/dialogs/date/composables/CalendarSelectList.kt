package com.michaelflisar.composedialogs.dialogs.date.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import com.michaelflisar.composedialogs.core.DialogContentScrollableLazyColumn
import com.michaelflisar.composedialogs.dialogs.date.DialogDate
import com.michaelflisar.composedialogs.dialogs.date.classes.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.date.classes.DateViewState
import kotlinx.coroutines.launch
import kotlinx.datetime.Month

@Composable
internal fun CalendarSelectListYear(
    pageData: State<CalendarPageData>,
    setup: DialogDate.Setup,
    dateRange: DialogDate.Range,
    pagerState: PagerState,
    viewState: MutableState<DateViewState>
) {
    val scope = rememberCoroutineScope()
    CalendarSelectList(
        value = pageData.value.year,
        range = dateRange.years,
        itemFormatter = {
            setup.formatterYearSelectorList(it)
        }
    ) {
        val offsetPages = (it - dateRange.years.first) * 12 + pageData.value.month - 1
        scope.launch { pagerState.scrollToPage(offsetPages) }
        viewState.value = DateViewState.Calendar
    }
}

@Composable
internal fun CalendarSelectListMonth(
    pageData: State<CalendarPageData>,
    setup: DialogDate.Setup,
    pagerState: PagerState,
    viewState: MutableState<DateViewState>
) {
    val scope = rememberCoroutineScope()
    CalendarSelectList(
        value = pageData.value.month,
        range = (1..12),
        itemFormatter = {
            setup.formatterMonthSelectorList(Month(it))
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
    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    DialogContentScrollableLazyColumn(state = state) {
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