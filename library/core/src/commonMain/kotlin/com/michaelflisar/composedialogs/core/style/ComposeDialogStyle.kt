package com.michaelflisar.composedialogs.core.style

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.SpecialOptions

interface ComposeDialogStyle {

    val type: Type

    @Composable
    fun Show(
        title: String? = null,
        icon: (@Composable () -> Unit)? = null,
        buttons: DialogButtons,
        options: Options,
        specialOptions: SpecialOptions,
        state: DialogState,
        onEvent: (event: DialogEvent) -> Unit,
        content: @Composable (ColumnScope.() -> Unit)
    )

    enum class Type {
        Dialog,
        BottomSheet
    }

}