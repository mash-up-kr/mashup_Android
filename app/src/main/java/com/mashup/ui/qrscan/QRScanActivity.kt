package com.mashup.ui.qrscan

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.common.Barcode
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityQrScanBinding
import com.mashup.extensions.showToast
import com.mashup.ui.qrscan.camera.CameraManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QRScanActivity : BaseActivity<ActivityQrScanBinding>() {

    private lateinit var qrCodeAnalyzer: QRCodeAnalyzer
    private lateinit var cameraManager: CameraManager<List<Barcode>>

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                cameraManager.startCamera()
            } else {
                showToast(getString(R.string.denied_camera_permission))
            }
        }

    override fun initViews() {
        initCamera()
    }

    private fun initCamera() {
        createCardAnalyzer()
        createCameraManager()


        when {
            isCameraPermissionGranted() -> {
                cameraManager.startCamera()
            }
            else -> {
                requestPermissionLauncher.launch(CAMERA_PERMISSION)
            }
        }
    }

    private fun createCardAnalyzer() {
        qrCodeAnalyzer = QRCodeAnalyzer(
            onCardRecognitionSuccess = { cardInfo ->
                cameraManager.stopCamera()
            },
            onCardRecognitionFailure = { throwable ->
                throwable?.message?.let { message ->
                    showToast(message)
                }
            }
        )
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
            context = this,
            finderView = viewBinding.viewFinder,
            lifecycleOwner = this,
            baseImageAnalyzer = qrCodeAnalyzer
        )
    }

    override fun onResume() {
        super.onResume()
        cameraManager.startCamera()
    }

    override fun onPause() {
        super.onPause()
        cameraManager.stopCamera()
    }

    private fun isCameraPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            CAMERA_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override val layoutId: Int = R.layout.activity_qr_scan


    companion object {
        private const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA

        fun newIntent(context: Context) = Intent(context, QRScanActivity::class.java)
    }

}