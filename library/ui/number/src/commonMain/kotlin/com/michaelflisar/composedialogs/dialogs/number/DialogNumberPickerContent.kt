package com.michaelflisar.composedialogs.dialogs.number

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.michaelflisar.composedialogs.core.ui.ComposeDialogsUI
import com.michaelflisar.composedialogs.dialogs.number.composables.PickerIcon

/**
 * Displays the number picker and updates [value] when a picker button is activated.
 */
@Composable
fun <T : Number> DialogNumberPickerContent(
    value: MutableState<T>,
    setup: NumberPickerSetup<T>,
    iconDown: @Composable () -> Unit = {
        ComposeDialogsUI.provider.Icon(
            ComposeDialogsUI.IconType.ChevronLeft,
            null,
            Modifier,
            ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.OnSurface)
        )
    },
    iconUp: @Composable () -> Unit = {
        ComposeDialogsUI.provider.Icon(
            ComposeDialogsUI.IconType.ChevronRight,
            null,
            Modifier,
            ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.OnSurface)
        )
    },
    iconDown2: @Composable () -> Unit = {
        ComposeDialogsUI.provider.Icon(
            ComposeDialogsUI.IconType.DoubleChevronLeft,
            null,
            Modifier,
            ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.OnSurface)
        )
    },
    iconUp2: @Composable () -> Unit = {
        ComposeDialogsUI.provider.Icon(
            ComposeDialogsUI.IconType.DoubleChevronRight,
            null,
            Modifier,
            ComposeDialogsUI.provider.color(ComposeDialogsUI.ColorRole.OnSurface)
        )
    },
    formatter: @Composable (value: T) -> String = { it.toString() },
    textStyle: TextStyle = ComposeDialogsUI.provider.textStyle(ComposeDialogsUI.TextRole.Body),
    onValueStateChanged: (value: T) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (setup.stepSize2 != setup.stepSize) {
            PickerIcon(iconDown2, setup.canDecrease(value.value), setup) {
                value.value = setup.decrease2(value.value)
                onValueStateChanged(value.value)
            }
        }
        PickerIcon(iconDown, setup.canDecrease(value.value), setup) {
            value.value = setup.decrease(value.value)
            onValueStateChanged(value.value)
        }
        androidx.compose.foundation.text.BasicText(
            text = formatter(value.value),
            style = textStyle.copy(textAlign = TextAlign.Center),
            modifier = Modifier.weight(1f),
        )
        PickerIcon(iconUp, setup.canIncrease(value.value), setup) {
            value.value = setup.increase(value.value)
            onValueStateChanged(value.value)
        }
        if (setup.stepSize2 != setup.stepSize) {
            PickerIcon(iconUp2, setup.canIncrease(value.value), setup) {
                value.value = setup.increase2(value.value)
                onValueStateChanged(value.value)
            }
        }
    }
}
