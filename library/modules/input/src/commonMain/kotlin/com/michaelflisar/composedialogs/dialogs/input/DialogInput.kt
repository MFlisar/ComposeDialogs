package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.michaelflisar.composedialogs.core.BaseDialogState
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogOptions

// begin-snippet: DialogInput::constructor
/**
 * Shows a dialog with an input field
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param value the selected text
 * @param label the optional label of the input field
 * @param inputPlaceholder the placeholder the for the input field
 * @param singleLine if true, the input field will only allow a single line
 * @param maxLines the max lines for the input field
 * @param minLines the min lines for the input field
 * @param keyboardOptions the [KeyboardOptions] for the input field
 * @param enabled if true, the input field is enabled
 * @param clearable if true, the input field can be cleared by a trailing clear icon
 * @param prefix the prefix for the input field
 * @param suffix the prefix for the input field
 * @param textStyle the [TextStyle] for the input field
 * @param validator the [DialogInputValidator] for the input field - use [rememberDialogInputValidator]
 * @param requestFocus if true, the input field will request the focus when the dialog si shown (and open the keyboard)
 * @param selectionState if initial selection state ([DialogInput.SelectionState]) of the input field
 * @param onTextStateChanged an optional callback that will be called whenever the value of the input field changes
 */
@Composable
fun DialogInput(
    // Base Dialog - State
    state: BaseDialogState,
    // Custom - Required
    value: MutableState<String>,
    label: String = "",
    // Custom - Optional
    inputPlaceholder: String = "",
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    enabled: Boolean = true,
    clearable: Boolean = true,
    prefix: String = "",
    suffix: String = "",
    textStyle: TextStyle = DialogInputDefaults.textStyle(),
    validator: DialogInputValidator = rememberDialogInputValidator(),
    requestFocus: Boolean = false,
    selectionState: DialogInput.SelectionState = DialogInput.SelectionState.Default,
    onTextStateChanged: (valid: Boolean, text: String) -> Unit = { _, _ -> },
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: DialogOptions = DialogDefaults.options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
// end-snippet
{
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        DialogInputContent(
            value = value,
            label = label,
            inputPlaceholder = inputPlaceholder,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            keyboardOptions = keyboardOptions,
            enabled = enabled,
            clearable = clearable,
            prefix = prefix,
            suffix = suffix,
            textStyle = textStyle,
            validator = validator,
            requestFocus = requestFocus,
            selectionState = selectionState,
            onTextStateChanged = onTextStateChanged,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
