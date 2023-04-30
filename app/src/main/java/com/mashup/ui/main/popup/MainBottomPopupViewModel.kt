package com.mashup.ui.main.popup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.mashup.core.common.base.BaseViewModel
import com.mashup.data.repository.StorageRepository
import com.mashup.ui.main.model.MainPopupEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainBottomPopupViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val _uiState = mutableStateOf<MainBottomPopupUiState>(MainBottomPopupUiState.Loading)
    val uiState: State<MainBottomPopupUiState> = _uiState

    init {
        getStorage()
    }

    fun getStorage() = mashUpScope {
        val result = storageRepository.getStorage("DANGGN")

        if (result.isSuccess()) {
            _uiState.value = MainBottomPopupUiState.Success(
                MainPopupEntity(
                    title = result.data?.valueMap?.get("title") ?: "",
                    description = result.data?.valueMap?.get("subTitle") ?: "",
                    imageResName = result.data?.valueMap?.get("imageName") ?: "",
                    leftButtonText = result.data?.valueMap?.get("leftButtonTitle") ?: "",
                    rightButtonText = result.data?.valueMap?.get("rightButtonTitle") ?: ""
                )
            )
        }
    }
}

sealed interface MainBottomPopupUiState {
    object Loading : MainBottomPopupUiState

    data class Success(
        val mainPopupEntity: MainPopupEntity
    ) : MainBottomPopupUiState
}