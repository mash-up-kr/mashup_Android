package com.mashup.ui.setting.sns

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.R
import com.mashup.URL
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.ui.model.SNSModel
import com.mashup.core.common.R as CR

val snsList = listOf(
    SNSModel(R.string.facebook, CR.drawable.ic_facebook, URL.FACEBOOK),
    SNSModel(R.string.instagram, CR.drawable.img_instagram, URL.INSTAGRAM),
    SNSModel(R.string.tistory, CR.drawable.ic_tistory, URL.TISTORY),
    SNSModel(R.string.youtube, CR.drawable.ic_youtube, URL.YOUTUBE),
    SNSModel(R.string.mHome, CR.drawable.ic_mashup, URL.MASHUP_UP_HOME),
    SNSModel(R.string.mRecruit, CR.drawable.ic_mashup_dark, URL.MASHUP_UP_RECRUIT)
)

@Composable
fun SNSList(
    modifier: Modifier = Modifier,
    onClickSNS: (link: String) -> Unit
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
