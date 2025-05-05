package com.michaelflisar.composedialogs.core

import androidx.compose.runtime.Composable

interface ComposeDialogStyle {

    val type: Type

    //private fun a() = {}

    @Composable
    fun Show(
        title: (@Composable () -> Unit)? = null,
        icon: (@Composable () -> Unit)? = null,
        buttons: DialogButtons,
        dialogOptions: DialogOptions,
        state: DialogState,
        onEvent: (event: DialogEvent) -> Unit,
        content: @Composable () -> Unit
    )

    enum class Type {
        Dialog,
        BottomSheet
    }

}