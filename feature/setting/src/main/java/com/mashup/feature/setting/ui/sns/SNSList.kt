package com.mashup.feature.setting.ui.sns

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.feature.setting.R
import com.mashup.feature.setting.constant.FACEBOOK
import com.mashup.feature.setting.constant.INSTAGRAM
import com.mashup.feature.setting.constant.MASHUP_UP_HOME
import com.mashup.feature.setting.constant.MASHUP_UP_RECRUIT
import com.mashup.feature.setting.constant.TISTORY
import com.mashup.feature.setting.constant.YOUTUBE
import com.mashup.feature.setting.model.SNSModel
import com.mashup.core.common.R as CR

val snsList = listOf(
    SNSModel(R.string.facebook, CR.drawable.ic_facebook, FACEBOOK),
    SNSModel(R.string.instagram, CR.drawable.img_instagram, INSTAGRAM),
    SNSModel(R.string.tistory, CR.drawable.ic_tistory, TISTORY),
    SNSModel(R.string.youtube, CR.drawable.ic_youtube, YOUTUBE),
    SNSModel(R.string.mHome, CR.drawable.ic_mashup, MASHUP_UP_HOME),
    SNSModel(R.string.mRecruit, CR.drawable.ic_mashup_dark, MASHUP_UP_RECRUIT),
)

@ExperimentalFoundationApi
@Composable
fun SNSList(
    modifier: Modifier = Modifier,
    onClickSNS: (link: String) -> Unit
) {
    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        LazyVerticalGrid(
            modifier = modifier.padding(12.dp),
            columns = GridCells.Fixed(2)
        ) {
            items(snsList) { item ->
                SNSItem(
                    name = stringResource(id = item.name),
                    snsIconRes = item.iconRes,
                    onClickItem = { onClickSNS(item.link) }
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@Preview("DarkMode", widthDp = 360)
@Preview(widthDp = 360)
@Composable
fun SNSListScreenPrev() {
    MashUpTheme {
        Surface(color = MaterialTheme.colors.onBackground) {
            SNSList {}
        }
    }
}
