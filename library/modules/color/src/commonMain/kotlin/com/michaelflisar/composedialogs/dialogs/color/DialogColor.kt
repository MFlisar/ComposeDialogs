package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.BackHandler
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.isLandscape
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.dialogs.color.DialogColorDefaults.texts
import com.michaelflisar.composedialogs.dialogs.color.classes.ColorStateSaver
import com.michaelflisar.composedialogs.dialogs.color.classes.ColorStateSaverNullable
import com.michaelflisar.composedialogs.dialogs.color.composables.Content
import com.michaelflisar.composedialogs.dialogs.color.composables.TitleForPages
import composedialogs.composedialogs.modules.color.generated.resources.Res
import composedialogs.composedialogs.modules.color.generated.resources.composedialogs_color_label_custom
import composedialogs.composedialogs.modules.color.generated.resources.composedialogs_color_label_presets
import org.jetbrains.compose.resources.stringResource

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
 * @param shape the shape of the color cells
 * @param gridSize the size of the color grid
 * @param labelStyle the [DialogColor.LabelStyle] for the color picker
 */
@Composable
fun DialogColor(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    color: MutableState<Color>,
    // Custom - Optional
    texts: DialogColor.Texts = DialogColorDefaults.texts(),
    alphaSupported: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    gridSize: Int = if (isLandscape()) 6 else 4,
    labelStyle: DialogColor.LabelStyle = DialogColor.LabelStyle.Value,
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
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

    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {

        val landscape = isLandscape()

        BackHandler(enabled = selectedPresetsLevel.intValue != 0, onBack = {
            selectedPresetsLevel.intValue = 0
        })

        Column {

            if (landscape) {
                Row {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TitleForPages(
                            Modifier.weight(1f),
                            texts,
                            colorState,
                            selectedSubColor,
                            selectedPresetsLevel
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Content(
                            color,
                            colorState,
                            selectedMainColor,
                            selectedSubColor,
                            selectedPresetsLevel,
                            selectedAlpha,
                            alphaSupported,
                            shape,
                            gridSize,
                            labelStyle
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TitleForPages(
                        Modifier.weight(1f),
                        texts,
                        colorState,
                        selectedSubColor,
                        selectedPresetsLevel
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Content(
                    color,
                    colorState,
                    selectedMainColor,
                    selectedSubColor,
                    selectedPresetsLevel,
                    selectedAlpha,
                    alphaSupported,
                    shape,
                    gridSize,
                    labelStyle
                )
            }
        }
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
        val custom: String
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

@Stable
object DialogColorDefaults {

    /**
     * texts for the color pager
     *
     * @param presets the label of the pager title for the presets color page
     * @param custom the label of the pager title for the custom color page
     */
    @Composable
    fun DialogColorDefaults.texts(
        presets: String = stringResource(Res.string.composedialogs_color_label_presets),
        custom: String = stringResource(Res.string.composedialogs_color_label_custom),
    ): DialogColor.Texts {
        return DialogColor.Texts(
            presets = presets,
            custom = custom
        )
    }
}