package com.mashup.feature.danggn.data

import android.hardware.SensorManager
import com.mashup.core.shake.ShakeDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

/**
 * ShakeDetector의 shake 이벤트를 탐지하여 콤보 카운트를 계산합니다.
 */
class DanggnShaker @Inject constructor(
    private val sensorManager: SensorManager,
    private val shakeDetector: ShakeDetector
) {
    private val shakerStateChannel = Channel<DanggnShakerState>(Channel.UNLIMITED)
    private val comboCountChannel = Channel<Int>(Channel.UNLIMITED)

    private var danggnShakerScope: CoroutineScope? = null
    private var debounceDetectorJob: Job? = null

    private var lastShakeTime: Long = 0
    private var comboCount = AtomicInteger()

    fun start() {
        danggnShakerScope = CoroutineScope(Dispatchers.Default)
        collectComboFlow()
        shakeDetector.startListening(
            sensorManager = sensorManager,
            threshold = 0,
            interval = 0,
            onShakeDevice = {
                danggnShakerScope?.launch {
                    val comboCount = comboCount.incrementAndGet()
                    shakerStateChannel.send(
                        DanggnShakerState.Combo(score = comboCount)
                    )
                    comboCountChannel.send(comboCount)
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
            comboCountChannel.consumeAsFlow().debounce(COMBO_TERM_DURATION)
                .collectLatest {
                    val lastScore = comboCount.getAndSet(0)
                    shakerStateChannel.send(
                        DanggnShakerState.End(lastScore = lastScore)
                    )
                }
        }
    }

    private fun clearFlag() {
        lastShakeTime = 0
        comboCount.set(0)
    }

    companion object {
        private const val COMBO_TERM_DURATION = 2000L
    }
}

sealed interface DanggnShakerState {
    data class Combo(val score: Int) : DanggnShakerState
    data class End(val lastScore: Int) : DanggnShakerState
}