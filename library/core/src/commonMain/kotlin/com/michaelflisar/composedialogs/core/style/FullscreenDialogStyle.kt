package com.michaelflisar.composedialogs.core.style

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.core.Dialog
import com.composables.core.DialogPanel
import com.composables.core.DialogProperties
import com.composables.core.rememberDialogState
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.internal.ComposeDialogContent
import com.michaelflisar.composedialogs.core.internal.ComposeDialogTitleToolbar
import com.michaelflisar.composedialogs.core.updateStatusbarColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object FullscreenDialogStyleDefaults {

    val toolbarColor: Color
        @Composable get() = MaterialTheme.colorScheme.background

    val toolbarActionColor: Color
        @Composable get() = MaterialTheme.colorScheme.primary

    val containerColor: Color
        @Composable get() = MaterialTheme.colorScheme.background

    val iconColor
        @Composable get() = MaterialTheme.colorScheme.secondary

    val titleColor
        @Composable get() = MaterialTheme.colorScheme.onBackground

    val contentColor
        @Composable get() = MaterialTheme.colorScheme.onBackground
}

internal class FullscreenDialogStyle(
    private val darkStatusBar: Boolean,
    private val menuActions: @Composable (RowScope.() -> Unit)?,
    // DialogProperties
    private val dismissOnBackPress: Boolean,
    // Style
    private val toolbarColor: Color,
    private val toolbarActionColor: Color,
    private val containerColor: Color,
    private val iconColor: Color,
    private val titleColor: Color,
    private val contentColor: Color,
) : ComposeDialogStyle {

    override val type = ComposeDialogStyle.Type.Dialog

    @Composable
    override fun Show(
        title: (@Composable () -> Unit)?,
        icon: @Composable (() -> Unit)?,
        buttons: DialogButtons,
        dialogOptions: DialogOptions,
        state: DialogState,
        onEvent: (event: DialogEvent) -> Unit,
        content: @Composable () -> Unit,
    ) {
        val coroutineScope = rememberCoroutineScope()
        val dialogState = rememberDialogState(initiallyVisible = true)

        val animDurationEnter = 250
        val animDurationExit = 150
        val animEnter =
            slideInVertically(initialOffsetY = { it }) + fadeIn(tween(durationMillis = animDurationEnter))
        val animExit =
            slideOutVertically(targetOffsetY = { it }) + fadeOut(tween(durationMillis = animDurationExit))

        val dismiss = { dialogState.visible = false }
        var buttonPressed = false
        val waitForDismissAnimationAndUpdateState = {
            coroutineScope.launch {
                delay(animDurationExit.toLong())
                if (buttonPressed)
                    state.dismiss()
                else
                    state.dismiss(onEvent)
            }
        }

        val shouldDismissOnBackPress by remember {
            derivedStateOf { dismissOnBackPress && state.interactionSource.dismissAllowed.value }
        }

        Dialog(
            state = dialogState,
            properties = DialogProperties(
                dismissOnBackPress = shouldDismissOnBackPress
            ),
            onDismiss = {
                waitForDismissAnimationAndUpdateState()
            }
        ) {

            updateStatusbarColor(darkStatusBar)

            DialogPanel(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .imePadding()
                    .background(containerColor),
                enter = animEnter,
                exit = animExit
            ) {
                val dismissOnButtonPressed = {
                    buttonPressed = true
                    dismiss()
                    waitForDismissAnimationAndUpdateState()
                    Unit
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Icon + Title
                    ComposeDialogTitleToolbar(
                        modifier = Modifier,
                        title = title,
                        icon = icon,
                        toolbarColor = toolbarColor,
                        toolbarActionColor = toolbarActionColor,
                        iconColor = iconColor,
                        titleColor = titleColor,
                        menuActions = menuActions,
                        buttons = buttons,
                        state = state,
                        dialogOptions = dialogOptions,
                        dismissOnButtonPressed = dismissOnButtonPressed,
                        onEvent = onEvent
                    )

                    // Content
                    ComposeDialogContent(
                        content = content,
                        contentColor = contentColor,
                        modifier = Modifier.weight(1f).padding(all = 16.dp),
                        bottomPadding = dialogOptions.spacingContentToBottom // we don't have buttons in this style!
                    )

                    // Buttons
                    //ComposeDialogButtons(
                    //    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    //    buttons = buttons,
                    //    options = options,
                    //    state = state,
                    //    dismissOnButtonPressed = dismissOnButtonPressed,
                    //    onEvent = onEvent
                    //)
                }
            }
        }
    }
}