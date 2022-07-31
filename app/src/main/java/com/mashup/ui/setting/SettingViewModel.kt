package com.mashup.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mashup.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor() : BaseViewModel() {

    fun onClickSNS(context: Context?, link: String) {
        context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    override fun handleErrorCode(code: String) {
    }
}