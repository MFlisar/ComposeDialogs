package com.michaelflisar.composedialogs.core.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign

object ComposeDialogsUI {

    interface Provider {
        @Composable
        fun Text(
            text: String,
            role: TextRole,
            modifier: Modifier
        )

        @Composable
        fun Text(
            text: String,
            role: TextRole,
            modifier: Modifier,
            color: Color?,
            textAlign: TextAlign?,
            fontWeight: FontWeight?
        )

        @Composable
        fun textStyle(role: TextRole): TextStyle

        @Composable
        fun color(role: ColorRole): Color

        @Composable
        fun ProvideContentColor(color: Color, content: @Composable () -> Unit)

        @Composable
        fun ProvideTextStyle(style: TextStyle, content: @Composable () -> Unit)

        @Composable
        fun TextButton(
            onClick: () -> Unit,
            modifier: Modifier,
            content: @Composable () -> Unit
        )

        @Composable
        fun OutlinedButton(
            onClick: () -> Unit,
            enabled: Boolean,
            modifier: Modifier,
            content: @Composable () -> Unit
        )

        @Composable
        fun IconButton(
            onClick: () -> Unit,
            enabled: Boolean,
            modifier: Modifier,
            content: @Composable () -> Unit
        )

        @Composable
        fun Card(
            modifier: Modifier,
            content: @Composable () -> Unit
        )

        @Composable
        fun OutlinedTextField(
            value: String,
            onValueChange: (String) -> Unit,
            modifier: Modifier,
            isError: Boolean
        )

        @Composable
        fun OutlinedTextField(
            value: String,
            onValueChange: (String) -> Unit,
            modifier: Modifier,
            enabled: Boolean,
            readOnly: Boolean,
            isError: Boolean,
            singleLine: Boolean,
            keyboardOptions: KeyboardOptions,
            label: (@Composable () -> Unit)?,
            placeholder: (@Composable () -> Unit)?,
            leadingIcon: (@Composable () -> Unit)?,
            trailingIcon: (@Composable () -> Unit)?,
            supportingText: (@Composable () -> Unit)?
        )

        @Composable
        fun OutlinedTextField(
            value: TextFieldValue,
            onValueChange: (TextFieldValue) -> Unit,
            modifier: Modifier,
            enabled: Boolean,
            isError: Boolean,
            singleLine: Boolean,
            minLines: Int,
            maxLines: Int,
            keyboardOptions: KeyboardOptions,
            textStyle: TextStyle,
            label: (@Composable () -> Unit)?,
            placeholder: (@Composable () -> Unit)?,
            prefix: (@Composable () -> Unit)?,
            suffix: (@Composable () -> Unit)?,
            trailingIcon: (@Composable () -> Unit)?,
            supportingText: (@Composable () -> Unit)?
        )

        @Composable
        fun OutlinedContainer(
            title: String,
            modifier: Modifier,
            enabled: Boolean,
            onClick: (() -> Unit)?,
            leadingIcon: (@Composable () -> Unit)?,
            placeholder: (@Composable () -> Unit)?,
            trailingIcon: (@Composable () -> Unit)?,
            content: @Composable () -> Unit
        )

        @Composable
        fun Slider(
            value: Float,
            onValueChange: (Float) -> Unit,
            modifier: Modifier
        )

        @Composable
        fun Icon(
            icon: IconType,
            contentDescription: String?,
            modifier: Modifier,
            tint: Color?
        )

        @Composable
        fun HorizontalDivider(modifier: Modifier)

        @Composable
        fun VerticalDivider(modifier: Modifier)

        @Composable
        fun Checkbox(checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?)

        @Composable
        fun RadioButton(selected: Boolean, onClick: (() -> Unit)?)

        @Composable
        fun DropdownMenu(
            expanded: Boolean,
            onDismissRequest: () -> Unit,
            content: @Composable ColumnScope.() -> Unit
        )

        @Composable
        fun DropdownMenuItem(
            modifier: Modifier,
            selected: Boolean,
            text: @Composable () -> Unit,
            onClick: () -> Unit
        )

        @Composable
        fun TimeInput(
            hour: Int,
            minute: Int,
            is24Hour: Boolean,
            modifier: Modifier,
            onTimeChange: (hour: Int, minute: Int) -> Unit
        )

        @Composable
        fun Dialog(
            onDismissRequest: () -> Unit,
            content: @Composable () -> Unit
        )

        @Composable
        fun smallShape(): Shape

        @Composable
        fun ProgressIndicator(
            type: ProgressIndicatorType,
            progress: Float?,
            modifier: Modifier
        )
    }

    enum class TextRole {
        Label,
        Body,
        BodyLarge,
        BodySmall,
        Button,
        TitleSmall,
        TitleMedium,
        HeadlineSmall
    }

    enum class ColorRole {
        Primary,
        OnPrimary,
        OnSurface,
        OnSurfaceVariant,
        Outline,
        Error
    }

    enum class ProgressIndicatorType {
        Circular,
        Linear
    }

    enum class IconType {
        Clear,
        Check,
        ArrowDropDown,
        ArrowBack,
        ArrowForward,
        ChevronLeft,
        ChevronRight,
        DoubleChevronLeft,
        DoubleChevronRight
    }

    private var instance: Provider? = null

    val provider: Provider
        get() = instance ?: ComposeDialogsUIProvider.provider

    /**
     * Installs a custom UI provider instead of the provider selected by the implementation module.
     */
    fun set(provider: Provider) {
        instance = provider
    }

    /**
     * Installs a default provider only when no provider has been set by the caller.
     */
    fun setDefault(provider: Provider) {
        if (instance == null) {
            instance = provider
        }
    }
}
