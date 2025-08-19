package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.ComposeDialogStyle

@Composable
expect fun BillingDemos(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
)