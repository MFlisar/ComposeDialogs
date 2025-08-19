package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.demo.composables.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.composables.DemoDialogRow

@Composable
actual fun BillingDemos(
    style: ComposeDialogStyle,
    icon: @Composable (() -> Unit)?,
    showInfo: (String) -> Unit
) {
    DemoDialogRegion("Billing Dialogs")
    DemoDialogRow {
        Text("Billing only works on android!")
    }
}