package com.michaelflisar.composedialogs.dialogs.color

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight

enum class ColorPickerIcon {
    Check,
    Edit
}

enum class ColorTextStyle {
    TitleSmall,
    BodySmall,
    BodyMedium
}

/** UI rendering contract for the color picker's controls and preview. */
interface ColorUIProvider {

    /** Renders channel labels, color values, and optional preset captions. */
    @Composable
    fun Text(
        text: String,
        modifier: Modifier,
        style: ColorTextStyle,
        color: Color?,
        fontWeight: FontWeight?,
    )

    /** Renders a color-channel or alpha slider. */
    @Composable
    fun Slider(
        modifier: Modifier,
        value: Float,
        onValueChange: (Float) -> Unit,
    )

    /** Renders the card containing the selected color's value preview. */
    @Composable
    fun Card(
        modifier: Modifier,
        content: @Composable () -> Unit,
    )

    /** Renders a text field for entering or editing a color value. */
    @Composable
    fun Input(
        modifier: Modifier,
        value: String,
        onValueChange: (String) -> Unit,
        isError: Boolean,
    )

    /** Renders the Presets/Custom page tabs and the color-preview edit action. */
    @Composable
    fun TabButton(
        modifier: Modifier,
        onClick: () -> Unit,
        content: @Composable RowScope.() -> Unit,
    )

    /** Renders the check mark on a selected swatch or the preview's edit icon. */
    @Composable
    fun Icon(
        icon: ColorPickerIcon,
        contentDescription: String?,
        tint: Color,
    )

    /** Returns the shape used for preset color swatches. */
    @Composable
    fun colorPresetShape(): Shape

    /**
     * Shows the color-value editor and validates its input before applying it.
     */
    @Composable
    fun InputDialog(
        visible: Boolean,
        title: String,
        value: MutableState<String>,
        isInputValid: (String) -> Boolean,
        invalidInputMessage: String,
        confirmText: String,
        dismissText: String,
        onConfirm: () -> Unit,
        onDismissRequest: () -> Unit,
    )
}
