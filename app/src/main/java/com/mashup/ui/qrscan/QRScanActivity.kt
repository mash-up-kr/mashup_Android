package com.mashup.ui.qrscan

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import com.google.mlkit.vision.barcode.common.Barcode
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityQrScanBinding
import com.mashup.extensions.showToast
import com.mashup.ui.qrscan.camera.CameraManager
import com.mashup.utils.PermissionHelper
import com.mashup.widget.CommonDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QRScanActivity : BaseActivity<ActivityQrScanBinding>() {

    private lateinit var qrCodeAnalyzer: QRCodeAnalyzer
    private lateinit var cameraManager: CameraManager<List<Barcode>>
    private val permissionHelper by lazy {
        PermissionHelper(this)
    }

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
        initButtons()
        initCamera()
    }

    private fun initButtons() {
        viewBinding.btnClose.setOnClickListener {
            finish()
        }
    }

    private fun initCamera() {
        createCardAnalyzer()
        createCameraManager()

        if (requestCameraPermission()) {
            cameraManager.startCamera()
        }
    }

    private fun createCardAnalyzer() {
        qrCodeAnalyzer = QRCodeAnalyzer(
            onQRCodeRecognitionSuccess = { qrcodeInfo ->
                cameraManager.stopCamera()
            },
            onQRCodeRecognitionFailure = { throwable ->
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

    private fun requestCameraPermission() =
        permissionHelper.checkGrantedPermission(
            permission = PERMISSION_CAMERA,
            onRequestPermission = {
                permissionHelper.requestPermission(
                    requestCode = REQUEST_CODE_CAMERA,
                    permission = PERMISSION_CAMERA
                )
            },
            onShowRationaleUi = {
                showRequestCameraPermissionDialog()
            }
        )

    private fun showRequestCameraPermissionDialog() {
        CommonDialog(this).apply {
            setTitle(text = "카메라 권한")
            setMessage(text = "QR 출석을 위해 카메라 권한이 필수로 필요합니다. 권한을 허용해주세요!")
            setNegativeButton(text = "거절")
            setPositiveButton {
                permissionHelper.requestPermission(
                    requestCode = REQUEST_CODE_CAMERA,
                    permission = PERMISSION_CAMERA
                )
            }
            show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_CODE_CAMERA -> {
                if (grantResults.find { it == PackageManager.PERMISSION_GRANTED } != null) {
                    cameraManager.startCamera()
                }
            }
        }
    }

    override val layoutId: Int = R.layout.activity_qr_scan

    companion object {
        private const val PERMISSION_CAMERA = android.Manifest.permission.CAMERA
        private const val REQUEST_CODE_CAMERA = 200

        fun newIntent(context: Context) = Intent(context, QRScanActivity::class.java)
    }
}