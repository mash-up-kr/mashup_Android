package com.mashup.core.common.widget

import android.animation.ValueAnimator
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.InputFilter
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.mashup.core.common.R
import com.mashup.core.common.databinding.ViewTextLayoutBinding

class TextFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val viewBinding: ViewTextLayoutBinding =
        ViewTextLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    val inputtedText: String
        get() = viewBinding.etText.text.toString()

    private var foregroundDrawableRes: Int = R.drawable.bg_text_field_out_line_idle

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

    private var focusChangedListener: ((Boolean) -> Unit)? = null

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
        setInputType(TextFieldInputType.TEXT)
    }

    fun setText(text: String) {
        if (inputtedText == text || text.isEmpty()) return
        viewBinding.etText.setText(text)
        viewBinding.etText.setSelection(text.length - 1)
        startExpendAnimationHintLabel()
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
            if (foregroundDrawableRes == R.drawable.bg_text_field_out_line_idle) {
                setStrokeForegroundDrawable(
                    if (hasFocus) R.drawable.bg_text_field_out_line_primary else R.drawable.bg_text_field_out_line_idle
                )
            }
            focusChangedListener?.invoke(hasFocus)
        }
    }

    fun setHintText(hint: String) = with(viewBinding.tvHintLabel) {
        if (text == hint) return@with
        text = hint
    }

    fun setHintTextColor(@ColorRes colorRes: Int) {
        viewBinding.tvHintLabel.setTextColor(ContextCompat.getColor(context, colorRes))
    }

    fun setDescriptionText(description: String) = with(viewBinding.tvDescription) {
        if (text.toString() == description) return@with
        visibility = View.VISIBLE
        text = description
    }

    fun setDescriptionTextColor(@ColorRes colorRes: Int) {
        viewBinding.tvDescription.setTextColor(ContextCompat.getColor(context, colorRes))
    }

    fun setTrailingImageIcon(
        @DrawableRes drawableRes: Int,
        @ColorRes colorTintRes: Int = R.color.black
    ) {
        viewBinding.imgIcon.run {
            setImageResource(drawableRes)
            imageTintList = ContextCompat.getColorStateList(
                context,
                colorTintRes
            )
        }
    }

    fun setStrokeForegroundDrawable(@DrawableRes drawableRes: Int) {
        foregroundDrawableRes = drawableRes
        viewBinding.layoutTextField.foreground =
            ResourcesCompat.getDrawable(context.resources, drawableRes, null)
    }

    fun setBackgroundDrawable(@DrawableRes drawableRes: Int) {
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

    fun setInputType(inputType: TextFieldInputType) {
        viewBinding.etText.inputType = when (inputType) {
            TextFieldInputType.PASSWORD -> {
                TYPE_CLASS_TEXT or TYPE_TEXT_VARIATION_PASSWORD
            }
            TextFieldInputType.TEXT_CAP_CHARACTERS -> {
                with(viewBinding.etText) {
                    privateImeOptions = "defaultInputmode=english"
                    filters += arrayOf<InputFilter>(InputFilter.AllCaps())
                }
                TYPE_TEXT_FLAG_CAP_CHARACTERS
            }
            else -> {
                TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
        }
    }

    fun setMaxLength(length: Int) {
        viewBinding.etText.filters += arrayOf<InputFilter>(InputFilter.LengthFilter(length))
    }

    fun isFocus() = viewBinding.etText.hasFocus()

    fun setEnabledTextField(enabled: Boolean) {
        viewBinding.etText.isEnabled = enabled
        setBackgroundDrawable(
            if (enabled) R.drawable.bg_text_field_enabled else R.drawable.bg_text_field_not_enabled
        )
    }

    fun setFocus() {
        with(viewBinding.etText) {
            requestFocus()
            isFocusableInTouchMode = true
            val imm: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, 0)
        }
    }

    fun clearTextFieldFocus() {
        viewBinding.etText.clearFocus()
    }

    fun addOnTextChangedListener(onTextChanged: (String) -> Unit) {
        viewBinding.etText.addTextChangedListener {
            onTextChanged(it.toString())
        }
    }

    fun setOnFocusChangedListener(onFocusChanged: (Boolean) -> Unit) {
        focusChangedListener = onFocusChanged
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
        if (!textFieldSaveState.etText.isNullOrEmpty()) {
            startExpendAnimationHintLabel()
        }
    }

    class TextFieldSaveState : BaseSavedState {
        var etText: String? = ""

        constructor(superState: Parcelable?) : super(superState)

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
        fun TextFieldView.bindText(hint: String?, description: String?) {
            hint?.run {
                setHintText(this)
            }
            description?.run {
                setDescriptionText(this)
            }
        }

        @JvmStatic
        @BindingAdapter("text_field_input_type")
        fun TextFieldView.bindInputType(inputType: TextFieldInputType) {
            setInputType(inputType)
        }

        @JvmStatic
        @BindingAdapter("text_field_length")
        fun TextFieldView.bindLength(length: Int) {
            setMaxLength(length)
        }
    }

    enum class TextFieldInputType {
        PASSWORD, TEXT, TEXT_CAP_CHARACTERS
    }
}
