package com.mashup.widget

import android.animation.ValueAnimator
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.mashup.R
import com.mashup.databinding.ViewTextLayoutBinding

class TextFieldView @JvmOverloads constructor(
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
    }

    private fun initEditText() {
        viewBinding.etText.setOnFocusChangeListener { editText, hasFocus ->
            when {
                hasFocus && (editText as? EditText)?.text?.isEmpty() == true -> {
                    startExpendAnimationHintLabel()
                }
                !hasFocus && (editText as? EditText)?.text?.isEmpty() == true -> {
                    startCollapseAnimationHintLabel()
                }
            }
            setStrokeBackground(
                if (hasFocus) R.drawable.bg_text_field_out_line_primary else R.drawable.bg_text_field_out_line_idle
            )
        }
    }

    fun setHintText(hint: String) {
        viewBinding.tvHintLabel.text = hint
    }

    fun setHintTextColor(@ColorRes colorRes: Int) {
        viewBinding.tvHintLabel.setTextColor(ContextCompat.getColor(context, colorRes))
    }

    fun setDescriptionText(description: String) {
        viewBinding.tvDescription.visibility = View.VISIBLE
        viewBinding.tvDescription.text = description
    }

    fun setDescriptionTextColor(@ColorRes colorRes: Int) {
        viewBinding.tvDescription.setTextColor(ContextCompat.getColor(context, colorRes))
    }

    fun setTrailingImageIcon(@DrawableRes drawableRes: Int) {
        viewBinding.imgIcon.setImageResource(drawableRes)
    }

    fun setStrokeBackground(@DrawableRes drawableRes: Int) {
        viewBinding.layoutTextField.setBackgroundResource(drawableRes)
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

    fun addOnTextChangedListener(onTextChanged: (String) -> Unit) {
        viewBinding.etText.addTextChangedListener {
            onTextChanged(it.toString())
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        return super.onSaveInstanceState().run {
            TextFieldSaveState(this).apply {
                etText = viewBinding.etText.text.toString()
            }
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val textFieldSaveState = state as? TextFieldSaveState ?: return
        super.onRestoreInstanceState(textFieldSaveState.superState)
        viewBinding.etText.setText(textFieldSaveState.etText)
    }

    class TextFieldSaveState : BaseSavedState {
        var etText: String? = ""

        constructor(superState: Parcelable?) : super(superState) {}

        constructor(source: Parcel) : super(source) {
            etText = source.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeString(etText)
        }

        companion object CREATOR : Parcelable.Creator<TextFieldSaveState> {
            override fun createFromParcel(parcel: Parcel): TextFieldSaveState {
                return TextFieldSaveState(parcel)
            }

            override fun newArray(size: Int): Array<TextFieldSaveState?> {
                return arrayOfNulls(size)
            }
        }
    }

    companion object {
        private const val SIZE_TEXT_COLLAPSE = 20
        private const val SIZE_TEXT_EXPEND = 13

        @JvmStatic
        @BindingAdapter(value = ["text_field_hint", "text_field_description"], requireAll = false)
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