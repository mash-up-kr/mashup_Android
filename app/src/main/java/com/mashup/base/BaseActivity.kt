package com.mashup.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<V : ViewBinding>(
    private val bindingFactory: (LayoutInflater) -> V
) : AppCompatActivity() {

    protected val viewBinding: V by lazy {
        bindingFactory.invoke(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}