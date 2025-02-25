package com.morse.moviesmagic.launcher

import android.annotation.SuppressLint
import androidx.lifecycle.lifecycleScope
import com.morse.core.activity.BaseDataBindingActivity
import com.morse.feature_view_movies.coordinator.MoviesDirection
import com.morse.moviesmagic.R
import com.morse.moviesmagic.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseDataBindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    override fun configureActions() {
        openAfter3Seconds()
    }

    private fun openAfter3Seconds() {
        lifecycleScope.launch {
            delay(3_000)
            MoviesDirection.navigate(this@SplashActivity)
        }
    }
}