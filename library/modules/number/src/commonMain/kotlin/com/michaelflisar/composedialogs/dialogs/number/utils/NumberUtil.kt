package com.michaelflisar.composedialogs.dialogs.number.utils

internal object NumberUtil {

    fun <T : Number> sum(value1: T, value2: T): T {
        return when (value1) {
            is Int -> value1.toInt() + value2.toInt()
            is Double -> value1.toDouble() + value2.toDouble()
            is Float -> value1.toFloat() + value2.toFloat()
            is Long -> value1.toLong() + value2.toLong()
            else -> throw RuntimeException("T not supported!")
        } as T
    }

    fun <T : Number> min(value1: T, value2: T): T {
        return when (value1) {
            is Int -> value1.toInt() - value2.toInt()
            is Double -> value1.toDouble() - value2.toDouble()
            is Float -> value1.toFloat() - value2.toFloat()
            is Long -> value1.toLong() - value2.toLong()
            else -> throw RuntimeException("T not supported!")
        } as T
    }

    fun <T : Number> isLess(value1: T, value2: T): Boolean {
        return when (value1) {
            is Int -> value1.toInt() < value2.toInt()
            is Double -> value1.toDouble() < value2.toDouble()
            is Float -> value1.toFloat() < value2.toFloat()
            is Long -> value1.toLong() < value2.toLong()
            else -> throw RuntimeException("T not supported!")
        }
    }

    fun <T : Number> isMore(value1: T, value2: T): Boolean {
        return when (value1) {
            is Int -> value1.toInt() > value2.toInt()
            is Double -> value1.toDouble() > value2.toDouble()
            is Float -> value1.toFloat() > value2.toFloat()
            is Long -> value1.toLong() > value2.toLong()
            else -> throw RuntimeException("T not supported!")
        }
    }
}