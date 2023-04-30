package com.mashup.feature.danggn.data.danggn

import com.mashup.feature.danggn.data.ShakeDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

/**
 * ShakeDetector의 shake 이벤트를 탐지하여 콤보 카운트를 계산합니다.
 */
class DanggnShaker @Inject constructor(
    private val shakeDetector: ShakeDetector
) {
    private val danggnModeChannel = Channel<DanggnMode>()
    private val shakerStateChannel = Channel<DanggnShakerState>()
    private val comboScoreChannel = Channel<Int>()

    private var danggnShakerScope: CoroutineScope? = null
    private var debounceDetectorJob: Job? = null

    private var lastShakeTime: Long = 0
    private var comboScore = AtomicInteger()

    private var danggnMode: DanggnMode = NormalDanggnMode

    fun start(
        threshold: Int,
        interval: Long,
        goldenDanggnPercent: Int
    ) {
        danggnShakerScope = CoroutineScope(Dispatchers.Default)
        collectComboFlow()
        runDanggnModeController()
        runComboScoreController(goldenDanggnPercent)
        shakeDetector.startListening(
            threshold = threshold,
            interval = interval,
            onShakeDevice = {
                danggnShakerScope?.launch {
                    val newComboScore = comboScore.updateAndGet(danggnMode::getNextScore)
                    shakerStateChannel.send(
                        DanggnShakerState.Combo(
                            mode = danggnMode,
                            score = newComboScore
                        )
                    )
                    comboScoreChannel.send(newComboScore)
                }
            }
        )
    }

    fun getDanggnShakeState(): Flow<DanggnShakerState> = shakerStateChannel.consumeAsFlow()

    fun stop() {
        danggnShakerScope?.cancel()
        debounceDetectorJob?.cancel()
        shakeDetector.stopListening()
        clearFlag()
    }

    private fun collectComboFlow() {
        danggnShakerScope?.launch {
            comboScoreChannel.consumeAsFlow().debounce(COMBO_TERM_DURATION)
                .collectLatest {
                    val lastScore = comboScore.getAndSet(0)
                    shakerStateChannel.send(
                        DanggnShakerState.End(
                            mode = danggnMode,
                            lastScore = lastScore
                        )
                    )
                    shakerStateChannel.send(DanggnShakerState.Idle(mode = danggnMode))
                }
        }
    }

    private fun runComboScoreController(goldenDanggnPercent: Int) {
        danggnShakerScope?.launch {
            while (danggnShakerScope?.isActive == true) {
                comboScoreChannel.receive()
                if (
                    (danggnMode as? NormalDanggnMode)?.canSwitchToGoldenDanggnMode(
                        goldenDanggnPercent
                    ) == true
                ) {
                    danggnModeChannel.send(GoldenDanggnMode)
                }
            }
        }
    }

    private fun runDanggnModeController() {
        danggnShakerScope?.launch {
            while (danggnShakerScope?.isActive == true) {
                danggnMode = danggnModeChannel.receiveMode()

                if (danggnMode is GoldenDanggnMode) {
                    delay(TIME_GOLDEN_STAGE)
                    danggnModeChannel.send(NormalDanggnMode)
                }
            }
        }
    }

    private fun clearFlag() {
        lastShakeTime = 0
        comboScore.set(0)
    }

    private suspend fun Channel<DanggnMode>.receiveMode(): DanggnMode {
        return if (isEmpty) NormalDanggnMode else receive()
    }

    companion object {
        private const val COMBO_TERM_DURATION = 2000L
        private const val TIME_GOLDEN_STAGE = 3000L
    }
}

sealed interface DanggnShakerState {
    data class Idle(
        val mode: DanggnMode = NormalDanggnMode
    ) : DanggnShakerState

    data class Combo(
        val score: Int,
        val mode: DanggnMode = NormalDanggnMode
    ) : DanggnShakerState

    data class End(
        val lastScore: Int,
        val mode: DanggnMode = NormalDanggnMode
    ) : DanggnShakerState
}