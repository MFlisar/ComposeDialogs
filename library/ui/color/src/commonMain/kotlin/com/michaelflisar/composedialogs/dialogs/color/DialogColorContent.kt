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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.dialogs.color.classes.ColorStateSaverNullable
import com.michaelflisar.composedialogs.dialogs.color.composables.Content
import com.michaelflisar.composedialogs.dialogs.color.composables.TitleForPages

/**
 * Displays the color picker without creating a dialog.
 *
 * All selection and navigation state is managed by this composable.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogColorContent(
    color: MutableState<Color>,
    texts: DialogColor.Texts = DialogColorDefaults.texts(),
    alphaSupported: Boolean = true,
    directEditSupported: Boolean = true,
    shape: Shape = DialogColorDefaults.shape(),
    gridSize: Int = if (LocalWindowInfo.current.containerSize.width > LocalWindowInfo.current.containerSize.height) 6 else 4,
    labelStyle: DialogColor.LabelStyle = DialogColor.LabelStyle.Value
) {
    val selectedSubColor =
        rememberSaveable(saver = ColorStateSaverNullable) { mutableStateOf(null) }
    val selectedPresetsLevel = rememberSaveable { mutableIntStateOf(0) }
    val selectedMainColor = rememberSaveable {
        mutableStateOf(DialogColorUtil.getNearestColorGroup(color.value))
    }
    val selectedAlpha = rememberSaveable { mutableFloatStateOf(color.value.alpha) }
    val colorState = rememberSaveable { mutableStateOf(DialogColor.Page.Presets) }
    val windowSize = LocalWindowInfo.current.containerSize
    val landscape = windowSize.width > windowSize.height

    BackHandler(enabled = selectedPresetsLevel.intValue != 0, onBack = {
        selectedPresetsLevel.intValue = 0
    })

    Column {
        if (landscape) {
            Row {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                        directEditSupported,
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
                directEditSupported,
                shape,
                gridSize,
                labelStyle
            )
        }
    }
}
