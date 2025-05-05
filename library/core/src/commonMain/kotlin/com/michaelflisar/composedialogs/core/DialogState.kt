package com.michaelflisar.composedialogs.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable

// Data Types:
// BaseBundle
//   * Boolean, Int, Long, Double, String
//   * BooleanArray, IntArray, LongArray, DoubleArray, StringArray
// Bundle
//   * Byte, Char, Short, Float, CharSequence, Parcelable, Size, SizeF
//   * ParcelableArray, ParcelableArrayList, SparseParcelableArray
//   * IntegerArrayList, StringArrayList, CharSequenceArrayList
//   * Serializable
//   * ByteArray, ShortArray, CharArray, FloatArray, CharSequenceArray
//   * Bundle

// ------------------------
// Custom Data - here the user MUST provide a Saver
// ------------------------

/*
 be aware that T must be saveable by autoSaver or you must provide a custom saver!
 supported types by autoSaver (all data types that are supported by Bundle):
   - Boolean, Int, Long, Double, String
   - BooleanArray, IntArray, LongArray, DoubleArray, StringArray
   - Byte, Char, Short, Float, CharSequence, Parcelable, Size, SizeF
   - ParcelableArray, ParcelableArrayList, SparseParcelableArray
   - IntegerArrayList, StringArrayList, CharSequenceArrayList
   - Serializable
   - ByteArray, ShortArray, CharArray, FloatArray, CharSequenceArray
   - Bundle
 */


/**
 * returns a [DialogStateWithData] object holding all necessary states for the dialog
 *
 * Consider using [rememberDialogState(showing: Boolean, ...)] if you do not need any data for this dialog
 *
 * @param data the initial data of this state (should nearly always be null which means that the dialog is not visible initially)
 * @param saver the [Saver] for the holded data ([autoSaver] will be used by default which will work for all types that are supported by [Bundle])
 * @param buttonPositiveEnabled define if the positive button should be enabled initially or not
 * @param buttonNegativeEnabled define if the negative button should be enabled initially or not
 * @param dismissAllowed define if the dialog can be initially dismissed or not
 * @param swipeAllowed define if the dialog can be initially swiped away or not
 */
@Composable
fun <T : Any> rememberDialogState(
    data: T?,
    saver: Saver<MutableState<T?>, out Any> = autoSaver(),
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
): DialogStateWithData<T> {

    // extra data - should survice screen rotations and activity recreates BUT must be reset if dialog is dismissed
    val state = rememberSaveable(saver = saver) { mutableStateOf(data) }

    // showing should survive, even screen rotations and activity recreations
    val showing = remember(state.value) {
        derivedStateOf { state.value != null }
    }

    // interaction state should be reset whenever the dialog is reshown
    val interaction = remember(showing.value) {
        mutableStateOf(
            DialogInteractionSource(
                buttonPositiveEnabled = mutableStateOf(buttonPositiveEnabled),
                buttonNegativeEnabled = mutableStateOf(buttonNegativeEnabled),
                dismissAllowed = mutableStateOf(dismissAllowed),
                swipeAllowed = mutableStateOf(swipeAllowed)
            )
        )
    }

    return DialogStateWithData(showing, state, interaction)
}

// ------------------------
// Simple - NO data
// ------------------------

/**
 * returns a [DialogStateNoData] object holding all necessary states for the dialog
 *
 * Consider using [rememberDialogState] with the data field overload if you do need some data inside the dialog if it is shown
 *
 * @param visible the initial visibility state
 * @param buttonPositiveEnabled define if the positive button should be enabled initially or not
 * @param buttonNegativeEnabled define if the negative button should be enabled initially or not
 * @param dismissAllowed define if the dialog can be initially dismissed or not
 * @param swipeAllowed define if the dialog can be initially swiped away or not
 */
@Composable
fun rememberDialogState(
    visible: Boolean = false,
    buttonPositiveEnabled: Boolean = true,
    buttonNegativeEnabled: Boolean = true,
    dismissAllowed: Boolean = true,
    swipeAllowed: Boolean = true
): DialogStateNoData {

    // showing should survive, even screen rotations and activity recreations
    val visible = rememberSaveable { mutableStateOf(visible) }

    // interaction state should be reset whenever the dialog is reshown
    val interaction = remember(visible.value) {
        mutableStateOf(
            DialogInteractionSource(
                buttonPositiveEnabled = mutableStateOf(buttonPositiveEnabled),
                buttonNegativeEnabled = mutableStateOf(buttonNegativeEnabled),
                dismissAllowed = mutableStateOf(dismissAllowed),
                swipeAllowed = mutableStateOf(swipeAllowed)
            )
        )
    }
    return DialogStateNoData(visible, interaction)
}