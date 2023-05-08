package com.mashup.feature.danggn.data

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

class ShakeDetector @Inject constructor(
    private val sensorManager: SensorManager,
) {

    private var lastUpdateTime: Long = 0
    private var lastShakeTime: Long = 0

    private var shakeListener: (() -> Unit)? = null

    private var shakeIntervalTime: Long = DEFAULT_SHAKE_INTERVAL_TIME
    private var shakeThreshold: Float = DEFAULT_SHAKE_THRESHOLD

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            val currentTime = System.currentTimeMillis()
            val timeDifference = currentTime - lastUpdateTime
            if (event != null && timeDifference > shakeIntervalTime) {
                lastUpdateTime = currentTime
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val speed = sqrt(x.pow(2)) + sqrt(y.pow(2)) + sqrt(z.pow(2))

                if (speed > shakeThreshold) {
                    lastShakeTime = currentTime
                    shakeListener?.invoke()
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }
    }

    fun startListening(
        threshold: Float = DEFAULT_SHAKE_THRESHOLD,
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
        private const val DEFAULT_SHAKE_THRESHOLD = 3.0f

    }
}