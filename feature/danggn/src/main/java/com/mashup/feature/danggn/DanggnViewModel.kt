package com.mashup.feature.danggn

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.common.constant.UNAUTHORIZED
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.feature.danggn.data.danggn.DanggnGameController
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
    private val danggnGameController: DanggnGameController,
    private val danggnRepository: DanggnRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<DanggnUiState>(DanggnUiState.Loading)
    val uiState: StateFlow<DanggnUiState> = _uiState.asStateFlow()

    private val _randomMessage = MutableStateFlow("")
    val randomMessage: StateFlow<String> = _randomMessage.asStateFlow()

    init {
        collectDanggnState()
        getDanggnRandomTodayMessage()
    }

    fun subscribeShakeSensor() = mashUpScope {
        val result = danggnRepository.getGoldDanggnPercent()
        if (result.isSuccess()) {
            danggnGameController.start(
                threshold = DANGGN_SHAKE_THRESHOLD,
                interval = DANGGN_SHAKE_INTERVAL_TIME,
                goldenDanggnPercent = result.data?.goldenDanggnPercent
                    ?: DEFAULT_GOLD_DANGGN_PERCENT
            )
        }
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
        danggnGameController.stop()
    }

    private fun collectDanggnState() {
        viewModelScope.launch {
            danggnGameController.getDanggnShakeState()
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

    private fun getDanggnRandomTodayMessage() = mashUpScope {
        val response = danggnRepository.getDanggnRandomTodayMessage()
        val defaultMessage = "힘들면 당근 흔들어잇!"

        if (response.isSuccess()) {
            _randomMessage.value = response.data?.todayMessage ?: defaultMessage
        } else {
            // 서버 에러시 기본 메시지
            _randomMessage.value = defaultMessage
        }
    }

    companion object {
        private const val DANGGN_SHAKE_INTERVAL_TIME = 200L
        private const val DANGGN_SHAKE_THRESHOLD = 200
        private const val DEFAULT_GOLD_DANGGN_PERCENT = 1
    }
}

sealed interface DanggnUiState {
    object Loading : DanggnUiState
    object Success : DanggnUiState

    data class Error(val code: String) : DanggnUiState
}

