package com.morse.core.binding_adapters

import android.view.View
import androidx.databinding.BindingAdapter

private fun View.isVisible() = this.visibility == View.VISIBLE

private fun View.isGone() = this.visibility == View.GONE

private fun View.visible() {
    if(isGone()) {
        this.visibility = View.VISIBLE
    }
}

private fun View.gone() {
    if (isVisible()) {
        this.visibility = View.GONE
    }
}

private fun View.visibleIf(boolean: Boolean) = if (boolean) visible() else gone()

@BindingAdapter("viewIf")
fun viewIf(view: View, boolean: Boolean) = view.visibleIf(boolean)

@BindingAdapter("hideIf")
fun hideIf(view: View, boolean: Boolean) = view.visibleIf(boolean.not())