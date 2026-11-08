package com.michaelflisar.composedialogs.dialogs.time

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.michaelflisar.composedialogs.core.internal.ui.InternalDialogLabeledField
import com.michaelflisar.composedialogs.core.internal.ui.InternalDialogNumberInput
import com.michaelflisar.composedialogs.core.internal.ui.InternalDialogTextDropdown
import com.michaelflisar.composedialogs.core.internal.ui.InternalDialogVerticalSegmentedButton

internal object Material3TimeUIProvider : TimeUIProvider {

    @Composable
    override fun LabeledField(
        label: String?,
        modifier: Modifier,
        errorMessage: String?,
        content: @Composable () -> Unit
    ) {
        InternalDialogLabeledField(
            label = label,
            modifier = modifier,
            errorMessage = errorMessage,
            labelTextAlign = TextAlign.Center,
            content = content
        )
    }

    @Composable
    override fun NumberInput(
        modifier: Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        title: String,
        onFocusChanged: (Boolean) -> Unit
    ) {
        InternalDialogNumberInput(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            title = title,
            onFocusChanged = onFocusChanged
        )
    }

    @Composable
    override fun NumberDropdown(
        modifier: Modifier,
        title: String,
        items: List<Int>,
        selected: Int,
        selectedText: String,
        itemText: (Int) -> String,
        onItemSelected: (Int) -> Unit
    ) {
        InternalDialogTextDropdown(
            modifier = modifier,
            title = title,
            items = items,
            selected = selected,
            selectedText = selectedText,
            itemText = itemText,
            onItemSelected = onItemSelected,
            selectedTextAlign = TextAlign.Center,
            selectedTextStyle = MaterialTheme.typography.bodyLarge,
            itemTextStyle = MaterialTheme.typography.bodyLarge
        )
    }

    @Composable
    override fun PeriodSelector(
        modifier: Modifier,
        selected: TimePickerPeriod,
        amText: String,
        pmText: String,
        onItemSelected: (TimePickerPeriod) -> Unit
    ) {
        InternalDialogVerticalSegmentedButton(
            modifier = modifier,
            items = listOf(TimePickerPeriod.AM, TimePickerPeriod.PM),
            selected = selected,
            itemText = { if (it == TimePickerPeriod.AM) amText else pmText },
            onItemSelected = onItemSelected
        )
    }
}
