package com.michaelflisar.composedialogs.dialogs.color.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI
import com.michaelflisar.composedialogs.dialogs.color.DialogColor

@Composable
internal fun TitleForPages(
    modifier: Modifier,
    texts: DialogColor.Texts,
    colorState: MutableState<DialogColor.Page>,
    selectedSubColor: MutableState<Color?>,
    selectedPresetsLevel: MutableState<Int>
) {
    ComposeDialogsUI.provider.TextButton(
        modifier = modifier
            .alpha(if (colorState.value == DialogColor.Page.Custom) .5f else 1f),
        onClick = {
            colorState.value = DialogColor.Page.Presets
            selectedSubColor.value = null
            selectedPresetsLevel.value = 0
        }
    ) {
        ComposeDialogsUI.provider.Text(
            text = texts.presets,
            role = ComposeDialogsUI.TextRole.Button,
            modifier = Modifier
        )
    }
    ComposeDialogsUI.provider.TextButton(
        modifier = modifier
            .alpha(if (colorState.value == DialogColor.Page.Presets) .5f else 1f),
        onClick = { colorState.value = DialogColor.Page.Custom }
    ) {
        ComposeDialogsUI.provider.Text(
            text = texts.custom,
            role = ComposeDialogsUI.TextRole.Button,
            modifier = Modifier
        )
    }
}