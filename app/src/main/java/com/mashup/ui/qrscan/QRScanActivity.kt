package com.mashup.ui.qrscan

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.viewModels
import com.google.mlkit.vision.barcode.common.Barcode
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.common.model.NavigationAnimationType
import com.mashup.common.utils.PermissionHelper
import com.mashup.common.widget.CommonDialog
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.databinding.ActivityQrScanBinding
import com.mashup.network.errorcode.*
import com.mashup.ui.qrscan.camera.CameraManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class QRScanActivity : BaseActivity<ActivityQrScanBinding>() {

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
                    QRCodeState.Loading -> {
                        showLoading()
                    }
                    QRCodeState.Success -> {
                        hideLoading()
                        setResult(RESULT_OK)
                        finish()
                    }
                    is QRCodeState.Error -> {
                        hideLoading()
                        handleCommonError(qrcodeState.code)
                        handleAttendanceErrorCode(qrcodeState)
                    }
                }
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
        startCameraWithPermissionCheck()
    }

    private fun startCameraWithPermissionCheck() {
        if (permissionHelper.isPermissionGranted(PERMISSION_CAMERA)) {
            cameraManager.startCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun createCardAnalyzer() {
        qrCodeAnalyzer = QRCodeAnalyzer(
            onQRCodeRecognitionSuccess = { qrcode ->
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
        if (permissionHelper.isPermissionGranted(PERMISSION_CAMERA)) {
            cameraManager.startCamera()
        }
    }

    private fun handleAttendanceErrorCode(error: QRCodeState.Error) {
        val codeMessage = when (error.code) {
            ATTENDANCE_ALREADY_CHECKED -> {
                "이미 출석 체크를 했어요"
            }
            ATTENDANCE_CODE_DUPLICATED -> {
                "이미 사용된 코드입니다."
            }
            ATTENDANCE_TIME_OVER -> {
                "출석 마감 시간이 지나서 결석이에요"
            }
            ATTENDANCE_TIME_BEFORE -> {
                "아직 출석할 수 없어요"
            }
            ATTENDANCE_CODE_INVALID, ATTENDANCE_CODE_NOT_FOUND -> {
                "올바르지 않은 QR 코드입니다"
            }
            MEMBER_NOT_FOUND -> {
                "회원 정보를 찾을 수 없습니다."
            }
            else -> {
                null
            }
        }
        codeMessage?.run { showToast(this) }
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
            setTitle(text = "카메라 권한 재설정")
            setMessage(text = "QR 출석체크를 하기 위해서는 카메라의 권한이 필수로 필요합니다")
            setNegativeButton(text = "거절") {
                finish()
            }
            setPositiveButton("확인") {
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
                } else {
                    CommonDialog(this).apply {
                        setTitle(text = "카메라 권한 없음")
                        setMessage(text = "QR 출석체크를 하기 위한 카메라의 권한이 허용되지 않아 종료됩니다.")
                        setPositiveButton("확인") {
                            finish()
                        }
                        show()
                    }
                }
            }
        }
    }

    override val layoutId: Int = R.layout.activity_qr_scan

    companion object {
        private const val PERMISSION_CAMERA = android.Manifest.permission.CAMERA
        private const val REQUEST_CODE_CAMERA = 200

        fun newIntent(context: Context) = Intent(context, QRScanActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
        }
    }
}