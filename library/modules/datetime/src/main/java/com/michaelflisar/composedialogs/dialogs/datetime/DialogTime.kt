package com.michaelflisar.composedialogs.dialogs.datetime

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogTime(
    state: DialogState,
    // Custom - Required
    time: TimePickerState,
    // Custom - Optional
    // ..
    // Base Dialog - Optional
    title: DialogTitle = DialogDefaults.titleSmall(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    //style: DialogStyle.BottomSheet = DialogDefaults.styleBottomSheet(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, onEvent) {
        TimePicker(
            state = time,
            modifier = Modifier.fillMaxWidth()
        )
    }
}