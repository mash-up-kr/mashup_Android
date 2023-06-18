package com.mashup.core.common.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(private val activity: Activity) {

    fun requestPermission(
        permission: String,
        requestCode: Int = 0
    ) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            requestCode
        )
    }

    fun checkGrantedPermission(
        permission: String,
        onRequestPermission: () -> Unit,
        onShowRationaleUi: () -> Unit
    ): Boolean {
        return if (!isPermissionGranted(permission)) {
            checkRationalePermission(
                permission,
                onRequestPermission,
                onShowRationaleUi
            )
        } else {
            true
        }
    }

    private fun checkRationalePermission(
        permission: String,
        onRequestPermission: () -> Unit,
        onShowRationaleUi: () -> Unit
    ): Boolean {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            onShowRationaleUi()
        } else {
            onRequestPermission()
        }
        return false
    }

    fun isPermissionGranted(permission: String) =
        ContextCompat.checkSelfPermission(
            activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
}
