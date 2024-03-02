package com.mashup.ui.qrscan

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.marginTop
import com.google.mlkit.vision.barcode.common.Barcode
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.log.LOG_QR
import com.mashup.constant.log.LOG_QR_DONE
import com.mashup.constant.log.LOG_QR_SUCCESS
import com.mashup.constant.log.LOG_QR_TIME_FAIL
import com.mashup.constant.log.LOG_QR_WRONG
import com.mashup.core.common.constant.MEMBER_NOT_FOUND
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.common.utils.PermissionHelper
import com.mashup.core.common.widget.CommonDialog
import com.mashup.databinding.ActivityQrScanBinding
import com.mashup.network.errorcode.ATTENDANCE_ALREADY_CHECKED
import com.mashup.network.errorcode.ATTENDANCE_CODE_DUPLICATED
import com.mashup.network.errorcode.ATTENDANCE_CODE_INVALID
import com.mashup.network.errorcode.ATTENDANCE_CODE_NOT_FOUND
import com.mashup.network.errorcode.ATTENDANCE_DISTANCE_OUT_OF_RANGE
import com.mashup.network.errorcode.ATTENDANCE_TIME_BEFORE
import com.mashup.network.errorcode.ATTENDANCE_TIME_OVER
import com.mashup.ui.qrscan.camera.CameraManager
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class QRScanActivity : BaseActivity<ActivityQrScanBinding>(), LocationListener {

    private val viewModel: QRScanViewModel by viewModels()

    private lateinit var qrCodeAnalyzer: QRCodeAnalyzer
    private lateinit var cameraManager: CameraManager<List<Barcode>>
    private val permissionHelper by lazy {
        PermissionHelper(this)
    }

    private val permissionList = listOf(
        PERMISSION_CAMERA,
        PERMISSION_COARSE_LOCATION,
        PERMISSION_FINE_LOCATION
    )

    private val locationManager: LocationManager? by lazy { (getSystemService(Context.LOCATION_SERVICE) as? LocationManager) }

    override fun initWindowInset() {
        // do nothing
    }

    override fun initViews() {
        window.statusBarColor = Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        AnalyticsManager.addEvent(LOG_QR)
        initStatusBarMargin()
        initButtons()
        initCamera()
    }

    private fun initStatusBarMargin() {
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root) { _, insets ->
            val systemInset = insets.getInsets(WindowInsetsCompat.Type.statusBars())

            val marginTop = viewBinding.btnClose.marginTop
            (viewBinding.btnClose.layoutParams as ViewGroup.MarginLayoutParams).topMargin =
                marginTop + (systemInset.top / 2)

            WindowInsetsCompat.CONSUMED
        }
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
                        setResult(RESULT_CONFIRM_SUCCESS_QR)
                        finish()
                    }
                    is QRCodeState.Error -> {
                        hideLoading()
                        handleCommonError(qrcodeState.code)
                        handleAttendanceErrorCode(qrcodeState)

                        setResult(RESULT_CONFIRM_QR)
                        finish()
                    }
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude

        viewModel.setLocation(latitude, longitude)
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
        if (permissionList.any { !permissionHelper.isPermissionGranted(it) }) {
            requestQrAttendancePermissions()
        } else {
            setLocationInfo()
            cameraManager.startCamera()
        }
    }

    private fun createCardAnalyzer() {
        qrCodeAnalyzer = QRCodeAnalyzer(
            onQRCodeRecognitionSuccess = { qrcode ->
                AnalyticsManager.addEvent(LOG_QR_SUCCESS)
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

    override fun onDestroy() {
        super.onDestroy()
        locationManager?.removeUpdates(this)
    }

    private fun handleAttendanceErrorCode(error: QRCodeState.Error) {
        val codeMessage = when (error.code) {
            ATTENDANCE_ALREADY_CHECKED -> {
                AnalyticsManager.addEvent(LOG_QR_DONE)
                "이미 출석 체크를 했어요"
            }
            ATTENDANCE_CODE_DUPLICATED -> {
                "이미 사용된 코드입니다."
            }
            ATTENDANCE_TIME_OVER -> {
                AnalyticsManager.addEvent(LOG_QR_TIME_FAIL)
                "출석 마감 시간이 지나서 결석이에요"
            }
            ATTENDANCE_TIME_BEFORE -> {
                "아직 출석할 수 없어요"
            }
            ATTENDANCE_CODE_INVALID, ATTENDANCE_CODE_NOT_FOUND -> {
                AnalyticsManager.addEvent(LOG_QR_WRONG)
                "올바르지 않은 QR 코드입니다"
            }
            MEMBER_NOT_FOUND -> {
                "회원 정보를 찾을 수 없습니다."
            }
            ATTENDANCE_DISTANCE_OUT_OF_RANGE -> {
                "유효한 출석체크 거리를 벗어났습니다."
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

    private fun requestQrAttendancePermissions() {
        val deniedPermissions = mutableListOf<String>()
        permissionList.forEach { permission ->
            if (!permissionHelper.isPermissionGranted(permission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    showRequestPermissionRationale()
                    return
                } else {
                    deniedPermissions.add(permission)
                }
            }
        }

        if (deniedPermissions.isNotEmpty()) {
            permissionHelper.requestPermissions(deniedPermissions.toTypedArray(), REQUEST_PERMISSION_CODE)
        }
    }

    private fun showRequestPermissionRationale() {
        CommonDialog(this).apply {
            setTitle(text = "필요 권한 재설정")
            setMessage(text = "QR 출석체크를 하기 위해서는\n[카메라, 위치정보]의 접근 권한이 필요해요.")
            setNegativeButton(text = "닫기") {
                finish()
            }
            setPositiveButton("재설정") {
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                ).also { intent ->
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
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
            REQUEST_PERMISSION_CODE -> {
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

    private fun setLocationInfo() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
        locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1f, this)

        val currentLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        viewModel.setLocation(currentLocation?.latitude, currentLocation?.longitude)
    }

    override val layoutId: Int = R.layout.activity_qr_scan

    companion object {
        private const val PERMISSION_CAMERA = android.Manifest.permission.CAMERA
        private const val PERMISSION_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION
        private const val PERMISSION_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION
        private const val REQUEST_PERMISSION_CODE = 200
        const val RESULT_CONFIRM_QR = 201
        const val RESULT_CONFIRM_SUCCESS_QR = 202

        fun newIntent(context: Context) = Intent(context, QRScanActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
        }
    }
}
