package com.morse.core.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

interface Events

abstract  class BaseViewModel <State> () : ViewModel() {
    lateinit var state : MutableStateFlow<State>
    abstract fun onEvent (event : Events)
    abstract fun update (`do` : (State) -> State)
}