package com.michaelflisar.composedialogs.dialogs.progress

import androidx.compose.runtime.Stable

@Stable
object DialogProgress {

    sealed class Style {
        abstract val linear: Boolean

        class Indeterminate(
            override val linear: Boolean = true
        ) : Style()

        class Determinate(
            override val linear: Boolean = true,
            val progress: Int,
            val max: Int
        ) : Style()
    }
}
