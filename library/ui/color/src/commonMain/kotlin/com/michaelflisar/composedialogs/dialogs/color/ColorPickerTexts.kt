package com.michaelflisar.composedialogs.dialogs.color

class ColorPickerTexts(
    val presets: String,
    val custom: String,
    val editColor: String = "Edit color",
    val invalidColor: String = "Invalid color value",
    val apply: String = "Apply",
    val cancel: String = "Cancel"
)

enum class ColorPickerLabelStyle {
    Value,
    Percent
}
