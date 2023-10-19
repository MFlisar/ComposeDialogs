package com.michaelflisar.composedialogs.dialogs.color.composables

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composedialogs.dialogs.color.DialogColor

@Composable
internal fun TitleForPages(
    modifier: Modifier,
    texts: DialogColor.Texts,
    colorState: MutableState<DialogColor.Page>,
    selectedSubColor: MutableState<Color?>,
    selectedPresetsLevel: MutableState<Int>
) {
    TextButton(
        modifier = modifier
            .alpha(if (colorState.value == DialogColor.Page.Custom) .5f else 1f),
        onClick = {
            colorState.value = DialogColor.Page.Presets
            selectedSubColor.value = null
            selectedPresetsLevel.value = 0
        }
    ) {
        Text(text = texts.presets)
    }
    TextButton(
        modifier = modifier
            .alpha(if (colorState.value == DialogColor.Page.Presets) .5f else 1f),
        onClick = { colorState.value = DialogColor.Page.Custom }
    ) {
        Text(text = texts.custom)
    }
}