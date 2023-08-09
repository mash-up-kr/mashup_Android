package com.mashup.feature.danggn.reward

import androidx.lifecycle.SavedStateHandle
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.data.repository.PopUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DanggnBottomPopupViewModel @Inject constructor(
    private val popUpRepository: PopUpRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val popupKey = savedStateHandle.get<String>(EXTRA_POPUP_KEY)

    fun patchPopupViewed() = mashUpScope {
        if (popupKey.isNullOrBlank()) return@mashUpScope
        popUpRepository.patchPopupViewed(popupKey)
    }

    companion object {
        private const val EXTRA_POPUP_KEY = "EXTRA_POPUP_KEY"
    }
}
