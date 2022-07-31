package com.mashup.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(private val activity: Activity) {

    fun requestPermission(
        requestCode: Int,
        permission: String
    ) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(permission), requestCode
        )
    }

    fun checkGrantedPermission(
        permission: String,
        onRequestPermission: () -> Unit,
        onShowRationaleUi: () -> Unit
    ): Boolean {
        return if (!isPermissionGranted(permission)) {
            checkRationalePermission(
                permission, onRequestPermission, onShowRationaleUi
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