package com.michaelflisar.composedialogs.core

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.michaelflisar.composedialogs.core.internal.ComposeAlertDialog
import com.michaelflisar.composedialogs.core.internal.ComposeBottomSheetDialog

// ------------------
// defaults functions
// ------------------

private val SheetPeekHeight = 56.dp * 2 // we want peek height for content + fixed buttons so with take 56*2

@Composable
fun rememberDialogState(
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
): DialogState {

    // showing should survive, even screen rotations and activity recreates
    val showing = rememberSaveable { mutableStateOf(showing) }

    // interaction state should be reset whenever the dialog is reshown
    val interaction = remember(showing.value) {
        mutableStateOf(
            DialogInteractionSource(
                buttonPositiveEnabled = mutableStateOf(buttonPositiveEnabled),
                buttonNegativeEnabled = mutableStateOf(buttonNegativeEnabled),
                dismissAllowed = mutableStateOf(dismissAllowed),
                swipeAllowed = mutableStateOf(swipeAllowed)
            )
        )
    }
    return DialogState(showing, interaction)
}

@Composable
fun <T> rememberDialogState(
    data: T,
    saver: Saver<MutableState<T>, out Any> = autoSaver(),
    showing: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
): DialogStateWithData<T> {

    // showing should survive, even screen rotations and activity recreates
    val showing = rememberSaveable { mutableStateOf(showing) }

    // extra data - should survice screen rotations and activity recreates BUT must be reset if dialog is dismissed
    val d = rememberSaveable(saver = saver) { mutableStateOf(data) }
    if (!showing.value) {
        d.value = data
    }

    // interaction state should be reset whenever the dialog is reshown
    val interaction = remember(showing.value) {
        mutableStateOf(
            DialogInteractionSource(
                buttonPositiveEnabled = mutableStateOf(buttonPositiveEnabled),
                buttonNegativeEnabled = mutableStateOf(buttonNegativeEnabled),
                dismissAllowed = mutableStateOf(dismissAllowed),
                swipeAllowed = mutableStateOf(swipeAllowed)
            )
        )
    }
    return DialogStateWithData(showing, d, interaction)
}

@Composable
fun Dialog(
    state: DialogState,
    title: DialogTitle = DialogDefaults.title(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    when (style) {
        is DialogStyle.BottomSheet -> {
            ComposeBottomSheetDialog(
                title,
                icon,
                style,
                buttons,
                options,
                state,
                onEvent,
                content
            )
        }
        is DialogStyle.Dialog -> {
            ComposeAlertDialog(
                title,
                icon,
                style,
                buttons,
                options,
                state,
                onEvent,
                content
            )
        }
    }
}

// ------------------
// defaults functions
// ------------------

object DialogDefaults {

    @Composable
    fun styleDialog(
        swipeDismissable: Boolean = false,
        // DialogProperties
        dismissOnBackPress: Boolean = true,
        dismissOnClickOutside: Boolean = true,
        securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
        usePlatformDefaultWidth: Boolean = true,
        decorFitsSystemWindows: Boolean = true,
        // AlertDialog Settings
        shape: Shape = AlertDialogDefaults.shape,
        containerColor: Color = AlertDialogDefaults.containerColor,
        iconContentColor: Color = AlertDialogDefaults.iconContentColor,
        titleContentColor: Color = AlertDialogDefaults.titleContentColor,
        textContentColor: Color = AlertDialogDefaults.textContentColor,
        tonalElevation: Dp = AlertDialogDefaults.TonalElevation
    ) = DialogStyle.Dialog(
        swipeDismissable,
        // DialogProperties
        dismissOnBackPress,
        dismissOnClickOutside,
        securePolicy,
        usePlatformDefaultWidth,
        decorFitsSystemWindows,
        // AlertDialog Settings
        shape,
        containerColor,
        iconContentColor,
        titleContentColor,
        textContentColor,
        tonalElevation
    )

    @Composable
    fun styleBottomSheet(
        dragHandle: Boolean = true,
        hideAnimated: Boolean = false,
        resizeContent: Boolean = false,
        peekHeight: Dp = SheetPeekHeight,
        // DialogProperties
        dismissOnBackPress: Boolean = true,
        dismissOnClickOutside: Boolean = true,
        securePolicy: SecureFlagPolicy = SecureFlagPolicy.Inherit,
        //usePlatformDefaultWidth: Boolean = true,
        decorFitsSystemWindows: Boolean = true
    ) = DialogStyle.BottomSheet(
        dragHandle,
        hideAnimated,
        resizeContent,
        peekHeight,
        // DialogProperties
        dismissOnBackPress,
        dismissOnClickOutside,
        securePolicy,
        //usePlatformDefaultWidth,
        decorFitsSystemWindows
    )

    @Composable
    fun buttons(
        positive: DialogButton = DialogButton(stringResource(android.R.string.ok)),
        negative: DialogButton = DialogButton("")
    ) = DialogButtons(
        positive,
        negative
    )

    @Composable
    fun title(
        text: String = "",
        style: TextStyle? = null,
        fontWeight: FontWeight? = null
    ) = DialogTitle(text, style, fontWeight)

    @Composable
    fun titleSmall(
        text: String = "",
        fontWeight: FontWeight? = null
    ) = DialogTitle(text, MaterialTheme.typography.titleSmall, fontWeight)

    @Composable
    fun dialogNoButtons() = DialogButtons.DISABLED
}

// ------------------
// classes
// ------------------

sealed class DialogStyle {

    class Dialog internal constructor(
        val swipeDismissable: Boolean,
        // DialogProperties
        dismissOnBackPress: Boolean,
        dismissOnClickOutside: Boolean,
        securePolicy: SecureFlagPolicy,
        usePlatformDefaultWidth: Boolean,
        decorFitsSystemWindows: Boolean,
        // AlertDialog Settings
        val shape: Shape,
        val containerColor: Color,
        val iconContentColor: Color,
        val titleContentColor: Color,
        val textContentColor: Color,
        val tonalElevation: Dp
    ) : DialogStyle() {
        @OptIn(ExperimentalComposeUiApi::class)
        internal val dialogProperties = DialogProperties(
            dismissOnBackPress,
            dismissOnClickOutside,
            securePolicy,
            usePlatformDefaultWidth,
            decorFitsSystemWindows
        )
    }

    class BottomSheet internal constructor(
        val dragHandle: Boolean,
        val hideAnimated: Boolean,
        val resizeContent: Boolean,
        val peekHeight: Dp,
        // DialogProperties
        dismissOnBackPress: Boolean,
        dismissOnClickOutside: Boolean,
        securePolicy: SecureFlagPolicy,
        //usePlatformDefaultWidth: Boolean,
        decorFitsSystemWindows: Boolean,
    ) : DialogStyle() {
        @OptIn(ExperimentalComposeUiApi::class)
        internal val dialogProperties = DialogProperties(
            dismissOnBackPress,
            dismissOnClickOutside,
            securePolicy,
            false,//usePlatformDefaultWidth,
            decorFitsSystemWindows
        )
    }

    // Popup?
    // Fullscreen?
}

sealed class DialogEvent {
    abstract val dismissed: Boolean
    abstract val isPositiveButton: Boolean // most interesting attribute so we make it easily accessible

    data class Button(val button: DialogButtonType, override val dismissed: Boolean) :
        DialogEvent() {
        override val isPositiveButton = button == DialogButtonType.Positive
    }

    object Dismissed : DialogEvent() {
        override val dismissed = true
        override val isPositiveButton = false
        override fun toString() = "DialogEvent::Dismissed"
    }
}

enum class DialogButtonType {
    Positive,
    Negative
}

class DialogInteractionSource internal constructor(
    val buttonPositiveEnabled: MutableState<Boolean>,
    val buttonNegativeEnabled: MutableState<Boolean>,
    val dismissAllowed: MutableState<Boolean>,
    val swipeAllowed: MutableState<Boolean>
)

@Composable
fun <T : DialogState> T.ifShow(block: @Composable (state: T) -> Unit) {
    if (showing)
        block(this)
}

open class DialogState internal constructor(
    showing: MutableState<Boolean>,
    interactionSource: MutableState<DialogInteractionSource>
) {

    var showing by showing
    var interactionSource by interactionSource

    fun show() {
        showing = true
    }

    fun dismiss(): Boolean {
        if (interactionSource.dismissAllowed.value) {
            showing = false
        }
        return !showing
    }

    internal fun dismiss(
        onEvent: (event: DialogEvent) -> Unit
    ): Boolean {
        if (dismiss())
            onEvent(DialogEvent.Dismissed)
        return !showing
    }

    internal fun onButtonPressed(
        onEvent: (event: DialogEvent) -> Unit,
        button: DialogButtonType,
        dismiss: Boolean
    ) {
        onEvent(DialogEvent.Button(button, dismiss))
    }

    fun isButtonEnabled(button: DialogButtonType): Boolean {
        return when (button) {
            DialogButtonType.Positive -> interactionSource.buttonPositiveEnabled.value
            DialogButtonType.Negative -> interactionSource.buttonNegativeEnabled.value
        }
    }

    fun enableButton(
        button: DialogButtonType,
        enabled: Boolean
    ) {
        when (button) {
            DialogButtonType.Positive -> interactionSource.buttonPositiveEnabled.value = enabled
            DialogButtonType.Negative -> interactionSource.buttonNegativeEnabled.value = enabled
        }
    }

    fun dismissable(enabled: Boolean) {
        interactionSource.dismissAllowed.value = enabled
    }
}

class DialogStateWithData<T> internal constructor(
    showing: MutableState<Boolean>,
    private val d: MutableState<T>,
    interactionSource: MutableState<DialogInteractionSource>
) : DialogState(showing, interactionSource) {
    val data: T
        get() = d.value

    fun requireData() = d.value!!
    fun show(data: T) {
        this.d.value = data
        show()
    }
}

data class DialogButton(
    val text: String
)

data class DialogIcon(
    val painter: Painter,
    val tint: Color? = null
)

data class DialogTitle internal constructor(
    val text: String,
    val style: TextStyle?,
    val fontWeight: FontWeight?,
    //val alignment: Alignment.Horizontal
)

data class DialogButtons internal constructor(
    val positive: DialogButton,
    val negative: DialogButton
) {
    companion object {
        val DISABLED = DialogButtons(DialogButton(""), DialogButton(""))
    }
}

data class Options(
    val dismissOnButtonClick: Boolean = true,
    val wrapContentInScrollableContainer: Boolean = false
)

