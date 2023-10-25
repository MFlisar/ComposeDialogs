package com.michaelflisar.composedialogs.dialogs.billing

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButtons
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogStyle
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.kotbilling.KotBilling
import com.michaelflisar.kotbilling.classes.Product
import com.michaelflisar.kotbilling.classes.ProductType
import com.michaelflisar.kotbilling.classes.ProductWithDetails
import com.michaelflisar.kotbilling.results.IKBProductResult
import com.michaelflisar.kotbilling.results.IKBPurchaseQueryResult
import com.michaelflisar.kotbilling.results.KBError
import com.michaelflisar.kotbilling.results.KBProductDetailsList
import com.michaelflisar.kotbilling.results.KBPurchaseQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Shows a dialog with prices of purchaseable products => clicking an item will launch the buy process
 *
 * &nbsp;
 *
 * **Basic Parameters:** all params not described here are derived from [Dialog], check it out for more details
 *
 * @param products the products to show inside the list
 * @param textAlreadyOwned the text that is shown if a product is already owned
 */
@Composable
fun DialogBilling(
    state: DialogState,
    // custom settings
    products: List<DialogBilling.BillingProduct>,
    textAlreadyOwned: String = "Item already owned!",
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: DialogStyle = DialogDefaults.styleDialog(),
    buttons: DialogButtons = DialogDefaults.buttons(),
    options: Options = Options(),
    onEvent: (event: DialogEvent) -> Unit = {}
) {
    var result by remember { mutableStateOf<LoadedData?>(null) }
    LaunchedEffect(products) {
        withContext(Dispatchers.IO) {
            val products = KotBilling.queryProducts(products.map { it.product })
            val purchasesInApp = KotBilling.queryPurchases(ProductType.InApp)
            val purchasesSubscription = KotBilling.queryPurchases(ProductType.Subscription)
            result = LoadedData(products, purchasesInApp, purchasesSubscription)
        }
    }

    Dialog(state, title, icon, style, buttons, options, onEvent = onEvent) {
        val r = result
        if (r == null) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (r.hasError()) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Error")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                val result = r.products as KBProductDetailsList

                result.details.forEach {
                    item {
                        val billingProduct = products.find { p -> p.product == it.product }!!
                        Item(billingProduct, it, r, textAlreadyOwned, state)
                    }
                }
            }
        }
    }
}

@Stable
object DialogBilling {

    /**
     * class holding data of product
     *
     * @param product the [KotBilling] product
     * @parm an optional icon for this product
     */
    data class BillingProduct(
        val product: Product,
        val icon: (@Composable () -> Unit)? = null
    )
}

@Composable
private fun Item(
    billingProduct: DialogBilling.BillingProduct,
    product: ProductWithDetails,
    loadedData: LoadedData,
    textAlreadyOwned: String,
    state: DialogState
) {
    val owned = isOwned(
        product.product,
        loadedData.purchasesInApp,
        loadedData.purchasesSubscription
    ) == true

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier.then(
            if (owned) Modifier else Modifier.clickable {
                scope.launch(Dispatchers.IO) {
                    KotBilling.purchase(context.findActivity(), product.product, null)
                    withContext(Dispatchers.Main) {
                        state.dismiss()
                    }
                }
            }
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            billingProduct.icon?.invoke()
            Text(product.details.singlePrice ?: "", style = MaterialTheme.typography.bodyMedium, fontWeight =  FontWeight.Bold)
            Text(product.details.name, style = MaterialTheme.typography.bodyMedium)

        }
        if (owned) {
            Text(textAlreadyOwned)
        }
    }
}

private fun isOwned(
    product: Product,
    purchasesInApp: IKBPurchaseQueryResult,
    purchasesSubscription: IKBPurchaseQueryResult
): Boolean? {
    val resultToCheck = when (product.type) {
        ProductType.InApp -> purchasesInApp
        ProductType.Subscription -> purchasesSubscription
    }
    return when (resultToCheck) {
        is KBError -> null
        is KBPurchaseQuery -> resultToCheck.details.find { it.products.contains(product.id) }?.isOwned
    }
}

/*
 * same as https://github.com/google/accompanist/blob/a9506584939ed9c79890adaaeb58de01ed0bb823/permissions/src/main/java/com/google/accompanist/permissions/PermissionsUtil.kt#L132
 */
internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("DialogBilling should be called in the context of an Activity")
}

private class LoadedData(
    val products: IKBProductResult,
    val purchasesInApp: IKBPurchaseQueryResult,
    val purchasesSubscription: IKBPurchaseQueryResult
) {
    fun hasError() =
        products is KBError || purchasesInApp is KBError || purchasesSubscription is KBError
}