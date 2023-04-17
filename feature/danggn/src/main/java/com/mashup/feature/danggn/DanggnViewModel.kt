package com.mashup.feature.danggn

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.feature.danggn.data.DanggnShaker
import com.mashup.feature.danggn.data.DanggnShakerState
import com.mashup.feature.danggn.data.dto.DanggnScoreRequest
import com.mashup.feature.danggn.data.repository.DanggnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DanggnViewModel @Inject constructor(
    private val danggnShaker: DanggnShaker,
    private val danggnRepository: DanggnRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) : BaseViewModel() {

    val danggnComboState: Flow<DanggnShakerState> = danggnShaker.getDanggnShakeState()
        .filter { it is DanggnShakerState.Combo }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            DanggnShakerState.Idle
        )

    init {
        sendDanggnScoreWhenComboEnd()
    }

    fun subscribeShakeSensor() {
        danggnShaker.start(
            threshold = DANGGN_SHAKE_THRESHOLD,
            interval = DANGGN_SHAKE_INTERVAL_TIME,
        )
    }

    override fun handleErrorCode(code: String) {
    }

    override fun onCleared() {
        super.onCleared()
        danggnShaker.stop()
    }

    private fun sendDanggnScoreWhenComboEnd() {
        viewModelScope.launch {
            danggnShaker.getDanggnShakeState()
                .filter { it is DanggnShakerState.End }
                .collect {
                    sendDanggnScore(it as DanggnShakerState.End)
                }
        }
    }

    private fun sendDanggnScore(
        danggnShakerState: DanggnShakerState.End
    ) = mashUpScope {
        val generateNumber =
            userPreferenceRepository.getUserPreference().first().generationNumbers.last()

        danggnRepository.postDanggnScore(
            generationNumber = generateNumber,
            scoreRequest = DanggnScoreRequest(danggnShakerState.lastScore)
        )
    }

    companion object {
        private const val DANGGN_SHAKE_INTERVAL_TIME = 200L
        private const val DANGGN_SHAKE_THRESHOLD = 200
    }
}
