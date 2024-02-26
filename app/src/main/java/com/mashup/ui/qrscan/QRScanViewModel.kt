package com.mashup.ui.qrscan

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.data.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class QRScanViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) : BaseViewModel() {
    private val _qrcodeState = MutableSharedFlow<QRCodeState>()
    val qrcodeState: SharedFlow<QRCodeState> = _qrcodeState

    private val recognitionQRCode = MutableSharedFlow<QRCode>()

    private val isCheckingCode = AtomicBoolean(false)

    private var location = Pair<Double?, Double?>(null, null)

    init {
        recognitionQRCode
            .onEach {
                isCheckingCode.set(true)
                checkAttendance(it)
                delay(5000)
                isCheckingCode.set(false)
            }.launchIn(viewModelScope)
    }

    fun requestAttendance(code: QRCode) {
        mashUpScope {
            if (!isCheckingCode.get()) {
                recognitionQRCode.emit(code)
            }
        }
    }

    private fun checkAttendance(qrcode: QRCode) = mashUpScope {
        _qrcodeState.emit(QRCodeState.Loading)
        val recognizedCode = qrcode.recognizedCode
        val response = attendanceRepository.attendanceCheck(
            code = recognizedCode,
            latitude = location.first ?: return@mashUpScope,
            longitude = location.second ?: return@mashUpScope,
        )

        if (!response.isSuccess()) {
            handleErrorCode(response.code)
            return@mashUpScope
        } else {
            _qrcodeState.emit(QRCodeState.Success)
        }
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _qrcodeState.emit(QRCodeState.Error(code))
        }
    }

    fun setLocation(latitude: Double?, longitude: Double?) {
        location = Pair(latitude, longitude)
    }
}

sealed interface QRCodeState {
    object Loading : QRCodeState
    data class Error(val code: String) : QRCodeState
    object Success : QRCodeState
}
