package com.michaelflisar.composedialogs.dialogs.date.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.ui.DialogUI
import com.michaelflisar.composedialogs.dialogs.date.DialogDate
import com.michaelflisar.composedialogs.dialogs.date.classes.DateViewState
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
internal fun CalendarSelectionHeader(
    state: MutableState<LocalDate>,
    setup: DialogDate.Setup,
    pagerState: PagerState,
    currentPage: State<Int>,
    viewState: MutableState<DateViewState>
) {
    val scope = rememberCoroutineScope()
    //val interactionSource = remember { MutableInteractionSource() }
    Column(modifier = Modifier
        .background(
            DialogUI.provider.color(DialogUI.ColorRole.Primary),
            DialogUI.provider.smallShape()
        )
        .clickable(
            enabled = pagerState.currentPage != currentPage.value || viewState.value != DateViewState.Calendar,
            //interactionSource = interactionSource,
            //indication = ripple(color = MaterialTheme.colorScheme.onPrimary)
        ) {
            if (viewState.value != DateViewState.Calendar) {
                viewState.value = DateViewState.Calendar
            }
            scope.launch { pagerState.animateScrollToPage(currentPage.value) }
        }
        .padding(8.dp)
    ) {
        DialogUI.provider.Text(
            text = setup.formatterSelectedDate(state.value),
            role = DialogUI.TextRole.TitleMedium,
            modifier = Modifier,
            fontWeight = FontWeight.Bold,
            color = DialogUI.provider.color(DialogUI.ColorRole.OnPrimary),
            textAlign = null
        )
    }
}