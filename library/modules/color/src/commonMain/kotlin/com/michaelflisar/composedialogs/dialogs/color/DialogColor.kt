package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.isLandscape
import com.michaelflisar.composedialogs.core.BaseDialogState

// begin-snippet: DialogColor::constructor
/**
 * Shows a color dialog
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param color the selected color state
 * @param texts the texts ([DialogColor.Texts]) that are used inside this dialog - use [DialogColorDefaults.texts] to provide your own data
 * @param alphaSupported if true, the dialog supports color alpha values
 * @param directEditSupported if true, the dialog allows direct editing of rgba values (in the custom color page)
 * @param shape the shape of the color cells
 * @param gridSize the size of the color grid
 * @param labelStyle the [DialogColor.LabelStyle] for the color picker
 */
@Composable
fun DialogColor(
    // Base Dialog - State
    state: BaseDialogState,
    // Custom - Required
    color: MutableState<Color>,
    // Custom - Optional
    texts: DialogColor.Texts = DialogColorDefaults.texts(),
    alphaSupported: Boolean = true,
    directEditSupported: Boolean = true,
    shape: Shape = DialogColorDefaults.shape(),
    gridSize: Int = if (isLandscape()) 6 else 4,
    labelStyle: DialogColor.LabelStyle = DialogColor.LabelStyle.Value,
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
        DialogColorContent(color, texts, alphaSupported, directEditSupported, shape, gridSize, labelStyle)
    }
}
