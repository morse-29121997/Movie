package com.morse.core.binding_adapters

import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class FieldValidationInformation(
    val isValidate: Boolean,
    val currentData: String,
    @StringRes val errorMessage: Int
)

@BindingAdapter("validate")
fun TextInputLayout.error(data: Flow<FieldValidationInformation>) {
    data.onEach {
        if (it.isValidate) {
            error = null
        } else {
            error = resources.getString(it.errorMessage)
        }
    }.launchIn(CoroutineScope(Dispatchers.Main))
}
