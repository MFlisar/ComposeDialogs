package com.michaelflisar.composedialogs.dialogs.number

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.Modifier
import com.michaelflisar.composedialogs.core.ui.DialogUI
import com.michaelflisar.composedialogs.dialogs.number.utils.NumberUtil

class NumberPickerSetup<T : Number>(
    val min: T,
    val max: T,
    val stepSize: T,
    val stepSize2: T = stepSize,
    val repeatingButton: RepeatingButton = RepeatingButton.Disabled
) {
    internal fun canIncrease(value: T) = NumberUtil.isLess(value, max)
    internal fun canDecrease(value: T) = NumberUtil.isMore(value, min)

    internal fun increase(value: T): T {
        val next = NumberUtil.sum(value, stepSize)
        return if (NumberUtil.isMore(next, max)) max else next
    }

    internal fun decrease(value: T): T {
        val next = NumberUtil.min(value, stepSize)
        return if (NumberUtil.isLess(next, min)) min else next
    }

    internal fun increase2(value: T): T {
        val next = NumberUtil.sum(value, stepSize2)
        return if (NumberUtil.isMore(next, max)) max else next
    }

    internal fun decrease2(value: T): T {
        val next = NumberUtil.min(value, stepSize2)
        return if (NumberUtil.isLess(next, min)) min else next
    }
}

sealed class RepeatingButton {
    data object Disabled : RepeatingButton()

    class Enabled(
        val maxDelayMillis: Long = 500,
        val minDelayMillis: Long = 5,
        val delayDecayFactor: Float = .15f
    ) : RepeatingButton()
}

@Composable
fun <T : Number> rememberDialogNumber(number: T): MutableState<T> =
    rememberSaveable { mutableStateOf(number) }

object DialogNumberPickerDefaults {
    @Composable
    fun IconDown() {
        DialogUI.provider.Icon(
            icon = DialogUI.IconType.ChevronLeft,
            contentDescription = null,
            modifier = Modifier,
            tint = DialogUI.provider.color(DialogUI.ColorRole.OnSurface)
        )
    }

    @Composable
    fun IconUp() {
        DialogUI.provider.Icon(
            icon = DialogUI.IconType.ChevronRight,
            contentDescription = null,
            modifier = Modifier,
            tint = DialogUI.provider.color(DialogUI.ColorRole.OnSurface)
        )
    }

    @Composable
    fun IconDown2() {
        DialogUI.provider.Icon(
            icon = DialogUI.IconType.DoubleChevronLeft,
            contentDescription = null,
            modifier = Modifier,
            tint = DialogUI.provider.color(DialogUI.ColorRole.OnSurface)
        )
    }

    @Composable
    fun IconUp2() {
        DialogUI.provider.Icon(
            icon = DialogUI.IconType.DoubleChevronRight,
            contentDescription = null,
            modifier = Modifier,
            tint = DialogUI.provider.color(DialogUI.ColorRole.OnSurface)
        )
    }

    @Composable
    fun textStyle(): TextStyle = DialogUI.provider.textStyle(DialogUI.TextRole.Body)
}
