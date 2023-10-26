package com.michaelflisar.composedialogs.dialogs.date.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.DialogDate
import com.michaelflisar.composedialogs.dialogs.date.classes.CalendarPageData
import com.michaelflisar.composedialogs.dialogs.date.classes.DateViewState
import com.michaelflisar.composedialogs.dialogs.date.utils.DateUtil
import kotlinx.coroutines.launch
import java.time.Month
import java.time.Year

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CalendarHeader(
    pagerState: PagerState,
    range: DialogDate.Range,
    setup: DialogDate.Setup,
    pageData: State<CalendarPageData>,
    viewState: MutableState<DateViewState>
) {
    val scope = rememberCoroutineScope()
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderItem(
            modifier = Modifier.weight(1f),
            text = setup.formatterSelectedMonth(Month.of(pageData.value.month)),
            showButtons = setup.showNextPreviousMonthButtons,
            onShowMenu = { viewState.value = DateViewState.SelectMonth },
            onDownClick = if (pagerState.currentPage > 0) {
                { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } }
            } else null,
            onUpClick = if (pagerState.currentPage < pagerState.pageCount - 1) {
                { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } }
            } else null
        )
        //Spacer(modifier = Modifier.width(8.dp))
        HeaderItem(
            modifier = Modifier.weight(1f),
            text = setup.formatterSelectedYear(Year.of(pageData.value.year)),
            showButtons = setup.showNextPreviousYearButtons,
            onShowMenu = { viewState.value = DateViewState.SelectYear },
            onDownClick = if (pageData.value.year > range.years.first) {
                { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 12) } }
            } else null,
            onUpClick = if (pageData.value.year < range.years.last) {
                { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 12) } }
            } else null
        )
    }
}

@Composable
internal fun HeaderItem(
    modifier: Modifier = Modifier,
    text: String,
    showButtons: Boolean,
    onShowMenu: () -> Unit,
    onDownClick: (() -> Unit)?,
    onUpClick: (() -> Unit)?
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showButtons) {
            IconButton(
                modifier = Modifier.size(36.dp),
                onClick = { onDownClick?.invoke() },
                enabled = onDownClick != null
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, null)
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clip(MaterialTheme.shapes.small)
                .clickable {
                    onShowMenu()
                }
                .padding(4.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Icon(Icons.Default.ArrowDropDown, null)
        }
        if (showButtons) {
            IconButton(
                modifier = Modifier.size(36.dp),
                onClick = { onUpClick?.invoke() },
                enabled = onUpClick != null
            ) {
                Icon(Icons.Default.KeyboardArrowRight, null)
            }
        }
    }
}