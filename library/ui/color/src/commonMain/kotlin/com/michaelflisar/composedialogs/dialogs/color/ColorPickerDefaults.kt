package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.color.resources.Res
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_action_apply
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_action_cancel
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_error_invalid
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_label_custom
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_label_edit
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_label_presets
import org.jetbrains.compose.resources.stringResource

@Immutable
class ColorPickerConfig internal constructor(
    val alphaSupported: Boolean,
    val directEditSupported: Boolean,
    val shape: Shape,
    val gridSize: Int,
    val labelStyle: ColorPickerLabelStyle
)

@Immutable
class ColorPickerTexts internal constructor(
    val presets: String,
    val custom: String,
    val editColor: String,
    val invalidColor: String,
    val apply: String,
    val cancel: String
)

@Stable
object ColorPickerDefaults {
    @Composable
    fun config(
        alphaSupported: Boolean = true,
        directEditSupported: Boolean = true,
        shape: Shape = RoundedCornerShape(4.dp),
        gridSize: Int = 4,
        labelStyle: ColorPickerLabelStyle = ColorPickerLabelStyle.Value
    ) = ColorPickerConfig(
        alphaSupported = alphaSupported,
        directEditSupported = directEditSupported,
        shape = shape,
        gridSize = gridSize,
        labelStyle = labelStyle
    )

    @Composable
    fun texts(
        presets: String = stringResource(Res.string.composedialogs_color_label_presets),
        custom: String = stringResource(Res.string.composedialogs_color_label_custom),
        editColor: String = stringResource(Res.string.composedialogs_color_label_edit),
        invalidColor: String = stringResource(Res.string.composedialogs_color_error_invalid),
        apply: String = stringResource(Res.string.composedialogs_color_action_apply),
        cancel: String = stringResource(Res.string.composedialogs_color_action_cancel)
    ) = ColorPickerTexts(
        presets = presets,
        custom = custom,
        editColor = editColor,
        invalidColor = invalidColor,
        apply = apply,
        cancel = cancel
    )
}
