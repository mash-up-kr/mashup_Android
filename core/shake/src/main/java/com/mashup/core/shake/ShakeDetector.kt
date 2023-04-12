package com.mashup.core.shake

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.HandlerThread
import kotlin.math.pow
import kotlin.math.sqrt

class ShakeDetector : SensorEventListener {

    private val acceleration = FloatArray(3)
    private var lastAcceleration = FloatArray(3)
    private var lastUpdateTime: Long = 0

    private var sensorManager: SensorManager? = null

    private var shakeListener: (() -> Unit)? = null

    private var shakeIntervalTime: Long = SHAKE_INTERVAL_TIME
    private var shakeThreshold: Int = SHAKE_THRESHOLD

    // ShakeDetector를 Background에서 탐지하기 위한 변수
    private var sensorThread: HandlerThread? = null
    private var sensorHandler: Handler? = null

    fun startListening(
        sensorManager: SensorManager,
        threshold: Int = SHAKE_THRESHOLD,
        interval: Long = SHAKE_INTERVAL_TIME,
        onShakeDevice: () -> Unit
    ) {
        this.sensorManager = sensorManager
        shakeThreshold = threshold
        shakeIntervalTime = interval
        shakeListener = onShakeDevice

        sensorThread = HandlerThread("Sensor thread", Thread.MAX_PRIORITY).also { thread ->
            sensorHandler = Handler(thread.looper)
        }

        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(
            this,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL,
            sensorHandler
        )
    }

    fun stopListening() {
        shakeListener = null
        sensorManager?.unregisterListener(this)
        sensorThread?.quitSafely()
        sensorHandler = null
    }

    override fun onSensorChanged(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - lastUpdateTime
        if (timeDifference > shakeIntervalTime) {
            lastUpdateTime = currentTime
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            acceleration[0] = x
            acceleration[1] = y
            acceleration[2] = z
            if (lastAcceleration.isNotEmpty()) {
                val delta = acceleration.mapIndexed { index, value ->
                    value - lastAcceleration[index]
                }
                val speed = sqrt(
                    delta[0].pow(2) + delta[1].pow(2) + delta[2].pow(2)
                ) / timeDifference * 10000

                if (speed > shakeThreshold) {
                    shakeListener?.invoke()
                }
            }
            System.arraycopy(acceleration, 0, lastAcceleration, 0, acceleration.size)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    companion object {
        private const val SHAKE_INTERVAL_TIME = 100L
        private const val SHAKE_THRESHOLD = 5000
    }
}