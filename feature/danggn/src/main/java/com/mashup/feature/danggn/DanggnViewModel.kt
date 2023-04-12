package com.mashup.feature.danggn

import com.mashup.core.common.base.BaseViewModel
import com.mashup.feature.danggn.data.DanggnShaker
import com.mashup.feature.danggn.data.DanggnShakerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@HiltViewModel
class DanggnViewModel @Inject constructor(
    private val danggnShaker: DanggnShaker
) : BaseViewModel() {

    private val danggnState: Flow<DanggnShakerState> = danggnShaker.getDanggnShakeState()

    val danggnComboState = danggnShaker.getDanggnShakeState()
        .filter { it is DanggnShakerState.Combo }

    fun subscribeShakeSensor() {
        danggnShaker.start()
    }

    override fun handleErrorCode(code: String) {
    }

    override fun onCleared() {
        super.onCleared()
        danggnShaker.stop()
    }

    companion object {
        private const val DANGGN_SHAKE_INTERVAL_TIME = 100L
        private const val DANGGN_SHAKE_THRESHOLD = 5000
    }
}
