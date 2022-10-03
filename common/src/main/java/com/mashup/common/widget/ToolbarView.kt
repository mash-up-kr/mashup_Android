package com.mashup.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.mashup.common.databinding.ViewToolbarBinding
import com.mashup.common.extensions.gone
import com.mashup.common.extensions.visible

class ToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val viewBinding: ViewToolbarBinding =
        ViewToolbarBinding.inflate(LayoutInflater.from(context), this, true)

    fun setVisibleBackButton(visible: Boolean) {
        viewBinding.btnBack.isVisible = visible
    }

    fun setVisibleCloseButton(visible: Boolean) {
        viewBinding.btnClose.isVisible = visible
    }

    fun setTitle(title: String) {
        viewBinding.tvTitle.text = title
    }

    fun setOnBackButtonClickListener(clickListener: (ToolbarView) -> Unit) {
        viewBinding.btnBack.setOnClickListener {
            clickListener(this)
        }
    }

    fun setOnCloseButtonClickListener(clickListener: (ToolbarView) -> Unit) {
        viewBinding.btnClose.setOnClickListener {
            clickListener(this)
        }
    }

    fun showDivider() = with(viewBinding.divider) {
        visible()
    }

    fun hideDivider() = with(viewBinding.divider) {
        gone()
    }

    companion object {

        @JvmStatic
        @BindingAdapter(
            value = ["toolbar_back_button_visible", "toolbar_close_button_visible"],
            requireAll = false
        )
        fun ToolbarView.setVisibleButtons(
            backButtonVisible: Boolean,
            closeButtonVisible: Boolean
        ) {
            setVisibleBackButton(backButtonVisible)
            setVisibleCloseButton(closeButtonVisible)
        }

        @JvmStatic
        @BindingAdapter("toolbar_title")
        fun ToolbarView.setTitleText(title: String?) {
            title?.run {
                setTitle(this)
            }
        }
    }
}
