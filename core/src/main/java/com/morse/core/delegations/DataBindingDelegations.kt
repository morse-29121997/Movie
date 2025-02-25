package com.morse.core.delegations

import androidx.annotation.Nullable
import androidx.databinding.ViewDataBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
/* This will throw exception , because the layout inflator will throw an exception if the reference
is nullable and it will be null when we call it on not suit time */

/* 3 - Used When we want to pass some variable or lifecycle variable to datainding*/
class lazyDataBinding<DataBindingType : ViewDataBinding>(
    private val createBinding: () -> DataBindingType
) : ReadWriteProperty<Any, DataBindingType?> {

    var binding: DataBindingType? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): DataBindingType {
        return binding ?: createBinding.invoke()
            .also { binding = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, @Nullable value: DataBindingType?) {
        binding = value
    }
}

