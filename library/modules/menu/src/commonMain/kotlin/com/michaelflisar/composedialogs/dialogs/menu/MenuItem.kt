package com.michaelflisar.composedialogs.dialogs.menu

import androidx.compose.runtime.Composable

sealed class MenuItem {

    internal object BackMenu

    data class Item(
        val title: String,
        val description: String? = null,
        val dismissOnClick: Boolean = true,
        val icon: @Composable (() -> Unit)? = null,
        val onClick: () -> Unit
    ) : MenuItem()

    data class SubMenu(
        val title: String,
        val description: String? = null,
        val icon: @Composable (() -> Unit)? = null,
        val items: List<MenuItem>,
    ) : MenuItem()

    data object Divider : MenuItem()

    class Custom(
        val content: @Composable () -> Unit,
    ) : MenuItem()

}