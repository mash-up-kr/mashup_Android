package com.mashup.ui.extensions

import com.mashup.R
import com.mashup.widget.TextFieldView

fun TextFieldView.setFailedUiOfTextField() {
    setHintTextColor(R.color.red)
    setDescriptionTextColor(R.color.red)
    setTrailingImageIcon(R.drawable.ic_question_mark)
    setBackgroundResource(R.drawable.bg_text_field_out_line_error)
}

fun TextFieldView.setSuccessUiOfTextField() {
    setHintTextColor(R.color.gray600)
    setDescriptionTextColor(R.color.gray600)
    setTrailingImageIcon(R.drawable.ic_checked_success)
    setBackgroundResource(R.drawable.bg_text_field_out_line_idle)
}