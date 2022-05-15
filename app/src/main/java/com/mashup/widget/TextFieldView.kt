package com.mashup.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.databinding.BindingAdapter
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.mashup.databinding.ViewTextFieldBinding
import kotlin.math.exp

class TextFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val viewBinding: ViewTextFieldBinding =
        ViewTextFieldBinding.inflate(LayoutInflater.from(context), this, true)

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

    init {
        initViews()
    }

    private fun initViews() {
        initFocusEditText()
    }

    private fun initFocusEditText() {
        viewBinding.etText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                startExpendAnimationHintLabel()
            } else {
                startCollapseAnimationHintLabel()
            }
        }
    }

    fun setHintText(hint: String) {
        viewBinding.tvHintLabel.text = hint
    }

    fun setDescriptionText(description: String) {
        viewBinding.tvDescription.text = description
    }

    private fun startCollapseAnimationHintLabel() {
        if (collapseValueAnimator.isRunning) {
            return
        }
        expendValueAnimator.cancel()
        collapseValueAnimator.start()
    }

    private fun startExpendAnimationHintLabel() {
        if (expendValueAnimator.isRunning) {
            return
        }
        collapseValueAnimator.cancel()
        expendValueAnimator.start()
    }

    companion object {
        private const val SIZE_TEXT_COLLAPSE = 20
        private const val SIZE_TEXT_EXPEND = 13

        @BindingAdapter(value = ["text_field_hint", "text_field_description"])
        fun TextFieldView.setTitleText(hint: String?, description: String?) {
            hint?.run {
                setHintText(this)
            }
            description?.run {
                setDescriptionText(this)
            }
        }
    }
}