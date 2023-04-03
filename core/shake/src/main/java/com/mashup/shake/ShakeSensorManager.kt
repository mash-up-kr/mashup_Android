package com.mashup.shake

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

class ShakeDetector @Inject constructor(
    private val sensorManager: SensorManager
) : SensorEventListener {

    private val acceleration = FloatArray(3)
    private var lastAcceleration = FloatArray(3)
    private var lastUpdateTime: Long = 0
    private var shakeListener: ((Float) -> Unit)? = null

    fun startListening(listener: (Float) -> Unit) {
        shakeListener = listener
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopListening() {
        shakeListener = null
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - lastUpdateTime
        if (timeDifference > SHAKE_INTERVAL_TIME) {
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
                if (speed > SHAKE_THRESHOLD) {
                    shakeListener?.invoke(speed)
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