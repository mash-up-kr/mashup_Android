package com.mashup.feature.danggn.reward

import androidx.lifecycle.SavedStateHandle
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.data.repository.PopUpRepository
import com.mashup.core.data.repository.StorageRepository
import com.mashup.core.ui.widget.MashUpPopupEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DanggnBottomPopupViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val popUpRepository: PopUpRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    val popupKey = savedStateHandle.get<String>(EXTRA_POPUP_KEY)

    private val _popupEntity = MutableStateFlow<MashUpPopupEntity?>(null)
    val popupEntity: StateFlow<MashUpPopupEntity?> = _popupEntity.asStateFlow()

    init {
        getStorage()
    }

    private fun getStorage() = mashUpScope {
        if (popupKey.isNullOrBlank()) return@mashUpScope
        val result = storageRepository.getStorage(popupKey)

        if (result.isSuccess()) {
            MashUpPopupEntity(
                title = result.data?.valueMap?.get("title") ?: "",
                description = result.data?.valueMap?.get("subtitle") ?: "",
                imageResName = result.data?.valueMap?.get("imageName") ?: "",
                leftButtonText = result.data?.valueMap?.get("leftButtonTitle") ?: "",
                rightButtonText = result.data?.valueMap?.get("rightButtonTitle") ?: "",
            )
        }
    }

    fun patchPopupViewed() = mashUpScope {
        if (popupKey.isNullOrBlank()) return@mashUpScope
        popUpRepository.patchPopupViewed(popupKey)
    }

    companion object {
        private const val EXTRA_POPUP_KEY = "EXTRA_POPUP_KEY"
    }
}
