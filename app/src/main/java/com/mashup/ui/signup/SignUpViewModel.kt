package com.mashup.ui.signup

import com.mashup.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : BaseViewModel() {
    private val _platform = MutableStateFlow("")
    val platform: StateFlow<String>
        get() = _platform

    fun setPlatform(platform: String) {
        _platform.value = platform
    }
}