package com.mashup.ui.qrscan

import com.mashup.base.BaseViewModel
import com.mashup.data.datastore.UserDataSource
import com.mashup.data.repository.AttendanceRepository
import com.mashup.network.errorcode.ATTENDANCE_CODE_NOT_FOUND
import com.mashup.network.errorcode.UNAUTHORIZED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class QRScanViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val userDataSource: UserDataSource
) : BaseViewModel() {
    private val _qrcodeState = MutableSharedFlow<QRCodeState>()
    val qrcodeState: SharedFlow<QRCodeState> = _qrcodeState

    fun requestAttendance(qrcode: QRCode) = mashUpScope {
        val memberId = userDataSource.memberId
        if (memberId == null) {
            _qrcodeState.emit(QRCodeState.Error(code = UNAUTHORIZED))
            return@mashUpScope
        }

        val eventId = qrcode.recognizedCode.toIntOrNull()
        if (eventId == null) {
            _qrcodeState.emit(QRCodeState.Error(code = ATTENDANCE_CODE_NOT_FOUND))
            return@mashUpScope
        }

        val response = attendanceRepository.attendanceCheck(
            eventId = eventId,
            memberId = memberId
        )

        if (!response.isSuccess()) {
            _qrcodeState.emit(QRCodeState.Error(response.code, response.message))
            return@mashUpScope
        }
        _qrcodeState.emit(QRCodeState.SuccessAttendance)
    }
}

sealed interface QRCodeState {
    data class Error(val code: String, val message: String? = null) : QRCodeState
    object SuccessAttendance : QRCodeState
}