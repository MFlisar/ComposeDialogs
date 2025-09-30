package com.michaelflisar.composedialogs.dialogs.frequency.composables

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

internal object OutlinedDecoratedContainer {
    // TODO: find out why extra padding is necessary + fix it
    val MODIFIER_CORRECTION = Modifier.padding(vertical = 1.5.dp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OutlinedDecoratedContainer(
    title: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            // OutlinedTextFieldTopPadding is internal but resolves to 8.sp
            //.padding(top = with(LocalDensity.current) { OutlinedTextFieldTopPadding.toDp() })
            .padding(top = with(LocalDensity.current) { 8.sp.toDp() })
            .defaultMinSize(
                //minWidth = OutlinedTextFieldDefaults.MinWidth,
                minHeight = OutlinedTextFieldDefaults.MinHeight
            )
    ) {
        OutlinedTextFieldDefaults.DecorationBox(
            value = title,
            visualTransformation = VisualTransformation.Companion.None,
            innerTextField = {
                content()
            },
            label = if (title.isNotEmpty()) {
                { Text(title) }
            } else null,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            singleLine = true,
            enabled = enabled,
            isError = false,
            interactionSource = interactionSource,
            colors = colors,
            container = {
                OutlinedTextFieldDefaults.Container(
                    enabled = enabled,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = colors,
                    //shape = shape,
                    modifier = Modifier.then(
                        if (enabled && onClick != null) {
                            Modifier
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = LocalIndication.current,
                                    enabled = enabled
                                ) { onClick() }
                        } else Modifier
                    )
                )
            }
        )
    }
}