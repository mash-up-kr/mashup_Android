package com.mashup.ui.qrcode

import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentQrCodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QRCodeFragment : BaseFragment<FragmentQrCodeBinding>() {
    override val layoutId: Int = R.layout.fragment_qr_code
}