package com.mashup.ui.qrscan

import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.mashup.core.common.R
import com.mashup.core.common.utils.PermissionHelper
import com.mashup.ui.qrscan.camera.CameraManager
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QRScanScreen(
    viewModel: QRScanViewModel = viewModel(),
    cameraManager: CameraManager<List<Barcode>>,
    onShowLoading: () -> Unit,
    onHideLoading: () -> Unit,
    onFinish: () -> Unit,
    onHandleCommonError: (String) -> Unit,
    onHandleAttendanceErrorCode: (QRCodeState.Error) -> Unit,
    onRequestQrAttendancePermissions: () -> Unit,
    hasPermission: Boolean,
    onLocationInfo: () -> Unit
){
    LaunchedEffect(Unit) {
        viewModel.qrcodeState.collectLatest { state ->
            when(state){
                is QRCodeState.Success -> {
                    onHideLoading()
                    onFinish()
                }
                is QRCodeState.Error -> {
                    onHideLoading()
                    onHandleCommonError(state.code)
                    onHandleAttendanceErrorCode(state)
                    onFinish()
                }
                is QRCodeState.Loading -> {
                    onShowLoading()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ){
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                }
            },
            update = { view ->
                if(hasPermission){
                    onLocationInfo()
                    cameraManager.startCamera(view)
                }else{
                    onRequestQrAttendancePermissions()
                }

            }
        )
        IconButton(
            onClick = onFinish,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 20.dp, end = 20.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = "닫기",
                tint = Color.White
            )
        }
        Text(
            text = "QR 코드를  스캔하세요",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)

        )
    }
}