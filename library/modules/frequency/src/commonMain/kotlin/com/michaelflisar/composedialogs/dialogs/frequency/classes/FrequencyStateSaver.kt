package com.michaelflisar.composedialogs.dialogs.frequency.classes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope

object FrequencyStateSaver : Saver<MutableState<Frequency>, String> {
    override fun restore(value: String): MutableState<Frequency> {
        return mutableStateOf(Frequency.deserialize(value))
    }

    override fun SaverScope.save(value: MutableState<Frequency>): String {
        return value.value.serialize()
    }
}
