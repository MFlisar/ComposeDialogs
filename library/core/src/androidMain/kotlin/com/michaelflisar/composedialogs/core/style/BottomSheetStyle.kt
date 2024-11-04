package com.michaelflisar.composedialogs.core.style

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.SecureFlagPolicy
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.SpecialOptions
import com.michaelflisar.composedialogs.core.copied.AlertDialogFlowRow
import com.michaelflisar.composedialogs.core.copied.ButtonsCrossAxisSpacing
import com.michaelflisar.composedialogs.core.copied.ButtonsMainAxisSpacing
import com.michaelflisar.composedialogs.core.views.ComposeDialogButton
import kotlinx.coroutines.launch

internal class BottomSheetStyle(
    val dragHandle: Boolean,
    val hideAnimated: Boolean,
    //val resizeContent: Boolean,
    //val peekHeight: Dp,
    // DialogProperties
    val dismissOnBackPress: Boolean,
    val dismissOnClickOutside: Boolean,
    val securePolicy: SecureFlagPolicy,
    //usePlatformDefaultWidth: Boolean,
    //decorFitsSystemWindows: Boolean,
) : ComposeDialogStyle {

    override val type = ComposeDialogStyle.Type.BottomSheet

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Show(
        title: (@Composable () -> Unit)?,
        icon: @Composable (() -> Unit)?,
        buttons: DialogButtons,
        options: Options,
        specialOptions: SpecialOptions,
        state: DialogState,
        onEvent: (event: DialogEvent) -> Unit,
        content: @Composable (ColumnScope.() -> Unit)
    ) {
        val coroutineScope = rememberCoroutineScope()

        val dismissAllowed = rememberUpdatedState(state.interactionSource.dismissAllowed.value)

        val skipPartiallyExpanded by rememberSaveable { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = skipPartiallyExpanded
        ) {
            dismissOnClickOutside && dismissAllowed.value
        }
        val buttonPressed = remember { mutableStateOf(false) }

        val dismiss = {
            if (hideAnimated) {
                if (state.interactionSource.dismissAllowed.value) {
                    coroutineScope.launch {
                        sheetState.hide()
                    }
                    true
                } else {
                    coroutineScope.launch {
                        sheetState.show()
                    }
                    false
                }
            } else {
                if (buttonPressed.value)
                    state.dismiss()
                else
                    state.dismiss(onEvent)
            }
        }

        val sheetSize = remember { mutableStateOf(IntSize.Zero) }
        val shoudDismissOnBackPress by remember {
            derivedStateOf { dismissOnBackPress && dismissAllowed.value }
        }

        ModalBottomSheet(
            onDismissRequest = { dismiss() },
            modifier = Modifier
                .onSizeChanged { sheetSize.value = it },
            sheetState = sheetState,
            dragHandle = if (dragHandle) {
                { BottomSheetDefaults.DragHandle() }
            } else null,
            properties = ModalBottomSheetProperties(
                securePolicy,
                shoudDismissOnBackPress
            )
        ) {
            BottomSheetContent(
                sheetState,
                sheetSize,
                dismiss,
                title,
                icon,
                options,
                content,
                buttons,
                state,
                buttonPressed,
                onEvent
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ColumnScope.BottomSheetContent(
        sheetState: SheetState,
        sheetSize: MutableState<IntSize>,
        dismiss: () -> Boolean,
        title: @Composable() (() -> Unit)?,
        icon: @Composable() (() -> Unit)?,
        options: Options,
        content: @Composable() (ColumnScope.() -> Unit),
        buttons: DialogButtons,
        state: DialogState,
        buttonPressed: MutableState<Boolean>,
        onEvent: (event: DialogEvent) -> Unit
    ) {

        val density = LocalDensity.current

        val contentPadding = 16.dp
        val modifierContentPadding = Modifier.padding(
            start = contentPadding,
            end = contentPadding,
            top = contentPadding
        )

        val contentSize = remember { mutableStateOf(IntSize.Zero) }
        val footerSize = remember { mutableStateOf(IntSize.Zero) }
        val offset = remember { mutableStateOf<Int?>(null) }
        LaunchedEffect(Unit) {
            offset.value = sheetState.requireOffset().toInt()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifierContentPadding),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    //.nestedScroll(rememberNestedScrollInteropConnection())
                    .onSizeChanged { contentSize.value = it }
                    .padding(bottom = with(density) { footerSize.value.height.toDp() })
                //.offset { IntOffset(0, modalBottomSheetState.offset.roundToInt()) }
            ) {
                //val visibleHeight = remember {
                //    derivedStateOf {
                //        if (offset.value == null) {
                //            0
                //        } else {
                //            sheetState.requireOffset().toInt() - sheetSize.value.height
                //        }
//
                //    }
                //}
                //Text(text =
                //    "INFOS:\n" + (if (offset.value == null) "" else sheetState.requireOffset().toInt().toString()) + " | "
                //    + "content = ${contentSize.value.height} | footer = ${footerSize.value.height} | sheetSize = ${sheetSize.value.height}" + "\n" +
                //    "visibleHeight = ${visibleHeight.value}"
//
                //)
                SheetHeader(title, icon)
                SheetContent(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f, false),
                    options,
                    content
                )
            }
            SheetFooter(
                Modifier
                    .onSizeChanged { footerSize.value = it }
                //.offset { IntOffset(x = 0, y = -sheetState.requireOffset().toInt()) }
                ,
                buttons,
                options,
                state,
                dismiss = {
                    buttonPressed.value = true
                    val dismissed = dismiss()
                    buttonPressed.value = false
                    dismissed
                },
                onEvent
            )
        }
    }

    @Composable
    private fun ColumnScope.SheetHeader(
        title: (@Composable () -> Unit)? = null,
        icon: (@Composable () -> Unit)? = null
    ) {
        // Icon
        if (icon != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                //.size(24.dp)
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurface
                ) {
                    icon()
                }

            }
        }
        // Title
        if (title != null) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.headlineSmall
            ) {
                Box(
                    modifier = if (icon != null) Modifier.align(Alignment.CenterHorizontally) else Modifier
                ) {
                    title()
                }
            }
        }
        // Space
        Spacer(modifier = Modifier.height(8.dp))
    }

    @Composable
    private fun SheetFooter(
        modifier: Modifier,
        buttons: DialogButtons,
        options: Options,
        state: DialogState,
        dismiss: () -> Boolean,
        onEvent: (event: DialogEvent) -> Unit
    ) {
        // Buttons
        //val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
        //    LocalAbsoluteTonalElevation.current
        //)
        Row(
            modifier = modifier
            //.background(backgroundColor)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            //CompositionLocalProvider(LocalContentColor provides buttonContentColor) {
            //val textStyle =  MaterialTheme.typography.fromToken(DialogTokens.ActionLabelTextFont)
            //ProvideTextStyle(value = textStyle) {
            AlertDialogFlowRow(
                mainAxisSpacing = ButtonsMainAxisSpacing,
                crossAxisSpacing = ButtonsCrossAxisSpacing
            ) {
                ComposeDialogButton(
                    buttons.negative,
                    DialogButtonType.Negative,
                    options,
                    state,
                    requestDismiss = {
                        dismiss()
                    },
                    onEvent
                )
                ComposeDialogButton(
                    buttons.positive,
                    DialogButtonType.Positive,
                    options,
                    state,
                    requestDismiss = {
                        dismiss()
                    },
                    onEvent
                )
            }
            //}
            //}
        }
    }

    @Composable
    private fun SheetContent(
        modifier: Modifier,
        options: Options,
        content: @Composable (ColumnScope.() -> Unit)
    ) {
        val scrollModifier = if (options.wrapContentInScrollableContainer) {
            Modifier
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
        } else Modifier

        val density = LocalDensity.current

        //var contentHeight by remember { mutableStateOf(0.dp) }
        //val resizeByPaddingModifier = if (resizeContent) {
        //    Modifier.padding(bottom = with(density) { offsetState.value.toDp() })
        //    //Modifier.heightIn(max = contentHeight - with(LocalDensity.current) { offsetState.value.toDp() })
        //} else Modifier

        // Content
        Column(
            modifier = modifier
                //.then(resizeByPaddingModifier)
                .fillMaxWidth()
                .wrapContentHeight()
                //.onSizeChanged {
                //    contentHeight = with(density) { it.height.toDp() }
                //}
                .then(scrollModifier)
        ) {
            content()
        }
    }
}