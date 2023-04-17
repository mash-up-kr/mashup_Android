package com.mashup.feature.danggn

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.feature.danggn.data.DanggnShaker
import com.mashup.feature.danggn.data.DanggnShakerState
import com.mashup.feature.danggn.data.dto.DanggnScoreRequest
import com.mashup.feature.danggn.data.repository.DanggnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DanggnViewModel @Inject constructor(
    private val danggnShaker: DanggnShaker,
    private val danggnRepository: DanggnRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) : BaseViewModel() {

    private val _danggnState = MutableStateFlow<DanggnShakerState>(DanggnShakerState.Idle)
    val danggnState: StateFlow<DanggnShakerState> = _danggnState.asStateFlow()

    init {
        collectDanggnState()
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

    private fun collectDanggnState() {
        viewModelScope.launch {
            danggnShaker.getDanggnShakeState()
                .collect {
                    when (it) {
                        is DanggnShakerState.End -> {
                            sendDanggnScore(it)
                        }
                        else -> {
                            _danggnState.emit(it)
                        }
                    }
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
