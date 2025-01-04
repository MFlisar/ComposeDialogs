package com.michaelflisar.composedialogs.demo.demos

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composedialogs.core.ComposeDialogStyle
import com.michaelflisar.composedialogs.core.rememberDialogState
import com.michaelflisar.composedialogs.demo.DemoDialogButton
import com.michaelflisar.composedialogs.demo.DemoDialogRegion
import com.michaelflisar.composedialogs.demo.DemoDialogRow
import com.michaelflisar.composedialogs.demo.showToast
import com.michaelflisar.composedialogs.dialogs.billing.DialogBilling
import com.michaelflisar.kotbilling.classes.Product
import com.michaelflisar.kotbilling.classes.ProductType

@Composable
fun BillingDemos(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {
    DemoDialogRegion("Billing Dialogs")
    DemoDialogRow {
        DemoDialogBilling(style, icon)
    }
}

@Composable
private fun RowScope.DemoDialogBilling(style: ComposeDialogStyle, icon: (@Composable () -> Unit)?) {
    val context = LocalContext.current
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
                context.showToast("Event $event")
            }
        )
    }
    DemoDialogButton(
        state,
        Icons.Default.Info,
        "Simple Billing Dialog",
        "Shows a list with prices (and purchase state if already owned) of items to buy (this will only return a non empty result in a real app with products!)"
    )
}