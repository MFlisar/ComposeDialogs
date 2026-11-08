package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.michaelflisar.composedialogs.color.resources.Res
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_action_apply
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_action_cancel
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_label_edit
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_error_invalid
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_label_custom
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_label_presets
import org.jetbrains.compose.resources.stringResource

object DialogColorDefaults {

    /**
     * texts for the color pager
     *
     * @param presets the label of the pager title for the presets color page
     * @param custom the label of the pager title for the custom color page
     * @param editColor the title and accessibility label for direct color editing
     * @param invalidColor the validation message shown for an invalid color value
     * @param apply the label of the button that applies the edited color
     * @param cancel the label of the button that cancels direct color editing
     */
    @Composable
    fun texts(
        presets: String = stringResource(Res.string.composedialogs_color_label_presets),
        custom: String = stringResource(Res.string.composedialogs_color_label_custom),
        editColor: String = stringResource(Res.string.composedialogs_color_label_edit),
        invalidColor: String = stringResource(Res.string.composedialogs_color_error_invalid),
        apply: String = stringResource(Res.string.composedialogs_color_action_apply),
        cancel: String = stringResource(Res.string.composedialogs_color_action_cancel),
    ): DialogColor.Texts {
        return DialogColor.Texts(
            presets = presets,
            custom = custom,
            editColor = editColor,
            invalidColor = invalidColor,
            apply = apply,
            cancel = cancel
        )
    }
}