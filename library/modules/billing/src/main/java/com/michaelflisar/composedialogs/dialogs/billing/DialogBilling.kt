package com.michaelflisar.composedialogs.dialogs.billing

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedialogs.core.Dialog
import com.michaelflisar.composedialogs.core.DialogButton
import com.michaelflisar.composedialogs.core.DialogButtonType
import com.michaelflisar.composedialogs.core.DialogDefaults
import com.michaelflisar.composedialogs.core.DialogEvent
import com.michaelflisar.composedialogs.core.DialogState
import com.michaelflisar.composedialogs.core.DialogUtil
import com.michaelflisar.composedialogs.core.Options
import com.michaelflisar.composedialogs.core.defaultDialogStyle
import com.michaelflisar.composedialogs.core.style.ComposeDialogStyle
import com.michaelflisar.kotbilling.KotBilling
import com.michaelflisar.kotbilling.classes.Product
import com.michaelflisar.kotbilling.classes.ProductType
import com.michaelflisar.kotbilling.results.IKBProductResult
import com.michaelflisar.kotbilling.results.IKBPurchaseQueryResult
import com.michaelflisar.kotbilling.results.IKBPurchaseResult
import com.michaelflisar.kotbilling.results.KBError
import com.michaelflisar.kotbilling.results.KBProductDetailsList
import com.michaelflisar.kotbilling.results.KBPurchase
import com.michaelflisar.kotbilling.results.KBPurchaseQuery
import kotlinx.coroutines.CoroutineScope
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
    texts: DialogBilling.Texts = DialogBillingDefaults.texts(),
    // Base Dialog - Optional
    title: (@Composable () -> Unit)? = null,
    icon: (@Composable () -> Unit)? = null,
    style: ComposeDialogStyle = DialogDefaults.defaultDialogStyle(),
    //buttons: DialogButtons = DialogDefaults.buttons(),
    //options: Options = Options(),
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
        state.enableButton(DialogButtonType.Positive, false)
    }

    val buttonPositive =
        DialogButton(if (products.size == 1) texts.buttonBuySingle else stringResource(android.R.string.ok))
    val buttonPositiveBack = DialogButton(texts.buttonBack)
    val buttonNegative = DialogButton(stringResource(android.R.string.cancel))
    val buttons1 = DialogDefaults.buttons(buttonPositive, buttonNegative)
    val buttons2 = DialogDefaults.buttons(buttonPositiveBack, buttonNegative)

    var buttons by remember {
        mutableStateOf(buttons1)
    }

    val options = Options(
        dismissOnButtonClick = false
    )

    val purchasing = remember { mutableStateOf<Purchasing>(Purchasing.None) }
    LaunchedEffect(purchasing.value) {
        if (products.size == 1) {
            if (purchasing.value is Purchasing.Done) {
                buttons = buttons2
            } else buttons = buttons1
        }
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Dialog(state, title, icon, style, buttons, options, onEvent = {
        var handled = false
        if (it.isPositiveButton) {
            if (purchasing.value is Purchasing.None && products.size == 1) {
                buy(context, scope, products.first().product, purchasing)
                handled = true
            } else if (purchasing.value is Purchasing.Done) {
                purchasing.value = Purchasing.None
                handled = true
            }
        }
        if (!handled) {
            onEvent(it)
            state.dismiss()
        }
    }) {
        BackHandler(enabled = purchasing.value is Purchasing.Done) {
            purchasing.value = Purchasing.None
        }

        val r = result
        val p = purchasing.value

        var showInfo = false
        var showLoading = false
        var showEmpty = false
        var purchaseInfoInfo: IKBPurchaseResult? = null
        var productErrors: List<KBError> = emptyList()
        var items: List<ItemData> = emptyList()

        // handle all possible states
        // 1) purchase is in progress
        if (p == Purchasing.Pending) {
            showLoading = true
            showInfo = true
        }
        // 2) purchase is done
        else if (p is Purchasing.Done) {
            purchaseInfoInfo = p.result
        }
        // 3) laoding products in progress
        else if (r == null) {
            showLoading = true
            showInfo = true
        }
        // 4) loading products failed
        else if (r.getErrors().isNotEmpty()) {
            productErrors = r.getErrors()
        }
        // 5) loading prodcuts succeeded
        else {
            showInfo = true

            val details = (r.products as KBProductDetailsList).details
            if (details.isEmpty() && DialogBilling.TESTING) {
                items = products.mapIndexed { index, billingProduct ->
                    val name = billingProduct.product.id.toString()
                    val singlePrice = "${index + 1}.0€"
                    val owned = index % 2 == 1
                    ItemData(
                        billingProduct,
                        billingProduct.product,
                        name,
                        singlePrice,
                        owned,
                        texts.textItemAlreadyOwned,
                        purchasing
                    )
                }
            } else if (details.isEmpty()) {
                showEmpty = true
            } else {
                items = details.map {
                    val billingProduct =
                        products.find { p -> p.product == it.product }!!
                    val owned = isOwned(
                        it.product,
                        r.purchasesInApp,
                        r.purchasesSubscription
                    ) == true
                    ItemData(
                        billingProduct,
                        it.product,
                        it.details.name,
                        it.details.singlePrice,
                        owned,
                        texts.textItemAlreadyOwned,
                        purchasing
                    )
                }
            }
        }

        // ----------
        // UI
        // ----------

        if (showInfo) {
            Text(texts.info)
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (showLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        if (productErrors.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    texts.textErrorQueringProducts,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                productErrors.forEach {
                    Text(it.toString(), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        if (purchaseInfoInfo != null) {
            Column(modifier = Modifier.fillMaxWidth()) {
                when (purchaseInfoInfo) {
                    is KBError -> {
                        Text(
                            "Purchase Error", // TODO text
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            purchaseInfoInfo.toString(),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    is KBPurchase -> {
                        Text(
                            "Purchase Done",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        if (showEmpty) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = texts.textNoProductFound,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
        } else {
            state.enableButton(DialogButtonType.Positive, true)
            LazyColumn(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                items.forEach {
                    item { Item(it) }
                }
            }
        }
    }
}

@Stable
object DialogBilling {

    internal val TESTING = false

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

    /**
     * see [DialogBilling.texts]
     */
    @Immutable
    class Texts internal constructor(
        val info: String,
        val buttonBuySingle: String,
        val buttonBack: String,
        val textItemAlreadyOwned: String,
        val textSingleAlreadyOwned: String,
        val textNoProductFound: String,
        val textErrorQueringProducts: String
    )
}

@Stable
object DialogBillingDefaults {

    /**
     * texts for the billing dialog
     *
     * @param info the main information text of the dialog
     * @param buttonBuySingle if the dialog shows a single product only it will show a button to buy this single item - this is the label of this button
     * @param textItemAlreadyOwned if the dialog shows more than one item, this will be the text that will be shown to tell the user that an item in the list is already owned
     * @param textSingleAlreadyOwned if the dialog shows a single product only this is the text that will be shown to the user if the product is already owned
     * @param textNoProductFound if products cant be loaded this is the text the user will see
     */
    @Composable
    fun texts(
        info: String = stringResource(R.string.composedialogs_billing_pro_version_info),
        buttonBuySingle: String = stringResource(R.string.composedialogs_billing_button_buy),
        buttonBack: String = stringResource(R.string.composedialogs_billing_button_back),
        textItemAlreadyOwned: String = stringResource(R.string.composedialogs_billing_info_item_already_owned),
        textSingleAlreadyOwned: String = stringResource(R.string.composedialogs_billing_info_pro_version_already_owned),
        textNoProductFound: String = stringResource(R.string.composedialogs_billing_info_no_products_found),
        textErrorQueringProducts: String = stringResource(R.string.composedialogs_billing_error_quering_products)
    ): DialogBilling.Texts {
        return DialogBilling.Texts(
            info = info,
            buttonBuySingle = buttonBuySingle,
            buttonBack = buttonBack,
            textItemAlreadyOwned = textItemAlreadyOwned,
            textSingleAlreadyOwned = textSingleAlreadyOwned,
            textNoProductFound = textNoProductFound,
            textErrorQueringProducts = textErrorQueringProducts
        )
    }
}

@Composable
private fun Item(item: ItemData) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .then(
                if (item.owned) {
                    Modifier
                } else Modifier.clickable {
                    buy(context, scope, item.product, item.purchasing)
                }
            )
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item.billingProduct.icon?.invoke()
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        item.singlePrice ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(item.name, style = MaterialTheme.typography.bodyMedium)
                }
                if (item.owned) {
                    Text(
                        item.textAlreadyOwned,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}

private fun buy(
    context: Context,
    scope: CoroutineScope,
    product: Product,
    purchasing: MutableState<Purchasing>
) {
    purchasing.value = Purchasing.Pending
    scope.launch(Dispatchers.IO) {
        val result = KotBilling.purchase(DialogUtil.findActivity(context), product, null)
        purchasing.value = Purchasing.Done(result)
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

private class LoadedData(
    val products: IKBProductResult,
    val purchasesInApp: IKBPurchaseQueryResult,
    val purchasesSubscription: IKBPurchaseQueryResult
) {
    fun getErrors() =
        listOf(products, purchasesInApp, purchasesSubscription).filterIsInstance<KBError>()
}

private sealed class Purchasing {
    data object None : Purchasing()
    data object Pending : Purchasing()
    class Done(val result: IKBPurchaseResult) : Purchasing()
}

private class ItemData(
    val billingProduct: DialogBilling.BillingProduct,
    val product: Product,
    val name: String,
    val singlePrice: String?,
    val owned: Boolean,
    val textAlreadyOwned: String,
    val purchasing: MutableState<Purchasing>
)