package com.mashup.ui.qrscan

import com.mashup.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class QRScanViewModel @Inject constructor() : BaseViewModel() {
    private val _qrcodeState = MutableSharedFlow<QRCodeState>()
    val qrcodeState: SharedFlow<QRCodeState> = _qrcodeState

    fun requestAttendance(qrcode: QRCode) = mashUpScope {
        //TODO: add QRScan
        _qrcodeState.emit(QRCodeState.SuccessAttendance)
    }
}

sealed interface QRCodeState {
    data class InValidQRCode(
        val message: String
    ) : QRCodeState

    object SuccessAttendance : QRCodeState
}