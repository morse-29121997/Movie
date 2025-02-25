package com.morse.details.coordinator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.morse.core.coordinator.Direction
import com.morse.details.ui.MovieDetailActivity

object MovieDetailsDirection : Direction {
    val movieKey = "Clicked-Movie-Key"
    override val clearBackStack: Boolean
        get() = false

    override fun navigate(current: Activity, bundle: Bundle) = run {
        if (clearBackStack)
            current.finish()
        current.startActivity(Intent(current, MovieDetailActivity::class.java).apply {
            putExtras(bundle)
        } , bundle)
    }
}