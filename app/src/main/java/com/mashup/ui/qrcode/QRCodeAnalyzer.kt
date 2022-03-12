package com.mashup.ui.qrcode

import android.graphics.Rect
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.mashup.ui.qrcode.camera.BaseImageAnalyzer
import java.io.IOException

class QRCodeAnalyzer(
    private val onCardRecognitionSuccess: (QRCode) -> Unit,
    private val onCardRecognitionFailure: (Throwable?) -> Unit
) : BaseImageAnalyzer<List<Barcode>>() {

    companion object {
        private val TAG = QRCodeAnalyzer::class.java.name

        private const val EXCEPTION_DUPLICATED_QRCODE_MESSAGE =
            "하나의 QR 코드만 화면에 인식시켜주세요"
        private const val EXCEPTION_RETRY_QRCODE_MESSAGE =
            "잘못된 QR 코드 인식을 시도하셨습니다. 다시 시도해주세요"
    }

    private val qrcodeRecognizer by lazy {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_QR_CODE
                )
                .build()
        )
    }

    override fun onSuccess(results: List<Barcode>, rect: Rect) {
        if (results.size > 1) {
            onCardRecognitionFailure(
                IllegalStateException(EXCEPTION_DUPLICATED_QRCODE_MESSAGE)
            )
            return
        }

        val recognizedCodeQrcode = results[0].rawValue

        if (recognizedCodeQrcode == null) {
            onCardRecognitionFailure(
                IllegalArgumentException(EXCEPTION_RETRY_QRCODE_MESSAGE)
            )
            return
        } else {
            onCardRecognitionSuccess(QRCode(recognizedCodeQrcode))
        }
    }

    override fun onFailure(e: Exception) {
        onCardRecognitionFailure(e)
    }

    override fun stop() {
        try {
            qrcodeRecognizer.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Barcode Recognition: $e")
        }
    }

    override fun detectInImage(image: InputImage): Task<List<Barcode>> {
        return qrcodeRecognizer.process(image)
    }
}