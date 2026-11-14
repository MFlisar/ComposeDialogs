package com.michaelflisar.composedialogs.dialogs.date.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import kotlinx.datetime.LocalDate
import com.michaelflisar.composedialogs.dialogs.date.DatePickerConfig
import com.michaelflisar.composedialogs.dialogs.date.DatePickerButtonType
import com.michaelflisar.composedialogs.dialogs.date.DateTextStyle
import com.michaelflisar.composedialogs.dialogs.date.DateUIProvider

@Composable
internal fun CalendarSelectionHeader(
    state: MutableState<LocalDate>,
    config: DatePickerConfig,
    enabled: Boolean,
    onClick: () -> Unit,
    uiProvider: DateUIProvider
) {
    uiProvider.Button(
        type = DatePickerButtonType.SelectedDate,
        modifier = Modifier,
        enabled = enabled,
        onClick = onClick,
    ) {
        uiProvider.Text(
            text = config.formatterSelectedDate(state.value),
            modifier = Modifier,
            style = DateTextStyle.TitleMedium,
            color = null,
            textAlign = null,
            maxLines = Int.MAX_VALUE
        )
    }
}