package com.morse.core.activity

import android.view.WindowManager
import androidx.activity.R
import androidx.activity.enableEdgeToEdge
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.morse.core.delegations.activityAutoDestroyedDataBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseDataBindingActivity<B : ViewDataBinding> constructor(@LayoutRes layoutRes: Int) :
    AppCompatActivity() {

    internal var _binding: B? by activityAutoDestroyedDataBinding(
        this,
        layoutRes
    )

    val binding: B by lazy { _binding!! }

    override fun onStart() {
        super.onStart()
        binding.run {
            configureActions()
        }
    }

    abstract fun configureActions()

}


inline fun <T> AppCompatActivity.collect(flow: Flow<T>, crossinline block: (T) -> Unit) =
    lifecycleScope.launch {
        flow
            .flowWithLifecycle(this@collect.lifecycle, minActiveState = Lifecycle.State.RESUMED)
            .collect { action ->
                block.invoke(action)
            }
    }
