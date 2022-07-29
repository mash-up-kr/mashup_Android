package com.mashup.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mashup.R
import com.mashup.databinding.ViewTextLayoutBinding
import com.mashup.extensions.onThrottleFirstClick

class TextSelectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val viewBinding: ViewTextLayoutBinding =
        ViewTextLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    private val collapseValueAnimator: ValueAnimator by lazy {
        ValueAnimator.ofFloat(1f, 0f).apply {
            duration = 300
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                (it.animatedValue as? Float)?.let { value ->
                    viewBinding.layoutMotion.progress = value
                    viewBinding.tvHintLabel.textSize =
                        (SIZE_TEXT_COLLAPSE - ((SIZE_TEXT_COLLAPSE - SIZE_TEXT_EXPEND) * value))
                    viewBinding.etText.alpha = value
                }
            }
        }
    }

    private val expendValueAnimator: ValueAnimator by lazy {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 300
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener {
                (it.animatedValue as? Float)?.let { value ->
                    viewBinding.layoutMotion.progress = value
                    viewBinding.tvHintLabel.textSize =
                        (SIZE_TEXT_COLLAPSE - ((SIZE_TEXT_COLLAPSE - SIZE_TEXT_EXPEND) * value))
                    viewBinding.etText.alpha = value
                }
            }
        }
    }

    private var isExpend: Boolean = false

    init {
        initViews()
    }

    private fun initViews() {
        initIcon()
        initEditText()
    }

    private fun initIcon() {
        viewBinding.imgIcon.run {
            updateLayoutParams<LayoutParams> {
                topToTop = LayoutParams.PARENT_ID
                bottomMargin = 0
            }
            setImageResource(R.drawable.ic_chevron_down)
            imageTintList = ContextCompat.getColorStateList(context, R.color.gray400)
        }
    }

    private fun initEditText() {
        viewBinding.etText.run {
            isEnabled = false
            addTextChangedListener { text ->
                if (text.isNullOrEmpty()) {
                    startCollapseAnimationHintLabel()
                } else {
                    startExpendAnimationHintLabel()
                }
            }
        }
    }

    fun setHintText(hint: String) = with(viewBinding.tvHintLabel) {
        if (text == hint) return@with
        text = hint
    }

    fun setHintTextColor(@ColorRes colorRes: Int) {
        viewBinding.tvHintLabel.setTextColor(ContextCompat.getColor(context, colorRes))
    }

    fun setDescriptionText(description: String) {
        viewBinding.tvDescription.visibility = View.VISIBLE
        viewBinding.tvDescription.text = description
    }

    fun setSelectionThrottleFirstClickListener(
        lifecycleOwner: LifecycleOwner,
        clickListener: () -> Unit
    ) {
        viewBinding.viewTouch.onThrottleFirstClick(lifecycleOwner.lifecycleScope) {
            clickListener()
        }
    }

    fun setDescriptionTextColor(@ColorRes colorRes: Int) {
        viewBinding.tvDescription.setTextColor(ContextCompat.getColor(context, colorRes))
    }

    fun setText(text: String) = with(viewBinding.etText) {
        if (this.text.toString() == text) return
        setText(text)
    }

    fun setStrokeBackground(@DrawableRes drawableRes: Int) {
        viewBinding.layoutTextField.setBackgroundResource(drawableRes)
    }

    private fun startCollapseAnimationHintLabel() {
        if (collapseValueAnimator.isRunning || !isExpend) {
            return
        }
        isExpend = false
        expendValueAnimator.cancel()
        collapseValueAnimator.start()
    }

    private fun startExpendAnimationHintLabel() {
        if (expendValueAnimator.isRunning || isExpend) {
            return
        }
        isExpend = true
        collapseValueAnimator.cancel()
        expendValueAnimator.start()
    }

    companion object {
        private const val SIZE_TEXT_COLLAPSE = 20
        private const val SIZE_TEXT_EXPEND = 13

        @JvmStatic
        @BindingAdapter(value = ["text_field_hint", "text_field_description"], requireAll = false)
        fun TextSelectionView.setTitleText(hint: String?, description: String?) {
            hint?.run {
                setHintText(this)
            }
            description?.run {
                setDescriptionText(this)
            }
        }
    }
}