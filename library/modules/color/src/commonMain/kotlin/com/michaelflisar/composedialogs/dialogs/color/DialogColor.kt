package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.michaelflisar.composedialogs.dialogs.color.classes.ColorStateSaver
import com.michaelflisar.composedialogs.dialogs.color.classes.ColorStateSaverNullable
import com.michaelflisar.composedialogs.dialogs.color.classes.ColorDefinitions
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
 * @param directEditSupported if true, the custom color page allows direct editing of the color value
 * @param shape the shape of the color cells
 * @param gridSize the size of the color grid
 * @param labelStyle the [DialogColor.LabelStyle] for the color picker
 */
@OptIn(ExperimentalComposeUiApi::class)
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
    shape: Shape = MaterialTheme.shapes.small,
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
    // saved dialog state
    val selectedSubColor =
        rememberSaveable(saver = ColorStateSaverNullable) { mutableStateOf(null) }
    val selectedPresetsLevel = rememberSaveable { mutableIntStateOf(0) }
    val selectedMainColor = rememberSaveable {
        mutableStateOf(
            DialogColorUtil.getNearestColorGroup(color.value)
        )
    }
        val selectedAlpha = rememberSaveable { mutableFloatStateOf(color.value.alpha) }
        val colorState = rememberSaveable { mutableStateOf(DialogColor.Page.Presets) }
        val updateSelectedPresetColors = {
            selectedMainColor.value = DialogColorUtil.getNearestColorGroup(color.value)
            selectedSubColor.value = null
            selectedPresetsLevel.value = 0
        }

        Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
            ColorPicker(
                uiProvider = Material3ColorUIProvider,
                color = color,
                texts = ColorPickerTexts(
                    presets = texts.presets,
                    custom = texts.custom,
                    editColor = texts.editColor,
                    invalidColor = texts.invalidColor,
                    apply = texts.apply,
                    cancel = texts.cancel
                ),
                alphaSupported = alphaSupported,
                directEditSupported = directEditSupported,
                shape = shape,
                gridSize = gridSize,
                labelStyle = when (labelStyle) {
                    DialogColor.LabelStyle.Value -> ColorPickerLabelStyle.Value
                    DialogColor.LabelStyle.Percent -> ColorPickerLabelStyle.Percent
                },
                landscape = isLandscape(),
                customPage = colorState.value == DialogColor.Page.Custom,
                selectedSubColor = selectedSubColor,
                selectedPresetsLevel = selectedPresetsLevel,
                selectedAlpha = selectedAlpha,
                mainColors = ColorDefinitions.COLORS.map { it.getColor() },
                mainLabels = null,
                subColors = selectedMainColor.value.colors.map { it.getColor() },
                subLabels = selectedMainColor.value.colors.map { it.label },
                onMainColorSelected = {
                    selectedMainColor.value = ColorDefinitions.COLORS[it]
                    selectedPresetsLevel.value = 1
                },
                onSubColorSelected = {
                    selectedSubColor.value = selectedMainColor.value.colors[it].getColor()
                    color.value = selectedSubColor.value!!.copy(selectedAlpha.value)
                },
                onPresetsClick = {
                    colorState.value = DialogColor.Page.Presets
                    selectedSubColor.value = null
                    selectedPresetsLevel.value = 0
                },
                onCustomClick = { colorState.value = DialogColor.Page.Custom },
                updateSelectedPresetColors = updateSelectedPresetColors
            )
        }
}

@Stable
object DialogColor {

    @Immutable
    internal enum class Page {
        Custom,
        Presets
    }

    /**
     * enum to define how rgba values of a color are displayed (percentages or a value in the range of [0, 255])
     */
    @Immutable
    enum class LabelStyle {
        Value,
        Percent
    }

    /**
     * see [DialogColorDefaults.texts]
     */
    @Immutable
    class Texts internal constructor(
        val presets: String,
        val custom: String,
        val editColor: String,
        val invalidColor: String,
        val apply: String,
        val cancel: String
    )

}

/**
 * convenient function for [DialogColor]
 *
 * @param color the color state of the dialog
 *
 * @return a state holding the current color value
 */
@Composable
fun rememberDialogColor(
    color: Color
): MutableState<Color> {
    return rememberSaveable(saver = ColorStateSaver) { mutableStateOf(color) }
}