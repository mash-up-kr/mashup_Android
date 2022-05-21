package com.mashup.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.mashup.R
import com.mashup.databinding.ViewTextLayoutBinding

class TextSelectView @JvmOverloads constructor(
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

    init {
        initViews()
    }

    private fun initViews() {
        initEditText()
        setBackgroundStrokeColor(R.color.gray300)
    }

    private fun initEditText() {
        // set read-only edittext
        viewBinding.etText.keyListener = null
    }

    private fun setBackgroundStrokeColor(@ColorRes colorRes: Int) {
        viewBinding.layoutTextField.backgroundTintList =
            ContextCompat.getColorStateList(context, colorRes)
    }

    fun setHintText(hint: String) {
        viewBinding.tvHintLabel.text = hint
    }

    fun setText(text: String) {
        if (text.isEmpty()) {
            startCollapseAnimationHintLabel()
            setBackgroundStrokeColor(R.color.gray300)
        } else {
            startExpendAnimationHintLabel()
            setBackgroundStrokeColor(R.color.primary)
        }
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

        @BindingAdapter(
            value = ["text_select_hint", "text_select_text", "text_select_description"],
            requireAll = false
        )
        fun TextSelectView.setTitleText(hint: String?, text: String?, description: String?) {
            hint?.run {
                setHintText(this)
            }
            text?.run {
                setText(this)
            }
            description?.run {
                setDescriptionText(this)
            }
        }
    }
}