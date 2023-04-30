package com.mashup.feature.danggn.data.danggn

import com.mashup.feature.danggn.data.ShakeDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ShakeDetector의 shake 이벤트를 탐지하여 콤보 카운트를 계산합니다.
 */
class DanggnGameController @Inject constructor(
    private val shakeDetector: ShakeDetector,
    private val modeController: DanggnModeController,
    private val scoreController: DanggnScoreController
) {

    companion object {
        private const val DEFAULT_FRAME_RATE = 100L
    }

    private var danggnShakerScope: CoroutineScope? = null

    private var frameCallbackListener: ((DanggnGameState) -> Unit)? = null

    /**
     * DanggnComboCount 넘겨주는 Listener
     * @param comboCount 누적된 콤보 카운트
     */
    private var comboEndCallbackListener: ((comboCount: Int) -> Unit)? = null

    fun start(
        threshold: Int,
        interval: Long,
        goldenDanggnPercent: Int
    ) {
        danggnShakerScope = CoroutineScope(Dispatchers.Default)
        shakeDetector.startListening(
            threshold = threshold,
            interval = interval,
            onShakeDevice = this::onShakeDevice
        )
        modeController.setGoldDanggnPercent(goldenDanggnPercent)
        runDanggnGame()
    }

    private fun runDanggnGame() {
        danggnShakerScope?.launch {
            while (danggnShakerScope?.isActive == true) {
                modeController.checkDanggnMode()
                scoreController.checkDanggnScore()

                val lastComboScore = scoreController.getLastCombonScore()
                if (lastComboScore > 0) {
                    comboEndCallbackListener?.invoke(lastComboScore)
                }
                frameCallbackListener?.invoke(
                    DanggnGameState(
                        currentMode = modeController.getDanggnMode(),
                        danggnScoreModelList = scoreController.getDanggnScoreList()
                    )
                )
                delay(DEFAULT_FRAME_RATE)
            }
        }
    }

    fun setListener(
        frameCallbackListener: ((DanggnGameState) -> Unit),
        comboEndCallbackListener: ((comboCount: Int) -> Unit)
    ) {
        this.frameCallbackListener = frameCallbackListener
        this.comboEndCallbackListener = comboEndCallbackListener
    }

    fun stop() {
        danggnShakerScope?.cancel()
        shakeDetector.stopListening()
        modeController.reset()
        scoreController.reset()
    }

    private fun onShakeDevice() {
        modeController.switchToGoldenDanggnMode()

        val mode = modeController.getDanggnMode()
        scoreController.addScore(mode)
    }
}