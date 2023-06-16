package com.mashup.core.common.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment.STYLE_NO_TITLE
import com.mashup.core.common.databinding.DialogCommonBinding
import com.mashup.core.common.extensions.visible

class CommonDialog(context: Context) : Dialog(context) {

    private val viewBinding: DialogCommonBinding =
        DialogCommonBinding.inflate(LayoutInflater.from(context), null, false)

    init {
        requestWindowFeature(STYLE_NO_TITLE)
        setContentView(viewBinding.root)

        window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.82).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    fun setTitle(text: String? = null) {
        viewBinding.tvTitle.text = text
    }

    fun setMessage(text: String? = null) {
        viewBinding.tvMessage.text = text
    }

    fun setNegativeButton(
        text: String = "취소",
        clickListener: (Dialog) -> Unit = { it.dismiss() }
    ) {
        with(viewBinding.btnNegative) {
            visible()
            setButtonText(text)
            setOnClickListener {
                clickListener(this@CommonDialog)
                dismiss()
            }
        }
    }

    fun setPositiveButton(
        text: String = "확인",
        clickListener: (Dialog) -> Unit = {}
    ) {
        with(viewBinding.btnPositive) {
            visible()
            setButtonText(text)
            setOnClickListener {
                clickListener.invoke(this@CommonDialog)
                dismiss()
            }
        }
    }
}
