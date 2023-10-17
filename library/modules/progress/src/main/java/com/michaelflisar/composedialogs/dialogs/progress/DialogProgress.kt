package com.michaelflisar.composedialogs.dialogs.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.*

/**
 * Shows a dialog with an optional label and a progress indicator
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param label the label of the progress
 * @param progressStyle the style of the progress indicator ([DialogProgressStyle])
 */
@Composable
fun DialogProgress(
    state: DialogState,
    // Custom - Required
    // ...
    // Custom - Optional
    label: String = "",
    progressStyle: DialogProgressStyle = DialogProgressStyle.Indeterminate(),
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, SpecialOptions(true), onEvent) {
        if (label.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                text = label
            )
        }
        when (progressStyle) {
            is DialogProgressStyle.Indeterminate -> {
                if (progressStyle.linear) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                } else Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CircularProgressIndicator()
                }
            }

            is DialogProgressStyle.Determinate -> {
                val progress by animateFloatAsState(targetValue = progressStyle.progress.toFloat() / progressStyle.max.toFloat())
                if (progressStyle.linear) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = progress)
                } else Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CircularProgressIndicator(progress = progress)
                }
            }
        }
    }
}

/**
 * the progress style
 */
sealed class DialogProgressStyle {

    abstract val linear: Boolean

    /**
     * indeterminate progress style
     *
     * @param linear if true, a linear progress indicator is used, otherwise a circular
     */
    class Indeterminate(
        override val linear: Boolean = true
    ) : DialogProgressStyle()

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
    ) : DialogProgressStyle()
}