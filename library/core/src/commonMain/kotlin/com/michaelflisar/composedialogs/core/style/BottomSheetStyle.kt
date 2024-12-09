package com.michaelflisar.composedialogs.core.style

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalSheetProperties
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.internal.ComposeDialogButtons
import com.michaelflisar.composedialogs.core.internal.ComposeDialogContent
import com.michaelflisar.composedialogs.core.internal.ComposeDialogTitle
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

object BottomSheetStyleDefaults {

    val peekHeight: (containerHeight: Dp, sheetHeight: Dp) -> Dp =
        { containerHeight, sheetHeight -> sheetHeight * .5f }

    val shape: Shape
        @Composable get() = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    val containerColor: Color
        @Composable get() = MaterialTheme.colorScheme.surface

    val iconColor
        @Composable get() = MaterialTheme.colorScheme.secondary

    val titleColor
        @Composable get() = MaterialTheme.colorScheme.onSurface

    val contentColor
        @Composable get() = MaterialTheme.colorScheme.onSurface
}

internal class BottomSheetStyle(
    private val dragHandle: Boolean,
    private val peekHeight: ((containerHeight: Dp, sheetHeight: Dp) -> Dp)?,
    private val expandInitially: Boolean,
    // DialogProperties
    private val dismissOnBackPress: Boolean,
    private val dismissOnClickOutside: Boolean,
    // Style
    private val shape: Shape,
    private val containerColor: Color,
    private val iconColor: Color,
    private val titleColor: Color,
    private val contentColor: Color
) : ComposeDialogStyle {

    override val type = ComposeDialogStyle.Type.BottomSheet

    @Composable
    override fun Show(
        title: (@Composable () -> Unit)?,
        icon: @Composable (() -> Unit)?,
        buttons: DialogButtons,
        options: Options,
        state: DialogState,
        onEvent: (event: DialogEvent) -> Unit,
        content: @Composable () -> Unit
    ) {
        val coroutineScope = rememberCoroutineScope()

        val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
        val peek = peekHeight?.let {
            SheetDetent("peek", it)
        }

        val shoudDismissOnBackPress by remember {
            derivedStateOf { dismissOnBackPress && state.interactionSource.dismissAllowed.value }
        }
        val shoudDismissOnClickOutside by remember {
            derivedStateOf { dismissOnClickOutside && state.interactionSource.dismissAllowed.value }
        }

        val bottomSheetState = rememberModalBottomSheetState(
            initialDetent = if (expandInitially || peek == null) SheetDetent.FullyExpanded else peek,
            detents = listOfNotNull(SheetDetent.Hidden, peek, SheetDetent.FullyExpanded).filter {
                !skipPartiallyExpanded || it != peek
            },
            confirmDetentChange = {
                if (it == SheetDetent.Hidden) {
                    state.interactionSource.dismissAllowed.value
                }
                true
            }
        )
        val bottomSheetProperties = ModalSheetProperties(
            dismissOnBackPress = shoudDismissOnBackPress,
            dismissOnClickOutside = shoudDismissOnClickOutside
        )

        val onDismiss = { buttonPressed: Boolean ->
            //if (state.interactionSource.dismissAllowed.value) {
            coroutineScope.launch {
                bottomSheetState.animateTo(SheetDetent.Hidden)
                if (buttonPressed)
                    state.dismiss()
                else
                    state.dismiss(onEvent)
            }
            true
            //} else {
            //    coroutineScope.launch {
            //        bottomSheetState.animateTo(SheetDetent.FullyExpanded)
            //    }
            //    false
            //}
        }

        val density = LocalDensity.current

        ModalBottomSheet(
            state = bottomSheetState,
            properties = bottomSheetProperties,
            onDismiss = { onDismiss(false) }
        ) {

            val contentSize = remember { mutableStateOf(DpSize.Zero) }
            val scrimSize = remember { mutableStateOf(DpSize.Zero) }
            val isCompact by remember {
                derivedStateOf {
                    scrimSize.value.width < 640.dp
                }
            }

            Scrim(
                enter = fadeIn(),
                exit = fadeOut(),
                scrimColor = MaterialTheme.colorScheme.scrim.copy(0.3f),
                modifier = Modifier
                    .onSizeChanged {
                        scrimSize.value =
                            with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                    }
            )
            Sheet(
                modifier = Modifier
                    .onSizeChanged {
                        contentSize.value =
                            with(density) { DpSize(it.width.toDp(), it.height.toDp()) }
                    }
                    .padding(top = 12.dp)
                    .let { if (isCompact) it else it.padding(horizontal = 56.dp) }
                    .statusBarsPadding()
                    .padding(
                        WindowInsets.navigationBars
                            .only(WindowInsetsSides.Horizontal)
                            .asPaddingValues()
                    )
                    .shadow(4.dp, shape)
                    .clip(shape)
                    .background(containerColor)
                    .widthIn(max = 640.dp)
                    .fillMaxWidth()
                    .imePadding()

            ) {

                Column(
                    Modifier
                        .fillMaxWidth()
                ) {

                    // 1) Drag Header On Top
                    if (dragHandle) {
                        DragIndication(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 22.dp)
                                .background(
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                    RoundedCornerShape(percent = 50)
                                )
                                .width(32.dp)
                                .height(4.dp)
                        )
                    }

                    // 2) Icon + Title
                    ComposeDialogTitle(title, icon, iconColor, titleColor, modifier = Modifier.padding(horizontal = 24.dp))

                    // 3) Content
                    ComposeDialogContent(content, contentColor, modifier = Modifier.padding(horizontal = 24.dp))

                    // 4) Footer
                    ComposeDialogButtons(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    0,
                                    (contentSize.value.height.roundToPx() - bottomSheetState.offset).roundToInt() * -1
                                )
                            }
                            .background(containerColor)
                            .padding(
                                bottom = WindowInsets.navigationBars
                                    .only(WindowInsetsSides.Vertical)
                                    .asPaddingValues()
                                    .calculateBottomPadding()
                            ),
                        buttons = buttons,
                        options = options,
                        state = state,
                        dismissOnButtonPressed = { onDismiss(true) },
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}