package com.michaelflisar.composedialogs.dialogs.datetime

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogDate(
    state: DialogState,
    // Custom - Required
    date: DatePickerState,
    // Custom - Optional
    dateFormatter: DatePickerFormatter = remember { DatePickerFormatter() },
    dateValidator: (Long) -> Boolean = { true },
    showModeToggle: Boolean = true,
    // Base Dialog - Optional
    title: DialogTitle = DialogDefaults.titleSmall(),
    icon: DialogIcon? = null,
    //style: DialogStyle = DialogDefaults.styleDialog(),
    style: DialogStyle.BottomSheet = DialogDefaults.styleBottomSheet(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, onEvent) {
        DatePicker(
            state = date,
            modifier = Modifier.fillMaxWidth(),
            title = null,
            dateFormatter = dateFormatter,
            dateValidator = dateValidator,
            showModeToggle = showModeToggle
        )
    }
}