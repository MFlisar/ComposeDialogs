package com.michaelflisar.composedialogs.core

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.michaelflisar.composedialogs.core.internal.ComposeAlertDialog
import com.michaelflisar.composedialogs.core.internal.ComposeBottomSheetDialog
import java.io.Serializable

// ------------------
// defaults functions
// ------------------

private val SheetPeekHeight =
    56.dp * 2 // we want peek height for content + fixed buttons so with take 56*2

@Composable
fun Dialog(
    state: DialogState,
    title: String = "",
    titleStyle: DialogTitleStyle = DialogDefaults.titleStyle(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    specialOptions: SpecialOptions = SpecialOptions(),
    onEvent: (event: DialogEvent) -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    when (style) {
        is DialogStyle.BottomSheet -> {
            ComposeBottomSheetDialog(
                title,
                titleStyle,
                icon,
                style,
                buttons,
                options,
                specialOptions,
                state,
                onEvent,
                content
            )
        }
        is DialogStyle.Dialog -> {
            ComposeAlertDialog(
                title,
                titleStyle,
                icon,
                style,
                buttons,
                options,
                specialOptions,
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
    fun titleStyle(
        style: TextStyle? = null,
        fontWeight: FontWeight? = null
    ) = DialogTitleStyle(
        style = null,
        fontWeight = null
    )

    @Composable
    fun titleStyleSmall(fontWeight: FontWeight? = null) = DialogTitleStyle(
        style = MaterialTheme.typography.titleSmall,
        fontWeight = fontWeight
    )

    @Composable
    fun buttonsDisabled() = DialogButtons.DISABLED
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

class DialogStateWithData<T : Any> internal constructor(
    showing: MutableState<Boolean>,
    private val d: MutableState<T?>,
    interactionSource: MutableState<DialogInteractionSource>
) : DialogState(showing, interactionSource) {
    val data: T?
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

sealed class DialogIcon {

    class Painter(
        val painter: @Composable () -> androidx.compose.ui.graphics.painter.Painter
    ) : DialogIcon()

    class Vector(
        val imageVector: ImageVector,
        val tint: Color? = null
    ) : DialogIcon()
}

data class DialogTitleStyle internal constructor(
    val style: TextStyle?,
    val fontWeight: FontWeight?
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

data class SpecialOptions(
    val dialogIntrinsicWidthMin: Boolean = false
)

