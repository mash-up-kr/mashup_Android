package com.mashup.feature.danggn

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.common.constant.UNAUTHORIZED
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.feature.danggn.data.danggn.DanggnShaker
import com.mashup.feature.danggn.data.danggn.DanggnShakerState
import com.mashup.feature.danggn.data.dto.DanggnScoreRequest
import com.mashup.feature.danggn.data.repository.DanggnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DanggnViewModel @Inject constructor(
    private val danggnShaker: DanggnShaker,
    private val danggnRepository: DanggnRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<DanggnUiState>(DanggnUiState.Loading)
    val uiState: StateFlow<DanggnUiState> = _uiState.asStateFlow()

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
        mashUpScope {
            when (code) {
                UNAUTHORIZED -> {
                    _uiState.emit(DanggnUiState.Error(code))
                }
            }
        }
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
                            _uiState.emit(DanggnUiState.Success)
                        }
                    }
                }
        }
    }

    private fun sendDanggnScore(
        danggnShakerState: DanggnShakerState.End
    ) = mashUpScope {
        val generateNumber =
            userPreferenceRepository.getUserPreference()
                .firstOrNull()?.generationNumbers?.lastOrNull()
        if (generateNumber != null) {
            danggnRepository.postDanggnScore(
                generationNumber = generateNumber,
                scoreRequest = DanggnScoreRequest(danggnShakerState.lastScore)
            )
        } else {
            handleErrorCode(UNAUTHORIZED)
        }
    }

    companion object {
        private const val DANGGN_SHAKE_INTERVAL_TIME = 200L
        private const val DANGGN_SHAKE_THRESHOLD = 200
    }
}

sealed interface DanggnUiState {
    object Loading : DanggnUiState
    object Success : DanggnUiState

    data class Error(val code: String) : DanggnUiState
}

