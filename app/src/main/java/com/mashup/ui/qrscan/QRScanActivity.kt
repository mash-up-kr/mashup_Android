package com.mashup.ui.qrscan

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.viewModels
import com.google.mlkit.vision.barcode.common.Barcode
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityQrScanBinding
import com.mashup.extensions.showToast
import com.mashup.network.errorcode.*
import com.mashup.ui.extensions.gone
import com.mashup.ui.extensions.visible
import com.mashup.ui.qrscan.camera.CameraManager
import com.mashup.utils.PermissionHelper
import com.mashup.widget.CommonDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class
QRScanActivity : BaseActivity<ActivityQrScanBinding>() {

    private val viewModel: QRScanViewModel by viewModels()

    private lateinit var qrCodeAnalyzer: QRCodeAnalyzer
    private lateinit var cameraManager: CameraManager<List<Barcode>>
    private val permissionHelper by lazy {
        PermissionHelper(this)
    }

    override fun initViews() {
        initButtons()
        initCamera()
    }

    override fun initObserves() {
        flowLifecycleScope {
            viewModel.qrcodeState.collectLatest { qrcodeState ->
                when (qrcodeState) {
                    QRCodeState.SuccessAttendance -> {
                        setResult(RESULT_OK)
                        finish()
                    }
                    is QRCodeState.Error -> {
                        handleAttendanceErrorCode(qrcodeState)
                    }
                }
                cameraManager.startCamera()
            }
        }
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

    private fun showInvalidMessage(message: String) {
        viewBinding.tvInvalidMessage.run {
            visible()
            text = message
            postDelayed({
                gone()
            }, 3000L)
        }
    }

    private fun createCardAnalyzer() {
        qrCodeAnalyzer = QRCodeAnalyzer(
            onQRCodeRecognitionSuccess = { qrcode ->
                cameraManager.stopCamera()
                viewModel.requestAttendance(qrcode)
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

    private fun handleAttendanceErrorCode(error: QRCodeState.Error) {
        val codeMessage = when (error.code) {
            UNAUTHORIZED -> {
                "???????????? ??? ????????????????????????."
            }
            ATTENDANCE_CODE_NOT_FOUND -> {
                "?????? ????????? ???????????? ????????????."
            }
            ATTENDANCE_ALREADY_CHECKED -> {
                "?????? ?????? ????????? ????????????."
            }
            ATTENDANCE_CODE_DUPLICATED -> {
                "?????? ????????? ???????????????"
            }
            ATTENDANCE_TIME_OVER -> {
                "?????? ?????? ????????? ???????????????."
            }
            else -> {
                "?????? ??? ?????? ??????????????????."
            }
        }
        showInvalidMessage(
            error.message ?: codeMessage
        )
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
            setTitle(text = "????????? ??????")
            setMessage(text = "QR ????????? ?????? ????????? ????????? ????????? ???????????????. ????????? ??????????????????!")
            setNegativeButton(text = "??????")
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