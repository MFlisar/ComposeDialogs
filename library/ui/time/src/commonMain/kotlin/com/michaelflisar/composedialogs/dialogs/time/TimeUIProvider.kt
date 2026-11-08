package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

enum class TimePickerInputType {
    Input,
    Dropdown
}

enum class TimePickerPeriod {
    AM,
    PM
}

/** UI rendering contract for the time picker's labels and controls. */
interface TimeUIProvider {

    /** Groups a control with its label; a null label reserves the label slot for alignment. */
    @Composable
    fun LabeledField(
        label: String?,
        modifier: Modifier,
        errorMessage: String?,
        content: @Composable () -> Unit
    )

    /** Renders the editable hour or minute field. */
    @Composable
    fun NumberInput(
        modifier: Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        title: String,
        onFocusChanged: (Boolean) -> Unit
    )

    /** Renders the hour or minute dropdown. */
    @Composable
    fun NumberDropdown(
        modifier: Modifier,
        title: String,
        items: List<Int>,
        selected: Int,
        selectedText: String,
        itemText: (Int) -> String,
        onItemSelected: (Int) -> Unit
    )

    /** Renders the compact vertical selector for AM and PM. */
    @Composable
    fun PeriodSelector(
        modifier: Modifier,
        selected: TimePickerPeriod,
        amText: String,
        pmText: String,
        onItemSelected: (TimePickerPeriod) -> Unit
    )
}
