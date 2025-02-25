package com.morse.feature_view_movies.coordinator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.morse.core.coordinator.Direction
import com.morse.feature_view_movies.ui.MoviesListActivity


object MoviesDirection : Direction {
    override val clearBackStack: Boolean
        get() = true

    override fun navigate(current: Activity, bundle: Bundle) = run {
        current.startActivity(Intent(current, MoviesListActivity::class.java), bundle)
        if (clearBackStack)
            current.finish()
    }

}