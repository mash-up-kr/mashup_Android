package com.mashup.ui.qrscan

import com.mashup.base.BaseViewModel
import com.mashup.data.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class QRScanViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) : BaseViewModel() {
    private val _qrcodeState = MutableSharedFlow<QRCodeState>()
    val qrcodeState: SharedFlow<QRCodeState> = _qrcodeState

    fun requestAttendance(qrcode: QRCode) = mashUpScope {
        val recognizedCode = qrcode.recognizedCode

        val response = attendanceRepository.attendanceCheck(
            code = recognizedCode
        )

        if (!response.isSuccess()) {
            handleErrorCode(response.code)
            return@mashUpScope
        } else {
            if (response.data?.isAttendance() == true) {
                _qrcodeState.emit(QRCodeState.SuccessAttendance)
            }
        }
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _qrcodeState.emit(QRCodeState.Error(code))
        }
    }
}

sealed interface QRCodeState {
    data class Error(val code: String) : QRCodeState
    object SuccessAttendance : QRCodeState
}