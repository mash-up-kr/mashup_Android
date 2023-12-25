package com.mashup.ui.password.fragment.enterid

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.core.common.constant.MEMBER_DUPLICATED_IDENTIFICATION
import com.mashup.core.common.extensions.setEmptyUIOfTextField
import com.mashup.core.common.extensions.setFailedUiOfTextField
import com.mashup.core.common.extensions.setSuccessUiOfTextField
import com.mashup.core.common.utils.keyboard.TranslateDeferringInsetsAnimationCallback
import com.mashup.databinding.FragmentEnterIdBinding
import com.mashup.ui.password.ButtonState
import com.mashup.ui.password.IdState
import com.mashup.ui.password.PasswordViewModel
import com.mashup.ui.password.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class EnterIdFragment : BaseFragment<FragmentEnterIdBinding>() {
    private val passwordViewModel: PasswordViewModel by activityViewModels()

    override val layoutId: Int
        get() = R.layout.fragment_enter_id

    override fun initViews() {
        initTextField()
        initButton()
        initObserves()
    }

    override fun initObserves() {
        flowViewLifecycleScope {
            passwordViewModel.passwordScreenState.collectLatest { state ->
                setUiOfIdState(state.idState)
                setUiOfButtonState(state.buttonState)
            }
        }

        flowViewLifecycleScope {
            passwordViewModel.moveToNextScreen.collectLatest {
                val navController = findNavController()
                if (navController.currentDestination != null && navController.currentDestination?.id != R.id.changePasswordFragment) {
                    navController.navigate(R.id.action_enterIdFragment_to_changePasswordFragment)
                }
            }
        }
    }

    private fun initTextField() {
        viewBinding.textFieldId.apply {
            addOnTextChangedListener { id ->
                passwordViewModel.updateId(id = id)
            }
        }
    }

    private fun initButton() {
        ViewCompat.setWindowInsetsAnimationCallback(
            viewBinding.layoutButton,
            TranslateDeferringInsetsAnimationCallback(
                view = viewBinding.layoutButton,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime(),
            ),
        )
        viewBinding.btnNext.setOnButtonThrottleFirstClickListener(viewLifecycleOwner) {
            passwordViewModel.onClickNextButton(screen = ScreenState.EnterId)
        }
    }

    private fun setUiOfIdState(idState: IdState) = with(viewBinding) {
        val idDescription = requireContext().getString(R.string.desc_sign_up_id)
        val duplicatedIdDescription = requireContext().getString(R.string.id_already_used)
        when (idState) {
            is IdState.Empty -> {
                textFieldId.setDescriptionText(idDescription)
                textFieldId.setEmptyUIOfTextField()
            }
            is IdState.Success -> {
                textFieldId.setDescriptionText(idDescription)
                textFieldId.setSuccessUiOfTextField()
            }
            is IdState.Error -> {
                val errorMessage = when (idState.code) {
                    MEMBER_DUPLICATED_IDENTIFICATION -> {
                        duplicatedIdDescription
                    }
                    else -> {
                        idDescription
                    }
                }
                textFieldId.setDescriptionText(errorMessage)
                textFieldId.setFailedUiOfTextField()
            }
        }
    }

    private fun setUiOfButtonState(buttonState: ButtonState) = with(viewBinding) {
        when (buttonState) {
            is ButtonState.Loading -> {
                btnNext.showLoading()
            }
            is ButtonState.Disable -> {
                btnNext.hideLoading()
                btnNext.setButtonEnabled(false)
            }
            else -> {
                btnNext.hideLoading()
                btnNext.setButtonEnabled(true)
            }
        }
    }
}
