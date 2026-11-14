package com.michaelflisar.composedialogs.dialogs.date.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.dialogs.date.DatePickerButtonType
import com.michaelflisar.composedialogs.dialogs.date.DateUIProvider

@Composable
internal fun CalendarTodayButton(
    buttonToday: @Composable () -> Unit,
    enabled: Boolean,
    onClick: () -> Unit,
    uiProvider: DateUIProvider
) {
    uiProvider.Button(
        type = DatePickerButtonType.Today,
        modifier = Modifier,
        enabled = enabled,
        onClick = onClick
    ) {
        buttonToday()
    }
}