package com.morse.core.coordinator

import android.app.Activity
import android.content.Intent
import android.os.Bundle

interface Direction {
    val clearBackStack: Boolean
    fun navigate(current: Activity, bundle: Bundle = Bundle.EMPTY): Unit? = null
}

