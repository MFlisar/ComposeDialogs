package com.michaelflisar.composedialogs.core.style

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.composables.core.Dialog
import com.composables.core.DialogPanel
import com.composables.core.DialogProperties
import com.composables.core.rememberDialogState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogOptions
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.composables.ComposeDialogContent
import com.michaelflisar.composedialogs.core.internal.ComposeDialogButton
import com.michaelflisar.composedialogs.core.internal.ComposeDialogImageButton
import com.michaelflisar.composedialogs.core.internal.TitleIcon
import com.michaelflisar.composedialogs.core.internal.TitleTitle
import com.michaelflisar.composedialogs.core.updateStatusbarColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object FullscreenDialogStyleDefaults {

    val toolbarColor: Color
        @Composable get() = MaterialTheme.colorScheme.primary

    val toolbarColorExpanded: Color
        @Composable get() = MaterialTheme.colorScheme.background

    val toolbarActionColor: Color
        @Composable get() = MaterialTheme.colorScheme.onPrimary

    val toolbarActionColorExpanded: Color
        @Composable get() = MaterialTheme.colorScheme.onBackground

    val toolbarContentColor: Color
        @Composable get() = MaterialTheme.colorScheme.onPrimary

    val toolbarContentColorExpanded: Color
        @Composable get() = MaterialTheme.colorScheme.onBackground

    val containerColor: Color
        @Composable get() = MaterialTheme.colorScheme.background

    val contentColor
        @Composable get() = MaterialTheme.colorScheme.onBackground

    val iconColor
        @Composable get() = MaterialTheme.colorScheme.secondary

    @Composable
    fun NavigationIcon() {
        Icon(Icons.Default.Close, "Back")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
internal class FullscreenDialogStyle(
    private val navigationIcon: (@Composable () -> Unit)?,
    private val menuActions: @Composable (RowScope.() -> Unit)?,
    private val toolbarScrollBehaviour: TopAppBarScrollBehavior?,
    private val showIconBelowToolbar: Boolean,
    // DialogProperties
    private val dismissOnBackPress: Boolean,
    // Style
    private val toolbarColor: Color,
    private val toolbarColorExpanded: Color,
    private val toolbarActionColor: Color,
    private val toolbarActionColorExpanded: Color,
    private val toolbarContentColor: Color,
    private val toolbarContentColorExpanded: Color,
    private val iconColor: Color,
    private val containerColor: Color,
    private val contentColor: Color,
    private val applyContentPadding: Boolean,
) : ComposeDialogStyle {

    override val type = ComposeDialogStyle.Type.Dialog

    @Composable
    override fun Show(
        title: (@Composable () -> Unit)?,
        icon: @Composable (() -> Unit)?,
        buttons: DialogButtons,
        options: DialogOptions,
        state: DialogState,
        onEvent: (event: DialogEvent) -> Unit,
        content: @Composable () -> Unit,
    ) {
        val coroutineScope = rememberCoroutineScope()
        val dialogState = rememberDialogState(initiallyVisible = true)

        val spacing = spacing()

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

            val statusBarColor =
                rememberColor(toolbarScrollBehaviour, toolbarColor, toolbarColorExpanded)
            val darkStatusBar =
                remember { derivedStateOf { statusBarColor.value.luminance() < .5f } }
            updateStatusbarColor(darkStatusBar.value)

            DialogPanel(
                modifier = Modifier
                    .fillMaxSize(),
                enter = animEnter,
                exit = animExit
            ) {
                val dismissOnButtonPressed = {
                    buttonPressed = true
                    dismiss()
                    waitForDismissAnimationAndUpdateState()
                    Unit
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            toolbarScrollBehaviour
                                ?.let { Modifier.nestedScroll(it.nestedScrollConnection) }
                                ?: Modifier
                        ),
                    topBar = {
                        // Icon + Title
                        Toolbar(
                            title = title,
                            titleColor = toolbarContentColor,
                            titleColorExpanded = toolbarContentColorExpanded,
                            toolbarColor = toolbarColor,
                            toolbarColorExpanded = toolbarColorExpanded,
                            toolbarContentColor = toolbarContentColor,
                            toolbarContentColorExpanded = toolbarContentColorExpanded,
                            toolbarActionColor = toolbarActionColor,
                            toolbarActionColorExpanded = toolbarActionColorExpanded,
                            scrollBehavior = toolbarScrollBehaviour,
                            navigationIcon = navigationIcon,
                            menuActions = menuActions,
                            state = state,
                            options = options,
                            buttons = buttons,
                            dismissOnButtonPressed = dismissOnButtonPressed,
                            onEvent = onEvent
                        )
                    },
                    content = { paddingValues ->
                        Column(
                            modifier = Modifier.fillMaxSize().padding(paddingValues)
                        ) {
                            if (showIconBelowToolbar && icon != null) {
                                TitleIcon(
                                    icon = icon,
                                    iconColor = iconColor,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                        .padding(top = 16.dp)
                                )
                            }
                            // Content
                            ComposeDialogContent(
                                content = content,
                                contentColor = contentColor,
                                modifier = Modifier.weight(1f)
                                    .then(
                                        if (applyContentPadding) {
                                            Modifier.padding(
                                                top = 16.dp,
                                                start = 16.dp,
                                                end = 16.dp
                                            ) // not bottom padding => this is applied via bottomPadding directly below because we do not have buttons here!
                                        } else Modifier
                                    ),
                                bottomPadding = if (applyContentPadding) spacing.spacingContentToBottom /* 16.dp */ else 0.dp // we don't have buttons in this style!
                            )
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Toolbar(
    title: @Composable (() -> Unit)?,
    titleColor: Color,
    titleColorExpanded: Color,
    toolbarColor: Color,
    toolbarColorExpanded: Color,
    toolbarContentColor: Color,
    toolbarContentColorExpanded: Color,
    toolbarActionColor: Color,
    toolbarActionColorExpanded: Color,
    scrollBehavior: TopAppBarScrollBehavior?,
    navigationIcon: @Composable (() -> Unit)?,
    menuActions: @Composable (RowScope.() -> Unit)?,
    state: DialogState,
    options: DialogOptions,
    buttons: DialogButtons,
    dismissOnButtonPressed: () -> Unit,
    onEvent: (event: DialogEvent) -> Unit,
) {
    val currentTitleColor = rememberColor(scrollBehavior, titleColor, titleColorExpanded)
    val currentActionColor =
        rememberColor(scrollBehavior, toolbarActionColor, toolbarActionColorExpanded)
    val currentContentColor =
        rememberColor(scrollBehavior, toolbarContentColor, toolbarContentColorExpanded)

    LargeTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        scrollBehavior = scrollBehavior,
        title = { TitleTitle(title, currentTitleColor.value, Modifier) },
        navigationIcon = {
            if (navigationIcon != null) {
                ComposeDialogImageButton(
                    buttonType = DialogButtonType.Negative,
                    icon = navigationIcon,
                    state = state,
                    options = options,
                    dismissOnButtonPressed = dismissOnButtonPressed,
                    onEvent = onEvent
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = toolbarColorExpanded, // expanded color
            scrolledContainerColor = toolbarColor, // collapsed color
            navigationIconContentColor = currentContentColor.value,
            titleContentColor = currentContentColor.value,
            actionIconContentColor = currentActionColor.value
        ),
        actions = menuActions ?: {
            ComposeDialogButton(
                button = buttons.positive,
                buttonType = DialogButtonType.Positive,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = currentActionColor.value
                ),
                state = state,
                options = options,
                dismissOnButtonPressed = dismissOnButtonPressed,
                onEvent = onEvent
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberColor(
    scrollBehavior: TopAppBarScrollBehavior?,
    collapsedColor: Color,
    expandedColor: Color,
): State<Color> {
    // collapsedFraction == 1 => fully collapsed
    // collapsedFraction == 0 => fully expanded
    // =>
    // - <= .5 => expanded color
    // - > .5 => collapsed color
    return remember(scrollBehavior, collapsedColor, expandedColor) {
        derivedStateOf {
            if (scrollBehavior != null) {
                if (scrollBehavior.state.collapsedFraction >= .5f) collapsedColor else expandedColor
            } else collapsedColor
        }
    }
}