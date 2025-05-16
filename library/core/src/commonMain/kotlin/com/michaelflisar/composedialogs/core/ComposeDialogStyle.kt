package com.michaelflisar.composedialogs.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

interface ComposeDialogStyle {

    val type: Type

    //private fun a() = {}

    @Composable
    fun Show(
        title: (@Composable () -> Unit)? = null,
        icon: (@Composable () -> Unit)? = null,
        buttons: DialogButtons,
        options: DialogOptions,
        state: DialogState,
        onEvent: (event: DialogEvent) -> Unit,
        content: @Composable () -> Unit
    )

    enum class Type {
        Dialog,
        BottomSheet
    }

    @Composable
    fun spacing(): DialogSpacing {
        return if (type == Type.BottomSheet) {
            DialogSpacing(24.dp, 8.dp)
        } else DialogSpacing(24.dp, 24.dp)
    }

}