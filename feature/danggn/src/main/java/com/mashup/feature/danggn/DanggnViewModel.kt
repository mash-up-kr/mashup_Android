package com.mashup.feature.danggn

import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.shake.ShakeDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DanggnViewModel @Inject constructor(
    private val shakeDetector: ShakeDetector
) : BaseViewModel() {

    fun subscribeShakeSensor() {
        shakeDetector.startListening(
            interval = DANGGN_SHAKE_INTERVAL_TIME,
            threshold = DANGGN_SHAKE_THRESHOLD,
            onShakeDevice = {

            }
        )
    }

    override fun handleErrorCode(code: String) {
    }

    override fun onCleared() {
        super.onCleared()
        shakeDetector.stopListening()
    }

    companion object {
        private const val DANGGN_SHAKE_INTERVAL_TIME = 100L
        private const val DANGGN_SHAKE_THRESHOLD = 5000
    }
}
