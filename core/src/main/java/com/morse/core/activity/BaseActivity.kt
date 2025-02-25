package com.morse.core.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding

abstract class BaseActivity< B : ViewDataBinding > : AppCompatActivity() {

    private var binding: B? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindDataBinding()
    }

    override fun onStart() {
        super.onStart()
        configureActions()
    }


    abstract fun configureActions ()

    abstract fun bindDataBinding(): B

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}