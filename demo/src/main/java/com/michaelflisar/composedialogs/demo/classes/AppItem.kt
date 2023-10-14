package com.michaelflisar.composedialogs.demo.classes

import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

data class AppItem(
    val id: Int,
    val resolveInfo: ResolveInfo,
    val label: String,
    val icon: Drawable
)