package com.michaelflisar.composedialogs.dialogs.input

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogStyle
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.dialogs.input.composables.PickerIcon
import com.michaelflisar.composedialogs.dialogs.input.utils.NumberUtil

/**
 * Shows a dialog with a number picker
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param value the number state for this dialog
 * @param setup the [NumberPickerSetup]
 * @param iconDown the icon for the decrease button
 * @param iconUp the icon for the increase button
 * @param formatter the formatter for the text of this picker
 * @param textStyle the [TextStyle] for the text of this picker
 * @param onValueStateChanged a callback that will be called whenever the value of the number picker changes
 */
@Composable
fun <T : Number> DialogNumberPicker(
    // Base Dialog - State
    state: DialogState,
    // Custom - Required
    value: MutableState<T>,
    setup: NumberPickerSetup<T>,
    iconDown: @Composable () -> Unit = {
        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
    },
    iconUp: @Composable () -> Unit = {
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
    },
    formatter: (value: T) -> String = { it.toString() },
    // Custom - Optional
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onValueStateChanged: (value: T) -> Unit = { },
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {

        val modifier = when (style) {
            is DialogStyle.BottomSheet -> Modifier.fillMaxWidth()
            is DialogStyle.Dialog -> Modifier
        }

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PickerIcon(iconDown, setup.canDecrease(value.value), setup) {
                value.value = setup.decrease(value.value)
                onValueStateChanged(value.value)
            }
            Text(
                text = formatter(value.value),
                style = textStyle,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            PickerIcon(iconUp, setup.canIncrease(value.value), setup) {
                value.value = setup.increase(value.value)
                onValueStateChanged(value.value)
            }
        }
    }
}

/**
 * setup class for a number picker dialog
 *
 * @param min the minimum value for this picker
 * @param max the maximum value for this picker
 * @param stepSize the step size for the decrease and increase buttons of this picker
 * @param repeatingButton the [RepeatingButton] behavior (can be [RepeatingButton.Disabled] or [RepeatingButton.Enabled])
 */
class NumberPickerSetup<T : Number>(
    val min: T,
    val max: T,
    val stepSize: T,
    val repeatingButton: RepeatingButton = RepeatingButton.Disabled
) {
    internal fun canIncrease(value: T) = NumberUtil.isLess(value, max)
    internal fun canDecrease(value: T) = NumberUtil.isMore(value, min)

    internal fun increase(value: T): T {
        val next = NumberUtil.sum(value, stepSize)
        return if (NumberUtil.isMore(next, max))
            max
        else next
    }

    internal fun decrease(value: T): T {
        val next = NumberUtil.min(value, stepSize)
        return if (NumberUtil.isLess(next, min))
            min
        else next
    }
}

/**
 * repeating button settings class
 */
sealed class RepeatingButton {

    /**
     *  repeating button clicks on long press are disabled
     */
    data object Disabled : RepeatingButton()

    /**
     *  repeating button clicks on long press are enabled
     *
     *  @param maxDelayMillis the maximum delay between two consecutive button clicks emulated by a long press
     *  @param minDelayMillis the minimum delay between two consecutive button clicks emulated by a long press
     *  @param delayDecayFactor the increase factor of the initial dalay (=minDelayMillis) to the maximum delay (=maxDelayMillis) after each emulated button press
     */
    class Enabled(
        val maxDelayMillis: Long = 500,
        val minDelayMillis: Long = 5,
        val delayDecayFactor: Float = .15f
    ) : RepeatingButton()
}

/**
 * convenient function for [DialogNumberPicker]
 *
 * @param initialValue the initial number
 *
 * @return a state holding the current number value
 */
@Composable
fun <T : Number> rememberDialogNumber(
    initialValue: T
): MutableState<T> {
    return rememberSaveable {
        mutableStateOf(initialValue)
    }
}