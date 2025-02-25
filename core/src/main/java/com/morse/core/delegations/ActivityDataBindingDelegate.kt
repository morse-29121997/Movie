package com.morse.core.delegations

import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.morse.core.activity.BaseDataBindingActivity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class activityDataBinding<DataBindingType : ViewDataBinding>(
    private val activity: AppCompatActivity,
    @LayoutRes private val layoutRes: Int
) : ReadWriteProperty<Any, DataBindingType?> {

    var binding: DataBindingType? = null

    override fun getValue(thisRef: Any, property: KProperty<*>): DataBindingType {
        return binding ?: DataBindingUtil.setContentView<DataBindingType>(activity, layoutRes)
            .also { binding = it }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: DataBindingType?) {
        binding = value
    }
}

/* We must make the activity with BaseActivity to can access bindingObject*/
@SuppressWarnings ("unused")
class activityAutoDestroyedDataBinding<DataBindingType : ViewDataBinding>(
    private val activity: BaseDataBindingActivity<DataBindingType>,
    @LayoutRes private val layoutRes: Int
) : ReadWriteProperty<Any, DataBindingType?> {

    var binding: DataBindingType? = null

    init {
        invokeOnDestroy()
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): DataBindingType {
        return binding ?: DataBindingUtil.setContentView<DataBindingType>(activity, layoutRes)
            .also { binding = it }
    }

    private fun invokeOnDestroy() {
        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                owner.lifecycle.removeObserver(this)
                activity._binding = null
            }
        })
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: DataBindingType?) {
        binding = value
    }
}
