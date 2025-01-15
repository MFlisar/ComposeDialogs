package com.michaelflisar.composedialogs.dialogs.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.ComposeDialogStyle

/* --8<-- [start: constructor] */
/**
 * Shows a dialog with an optional label and a progress indicator
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param content the content of the progress
 * @param progressStyle the style of the progress indicator ([DialogProgress.Style])
 */
@Composable
fun DialogProgress(
    state: DialogState,
    // Custom - Required
    // ...
    // Custom - Optional
    content: (@Composable ColumnScope.() -> Unit)? = null,
    progressStyle: DialogProgress.Style = DialogProgress.Style.Indeterminate(),
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
)
/* --8<-- [end: constructor] */
{
    Dialog(state, title, icon, style, buttons, options, onEvent) {
        Column {
            if (content != null) {
                content()
                Spacer(modifier = Modifier.height(8.dp))
            }
            when (progressStyle) {
                is DialogProgress.Style.Indeterminate -> {
                    if (progressStyle.linear) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    } else Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is DialogProgress.Style.Determinate -> {
                    val progress by animateFloatAsState(targetValue = progressStyle.progress.toFloat() / progressStyle.max.toFloat())
                    if (progressStyle.linear) {
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    } else Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CircularProgressIndicator(progress = { progress })
                    }
                }
            }
        }
    }
}

@Stable
object DialogProgress {

    /**
     * the progress style
     */
    sealed class Style {

        abstract val linear: Boolean

        /**
         * indeterminate progress style
         *
         * @param linear if true, a linear progress indicator is used, otherwise a circular
         */
        class Indeterminate(
            override val linear: Boolean = true
        ) : Style()

        /**
         * determinate progress style
         *
         * @param linear if true, a linear progress indicator is used, otherwise a circular
         * @param progress the current progress value
         * @param max the maximum progress value
         */
        class Determinate(
            override val linear: Boolean = true,
            val progress: Int,
            val max: Int
        ) : Style()
    }
}