package com.mashup.core.common.utils

import android.app.Dialog
import android.content.Context

class ProgressDialogContainer {
    private var loadingDialog: Dialog? = null

    fun showLoading(context: Context) {
        loadingDialog?.show() ?: run {
            loadingDialog = ProgressbarUtil.show(context)
        }
    }

    fun hideLoading() = loadingDialog?.dismiss()

    fun clear() {
        loadingDialog = null
    }
}
