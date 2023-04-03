package com.mashup.danggn

import com.mashup.shake.ShakeDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DanggnViewModel @Inject constructor(
    private val shakeDetector: ShakeDetector
) {

}
