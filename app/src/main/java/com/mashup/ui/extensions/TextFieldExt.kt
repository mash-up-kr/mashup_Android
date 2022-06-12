package com.mashup.ui.extensions

import com.mashup.R
import com.mashup.ui.model.Validation
import com.mashup.widget.TextFieldView

fun TextFieldView.setValidation(validation: Validation) {
    when (validation) {
        Validation.SUCCESS -> {
            setSuccessUiOfTextField()
        }
        Validation.EMPTY -> {
            setEmptyUIOfTextField()
        }
        Validation.FAILED -> {
            setFailedUiOfTextField()
        }
    }
}

fun TextFieldView.setFailedUiOfTextField() {
    setHintTextColor(R.color.red500)
    setDescriptionTextColor(R.color.red500)
    setTrailingImageIcon(R.drawable.ic_question_mark)
    setStrokeBackground(R.drawable.bg_text_field_out_line_error)
}

fun TextFieldView.setSuccessUiOfTextField() {
    setHintTextColor(R.color.gray600)
    setDescriptionTextColor(R.color.gray600)
    setTrailingImageIcon(R.drawable.ic_checked)
    if (isFocus()) {
        setStrokeBackground(R.drawable.bg_text_field_out_line_primary)
    } else {
        setStrokeBackground(R.drawable.bg_text_field_out_line_idle)
    }
}

fun TextFieldView.setEmptyUIOfTextField() {
    setHintTextColor(R.color.gray600)
    setDescriptionTextColor(R.color.gray600)
    setTrailingImageIcon(0)
    if (isFocus()) {
        setStrokeBackground(R.drawable.bg_text_field_out_line_primary)
    } else {
        setStrokeBackground(R.drawable.bg_text_field_out_line_idle)
    }
}