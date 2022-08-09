package com.mashup.ui.extensions

import com.mashup.R
import com.mashup.widget.TextFieldView

fun TextFieldView.setFailedUiOfTextField() {
    setHintTextColor(R.color.gray600)
    setDescriptionTextColor(R.color.red500)
    setTrailingImageIcon(R.drawable.ic_question_mark, R.color.red500)
    setStrokeForegroundDrawable(R.drawable.bg_text_field_out_line_error)
}

fun TextFieldView.setSuccessUiOfTextField() {
    setHintTextColor(R.color.gray600)
    setDescriptionTextColor(R.color.gray600)
    setTrailingImageIcon(R.drawable.ic_check, R.color.green500)
    if (isFocus()) {
        setStrokeForegroundDrawable(R.drawable.bg_text_field_out_line_primary)
    } else {
        setStrokeForegroundDrawable(R.drawable.bg_text_field_out_line_idle)
    }
}

fun TextFieldView.setEmptyUIOfTextField() {
    setHintTextColor(R.color.gray300)
    setDescriptionTextColor(R.color.gray600)
    setTrailingImageIcon(0)
    if (isFocus()) {
        setStrokeForegroundDrawable(R.drawable.bg_text_field_out_line_primary)
    } else {
        setStrokeForegroundDrawable(R.drawable.bg_text_field_out_line_idle)
    }
}