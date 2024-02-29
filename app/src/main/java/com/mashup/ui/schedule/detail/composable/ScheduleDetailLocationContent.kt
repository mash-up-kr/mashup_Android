package com.mashup.ui.schedule.detail.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray200
import com.mashup.core.ui.colors.Gray500
import com.mashup.core.ui.colors.Gray700
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Caption2
import com.mashup.core.ui.typography.Caption3
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import com.mashup.core.common.R as CR

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ScheduleDetailLocationContent(
    placeName: String,
    address: String,
    latitude: Double?,
    longitude: Double?,
    modifier: Modifier = Modifier,
    copyToClipboard: (String) -> Unit = {}
) {
    val location = LatLng(latitude ?: 0.0, longitude ?: 0.0)

    // ZoomLevel 전부 10으로 통일
    val zoomLevel = 10.0

    // 카메라 초기 위치를 설정 (위도, 경도)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition(location, zoomLevel)
    }

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                isLocationButtonEnabled = false,
                isScaleBarEnabled = false,
                isZoomControlEnabled = false,
                isZoomGesturesEnabled = false,
                isScrollGesturesEnabled = false
            )
        )
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                ScheduleInfoText(iconRes = CR.drawable.ic_mappin, info = placeName)
                if (address.isNotEmpty()) {
                    Text(
                        text = address,
                        modifier = Modifier.padding(start = 24.dp),
                        style = Caption3,
                        color = Gray500
                    )
                }
            }

            if (address.isNotEmpty()) {
                OutlinedButton(
                    onClick = { copyToClipboard(address) },
                    border = BorderStroke(1.dp, Gray200),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.defaultMinSize(
                        minWidth = ButtonDefaults.MinWidth,
                        minHeight = 28.dp
                    )
                ) {
                    Text(text = "주소 복사하기", style = Caption2, color = Gray700)
                }
            }
        }

        if (address.isNotEmpty() && location.latitude != 0.0 && location.longitude != 0.0) {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                NaverMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = mapUiSettings
                ) {
                    Marker(
                        state = MarkerState(location),
                        icon = OverlayImage.fromResource(CR.drawable.img_mappin),
                        width = 36.dp,
                        height = 50.dp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewScheduleDetailLocationContent() {
    MashUpTheme {
        ScheduleDetailLocationContent(
            placeName = "종각 문화마을",
            address = "서울 특별시 봉천구 00동 131-6번지",
            latitude = 37.532600,
            longitude = 127.024612
        )
    }
}
