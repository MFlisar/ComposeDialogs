package com.michaelflisar.composedialogs.dialogs.number.composables

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import com.michaelflisar.composedialogs.core.DispatcherIO
import com.michaelflisar.composedialogs.dialogs.number.NumberPickerSetup
import com.michaelflisar.composedialogs.dialogs.number.RepeatingButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun PickerIcon(
    icon: @Composable () -> Unit,
    enabled: Boolean,
    setup: NumberPickerSetup<*>,
    onClick: () -> Unit
) {
    var modifier: Modifier = Modifier
    val repeatingButton = setup.repeatingButton
    var onClickForButton = onClick
    if (repeatingButton is RepeatingButton.Enabled) {
        modifier = Modifier.repeatingClickable(
            interactionSource = remember { MutableInteractionSource() },
            enabled = enabled,
            maxDelayMillis = repeatingButton.maxDelayMillis,
            minDelayMillis = repeatingButton.minDelayMillis,
            delayDecayFactor = repeatingButton.delayDecayFactor
        ) { onClick() }
        onClickForButton = {}
    }

    IconButton(
        onClick = onClickForButton,
        enabled = enabled,
        modifier = modifier
    ) {
        icon()
    }
}

/*
 * simply copied from here:
 * https://stackoverflow.com/a/76190587/1439522
 */
private fun Modifier.repeatingClickable(
    interactionSource: InteractionSource,
    enabled: Boolean,
    maxDelayMillis: Long,
    minDelayMillis: Long,
    delayDecayFactor: Float,
    onClick: () -> Unit
): Modifier = composed {

    val currentClickListener by rememberUpdatedState(onClick)
    val isEnabled by rememberUpdatedState(enabled)

    pointerInput(interactionSource, isEnabled) {
        awaitEachGesture {
            val down = awaitFirstDown(requireUnconsumed = false)
            val scope = CoroutineScope(DispatcherIO + SupervisorJob())
            val heldButtonJob = scope.launch {
                var currentDelayMillis = maxDelayMillis
                while (isEnabled && down.pressed) {
                    currentClickListener()
                    delay(currentDelayMillis)
                    val nextMillis =
                        currentDelayMillis - (currentDelayMillis * delayDecayFactor)
                    currentDelayMillis = nextMillis.toLong().coerceAtLeast(minDelayMillis)
                }
            }
            waitForUpOrCancellation()
            heldButtonJob.cancel()
        }
    }
}