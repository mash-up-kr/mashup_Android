package com.mashup.feature.danggn

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.common.constant.UNAUTHORIZED
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.feature.danggn.data.danggn.DanggnGameController
import com.mashup.feature.danggn.data.danggn.DanggnGameState
import com.mashup.feature.danggn.data.danggn.DanggnMode
import com.mashup.feature.danggn.data.danggn.GoldenDanggnMode
import com.mashup.feature.danggn.data.danggn.NormalDanggnMode
import com.mashup.feature.danggn.data.dto.DanggnScoreRequest
import com.mashup.feature.danggn.data.repository.DanggnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@HiltViewModel
class DanggnViewModel @Inject constructor(
    private val danggnGameController: DanggnGameController,
    private val danggnRepository: DanggnRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<DanggnUiState>(DanggnUiState.Loading)
    val uiState: StateFlow<DanggnUiState> = _uiState.asStateFlow()

    private val _danggnMode = MutableStateFlow<DanggnMode>(NormalDanggnMode)
    val danggnMode: StateFlow<DanggnMode> = _danggnMode.asStateFlow()

    private val _feverTimeCountDown = MutableStateFlow(0)
    val feverTimeCountDown: StateFlow<Int> = _feverTimeCountDown.asStateFlow()

    private val _randomMessage = MutableStateFlow("")
    val randomMessage: StateFlow<String> = _randomMessage.asStateFlow()

    private val _onSuccessAddScore = MutableSharedFlow<Unit>()
    val onSuccessAddScore: SharedFlow<Unit> = _onSuccessAddScore.asSharedFlow()

    private val _onShakeDevice = MutableSharedFlow<Unit>()
    val onShakeDevice: SharedFlow<Unit> = _onShakeDevice.asSharedFlow()


    init {
        initDanggnGame()
        getDanggnRandomTodayMessage()
    }

    private fun initDanggnGame() {
        danggnGameController.setListener(
            frameCallbackListener = {
                viewModelScope.launch {
                    _uiState.emit(DanggnUiState.Success(it))
                    _danggnMode.emit(it.currentMode)
                }
            },
            comboEndCallbackListener = this::sendDanggnScore,
            danggnModeChangedListener = this::countDownFeverTime,
            onShakeListener = {
                viewModelScope.launch {
                    _onShakeDevice.emit(Unit)
                }
            }
        )
    }

    fun startDanggnGame() = mashUpScope {
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

    private fun sendDanggnScore(comboScore: Int) = mashUpScope {
        val generateNumber =
            userPreferenceRepository.getUserPreference()
                .firstOrNull()?.generationNumbers?.lastOrNull()
        if (generateNumber != null) {
            danggnRepository.postDanggnScore(
                generationNumber = generateNumber,
                scoreRequest = DanggnScoreRequest(comboScore)
            )
            _onSuccessAddScore.emit(Unit)
        } else {
            handleErrorCode(UNAUTHORIZED)
        }
    }

    private fun countDownFeverTime(danggnMode: DanggnMode) {
        viewModelScope.launch {
            if (danggnMode is GoldenDanggnMode) {
                _feverTimeCountDown.value = FEVER_TIME_COUNT_DOWN

                repeat(FEVER_TIME_COUNT_DOWN) {
                    delay(1000)
                    _feverTimeCountDown.value = _feverTimeCountDown.value - 1
                }
            }
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
        private const val FEVER_TIME_COUNT_DOWN = 3
    }
}

sealed interface DanggnUiState {
    object Loading : DanggnUiState
    data class Success(
        val danggnGameState: DanggnGameState
    ) : DanggnUiState

    data class Error(val code: String) : DanggnUiState
}

