package com.mashup.core.shake

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

class ShakeDetector @Inject constructor(
    private val sensorManager: SensorManager,
) {

    private val acceleration = FloatArray(3)
    private var lastAcceleration = FloatArray(3)
    private var lastUpdateTime: Long = 0
    private var lastShakeTime: Long = 0

    private var shakeListener: (() -> Unit)? = null

    private var shakeIntervalTime: Long = DEFAULT_SHAKE_INTERVAL_TIME
    private var shakeThreshold: Int = DEFAULT_SHAKE_THRESHOLD

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            val currentTime = System.currentTimeMillis()
            val timeDifference = currentTime - lastUpdateTime
            if (event != null && timeDifference > shakeIntervalTime) {
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
                        lastShakeTime = currentTime
                        shakeListener?.invoke()
                    }
                    Log.d("DanggnShake", "speed: $speed, timeDiff $timeDifference")
                }
                System.arraycopy(acceleration, 0, lastAcceleration, 0, acceleration.size)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }

    fun startListening(
        threshold: Int = DEFAULT_SHAKE_THRESHOLD,
        interval: Long = DEFAULT_SHAKE_INTERVAL_TIME,
        onShakeDevice: () -> Unit
    ) {
        shakeThreshold = threshold
        shakeIntervalTime = interval
        shakeListener = onShakeDevice

        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun stopListening() {
        shakeListener = null
        sensorManager.unregisterListener(sensorEventListener)
    }

    companion object {
        private const val DEFAULT_SHAKE_INTERVAL_TIME = 100L
        private const val DEFAULT_SHAKE_THRESHOLD = 200
    }
}