package com.michaelflisar.composedialogs.core

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

object DialogUtil {

    /*
    * same as https://github.com/google/accompanist/blob/a9506584939ed9c79890adaaeb58de01ed0bb823/permissions/src/main/java/com/google/accompanist/permissions/PermissionsUtil.kt#L132
    */
    fun findActivity(context: Context): Activity {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("This dialog should be called in the context of an Activity")
    }

}