package com.mashup.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mashup.base.BaseViewModel
import com.mashup.ui.signup.model.Validation
import com.mashup.ui.signup.state.CodeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor() : BaseViewModel() {

    private val withdrawalCode = MutableStateFlow("")
    val codeState = withdrawalCode
        .map {
            val state = when (it) {
                "탈퇴할게요" -> Validation.SUCCESS
                "" -> Validation.EMPTY
                else -> Validation.FAILED
            }
            val codeState = CodeState(
                code = it,
                validationCode = state,
                isValidationState = state == Validation.SUCCESS
            )
            codeState
        }

    fun setCode(code: String) {
        withdrawalCode.value = code
    }

    fun onClickSNS(context: Context?, link: String) {
        context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }
}