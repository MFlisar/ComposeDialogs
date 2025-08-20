package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.democomposables.DemoButton
import com.michaelflisar.democomposables.layout.DemoRegion
import com.michaelflisar.democomposables.layout.DemoRow
import com.michaelflisar.composedialogs.dialogs.billing.DialogBilling
import com.michaelflisar.kotbilling.classes.Product
import com.michaelflisar.kotbilling.classes.ProductType

@Composable
actual fun BillingDemos(
    style: ComposeDialogStyle,
    icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    DemoRegion("Billing Dialogs")
    DemoRow {
        DemoBilling(style, icon, showInfo)
    }
}

@Composable
private fun RowScope.DemoBilling(
    style: ComposeDialogStyle, icon: (@Composable () -> Unit)?,
    showInfo: (info: String) -> Unit
) {
    val state = rememberDialogState()
    if (state.visible) {
        DialogBilling(
            state = state,
            title = { Text("Dialog") },
            products = listOf(
                DialogBilling.BillingProduct(
                    Product(
                        "pro-version",
                        ProductType.InApp,
                        false
                    ),
                    icon = {
                        Icon(Icons.Default.Shop, null)
                    }
                ),
                DialogBilling.BillingProduct(
                    Product(
                        "consumable1",
                        ProductType.InApp,
                        true
                    ),
                    icon = {
                        Icon(Icons.Default.Fastfood, null)
                    }
                )
            ),
            icon = icon,
            style = style,
            onEvent = { event ->
                showInfo("Event $event")
            }
        )
    }
    DemoButton(
        Icons.Default.Info,
        "Simple Billing Dialog",
        "Shows a list with prices (and purchase state if already owned) of items to buy (this will only return a non empty result in a real app with products!)"
    ) {
        state.show()
    }
}