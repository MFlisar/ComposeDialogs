package com.michaelflisar.composedialogs.dialogs.progress

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.*

@Composable
fun DialogProgress(
    state: DialogState,
    // Custom - Required
    // ...
    // Custom - Optional
    label: String = "",
    progressStyle: DialogProgressStyle = DialogProgressStyle.Indeterminate(),
    // Base Dialog - Optional
    title: DialogTitle = DialogDefaults.title(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, icon, style, buttons, options, onEvent) {
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
                    LinearProgressIndicator()
                } else CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            is DialogProgressStyle.Determinate -> {
                if (progressStyle.linear) {
                    LinearProgressIndicator(progressStyle.progress.toFloat() / progressStyle.max.toFloat())
                } else CircularProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = progressStyle.progress.toFloat() / progressStyle.max.toFloat()
                )
            }
        }
    }
}

sealed class DialogProgressStyle {

    abstract val linear: Boolean

    class Indeterminate(
        override val linear: Boolean = true
    ) : DialogProgressStyle()

    class Determinate(
        override val linear: Boolean = true,
        val progress: Int,
        val max: Int
    ) : DialogProgressStyle()
}