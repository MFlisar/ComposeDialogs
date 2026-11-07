package com.michaelflisar.composedialogs.dialogs.menu

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.michaelflisar.composedialogs.core.*

// begin-snippet: DialogMenu::constructor
/**
 * Shows a dialog holdinh a list of menu items
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param items a list of menu items
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogMenu(
    state: BaseDialogState,
    // Custom - Required
    items: List<MenuItem>,
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogButtons.DISABLED,
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {},
)
// end-snippet
{
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        DialogMenuContent(items, onDismiss = { state.dismiss() })
    }
}