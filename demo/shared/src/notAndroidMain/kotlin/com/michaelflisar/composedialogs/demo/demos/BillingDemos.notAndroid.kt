package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.democomposables.layout.DemoRegion
import com.michaelflisar.democomposables.layout.DemoRow

@Composable
actual fun BillingDemos(
    style: ComposeDialogStyle,
    icon: @Composable (() -> Unit)?,
    showInfo: (String) -> Unit
) {
    DemoRegion("Billing Dialogs")
    DemoRow {
        Text("Billing only works on android!")
    }
}