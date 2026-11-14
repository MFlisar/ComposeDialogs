package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.isLandscape
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.dialogs.color.internal.Material3ColorUIProvider
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
 * @param texts the texts ([ColorPickerTexts]) that are used inside this dialog - use [ColorPickerDefaults.texts] to provide your own data
 * @param alphaSupported if true, the dialog supports color alpha values
 * @param directEditSupported if true, the custom color page allows direct editing of the color value
 * @param shape the shape of the color cells
 * @param gridSize the size of the color grid
 * @param labelStyle the [ColorPickerLabelStyle] for the color picker
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogColor(
    // Base Dialog - State
    state: BaseDialogState,
    // Custom - Required
    color: MutableState<Color>,
    // Custom - Optional
    texts: ColorPickerTexts = ColorPickerDefaults.texts(),
    alphaSupported: Boolean = true,
    directEditSupported: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    gridSize: Int = if (isLandscape()) 6 else 4,
    labelStyle: ColorPickerLabelStyle = ColorPickerLabelStyle.Value,
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
        ColorPicker(
            uiProvider = Material3ColorUIProvider,
            color = color,
            texts = texts,
            alphaSupported = alphaSupported,
            directEditSupported = directEditSupported,
            shape = shape,
            gridSize = gridSize,
            labelStyle = labelStyle,
            landscape = isLandscape()
        )
    }
}