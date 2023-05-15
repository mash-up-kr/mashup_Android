package com.mashup.core.common.utils

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.safeShow(fragmentManager: FragmentManager) {
    fragmentManager.beginTransaction().add(this, this::class.simpleName).commitAllowingStateLoss()
}