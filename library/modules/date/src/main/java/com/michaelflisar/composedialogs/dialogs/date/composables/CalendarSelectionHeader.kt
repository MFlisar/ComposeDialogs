package com.michaelflisar.composedialogs.dialogs.date.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.date.DialogDate
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CalendarSelectionHeader(state: MutableState<LocalDate>, setup: DialogDate.Setup, pagerState: PagerState, currentPage: State<Int>) {
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    Column(modifier = Modifier
        .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
        .clickable(enabled =  pagerState.currentPage != currentPage.value, interactionSource = interactionSource, indication = rememberRipple(color = MaterialTheme.colorScheme.onPrimary)) {
            scope.launch { pagerState.animateScrollToPage(currentPage.value) }
        }
        .padding(8.dp)
    ) {
        Text(
            text = setup.formatterSelectedDate(state.value),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}