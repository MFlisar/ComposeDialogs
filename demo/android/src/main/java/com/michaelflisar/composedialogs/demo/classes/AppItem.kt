package com.michaelflisar.composedialogs.demo.classes

import android.content.Context
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppItem(
    val id: Int,
    val resolveInfo: ResolveInfo,
    val label: String,
    //val icon: Drawable
) : Parcelable {
    fun icon(context: Context) =resolveInfo.loadIcon(context.packageManager)
}