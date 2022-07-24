package com.mashup.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mashup.R
import com.mashup.databinding.ViewButtonBinding
import com.mashup.extensions.dp
import com.mashup.extensions.onDebouncedClick
import com.mashup.ui.extensions.gone
import com.mashup.ui.extensions.visible

class ButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val viewBinding: ViewButtonBinding =
        ViewButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        initButtonViewLayout()
        setButtonStyle(ButtonStyle.PRIMARY)
    }

    fun setOnButtonClickListener(clickListener: () -> Unit) {
        setOnClickListener {
            clickListener.invoke()
        }
    }

    fun setOnButtonDebounceClickListener(
        lifecycleOwner: LifecycleOwner,
        clickListener: () -> Unit
    ) {
        onDebouncedClick(lifecycleOwner.lifecycleScope) {
            clickListener.invoke()
        }
    }

    fun showLoading() {
        viewBinding.progressCircular.visible()
    }

    fun hideLoading() {
        viewBinding.progressCircular.gone()
    }

    fun setButtonStyle(buttonStyle: ButtonStyle) = with(viewBinding) {
        backgroundTintList =
            ContextCompat.getColorStateList(context, buttonStyle.backgroundColorRes)
        tvButton.setTextColor(
            ContextCompat.getColor(context, buttonStyle.textColorRes)
        )
    }

    private fun initButtonViewLayout() {
        updateLayoutParams {
            height = HEIGHT_BUTTON.dp(context)
            setPadding(
                paddingStart,
                PADDING_VERTICAL.dp(context),
                paddingEnd,
                PADDING_VERTICAL.dp(context)
            )
        }
        setBackgroundResource(R.drawable.bg_button)
    }

    fun setButtonEnabled(isEnabled: Boolean) {
        this.isEnabled = isEnabled
        setButtonStyle(
            if (isEnabled) ButtonStyle.PRIMARY else ButtonStyle.DISABLE
        )
    }

    private fun setButtonText(text: String) {
        viewBinding.tvButton.text = text
    }

    enum class ButtonStyle(@ColorRes val backgroundColorRes: Int, @ColorRes val textColorRes: Int) {
        PRIMARY(backgroundColorRes = R.color.brand500, textColorRes = R.color.white),
        DISABLE(backgroundColorRes = R.color.brand300, textColorRes = R.color.white),
        DEFAULT(backgroundColorRes = R.color.gray100, textColorRes = R.color.gray600)
    }

    companion object {
        private const val PADDING_VERTICAL = 14
        private const val HEIGHT_BUTTON = 52

        @JvmStatic
        @BindingAdapter(value = ["text_button"], requireAll = false)
        fun ButtonView.bindText(text: String?) {
            text?.run {
                setButtonText(this)
            }
        }

        @JvmStatic
        @BindingAdapter(value = ["style_button"], requireAll = false)
        fun ButtonView.bindStyle(buttonStyle: ButtonStyle?) {
            buttonStyle?.run {
                setButtonStyle(this)
            }
        }
    }
}