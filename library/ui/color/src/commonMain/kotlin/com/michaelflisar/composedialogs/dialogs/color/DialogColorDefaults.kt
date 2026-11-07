package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composedialogs.core.ui.DialogUI
import com.michaelflisar.composedialogs.color.resources.Res
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_label_custom
import com.michaelflisar.composedialogs.color.resources.composedialogs_color_label_presets
import org.jetbrains.compose.resources.stringResource

object DialogColorDefaults {

    @Composable
    fun shape(): Shape = DialogUI.provider.smallShape()

    /**
     * texts for the color pager
     *
     * @param presets the label of the pager title for the presets color page
     * @param custom the label of the pager title for the custom color page
     */
    @Composable
    fun texts(
        presets: String = stringResource(Res.string.composedialogs_color_label_presets),
        custom: String = stringResource(Res.string.composedialogs_color_label_custom),
    ): DialogColor.Texts {
        return DialogColor.Texts(
            presets = presets,
            custom = custom
        )
    }
}