package com.mashup.core.common.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.coroutines.EmptyCoroutineContext

class TimerUtils {
    companion object {
        private const val ONE_SEC = 1000
        private const val KOREAN_ONE_HOUR = 9 * 60 * 60 * 1000 // GMT 9시간 추가입니다
    }

    private var job: Job = Job()

    suspend fun startTimer(
        startTime: Date = Calendar.getInstance().time,
        endTime: Date,
        timerValue: (String) -> Unit
    ) {
        withContext(job + Dispatchers.IO) {
            var diffTime = (endTime.time - startTime.time)
            while (diffTime >= 0) {
                val result = kotlin.runCatching {
                    val cong = SimpleDateFormat("HH:mm:ss")
                    cong.format(Date(diffTime - KOREAN_ONE_HOUR))
                }.getOrDefault("??:??:??")

                timerValue(result)
                diffTime -= ONE_SEC
                delay(1000L)
            }
            stopTimer()
        }
    }

    private fun stopTimer() {
        job.cancel()
    }
}