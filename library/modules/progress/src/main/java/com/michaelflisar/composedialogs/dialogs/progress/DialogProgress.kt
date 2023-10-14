package com.michaelflisar.composedialogs.dialogs.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

@Composable
fun DialogProgress(
    state: DialogState,
    // Custom - Required
    // ...
    // Custom - Optional
    label: String = "",
    progressStyle: DialogProgressStyle = DialogProgressStyle.Indeterminate(),
    // Base Dialog - Optional
    title: String = "",
    titleStyle: DialogTitleStyle = DialogDefaults.titleStyle(),
    icon: DialogIcon? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    Dialog(state, title, titleStyle, icon, style, buttons, options, SpecialOptions(true), onEvent) {
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
                } else Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                    CircularProgressIndicator()
                }
            }
            is DialogProgressStyle.Determinate -> {
                val progress by animateFloatAsState(targetValue = progressStyle.progress.toFloat() / progressStyle.max.toFloat())
                if (progressStyle.linear) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = progress)
                } else Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                    CircularProgressIndicator(progress = progress)
                }
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