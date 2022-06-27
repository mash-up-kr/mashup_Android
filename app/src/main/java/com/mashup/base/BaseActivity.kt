package com.mashup.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mashup.utils.keyboard.RootViewDeferringInsetsCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {
    abstract val layoutId: Int

    protected val viewBinding: V by lazy {
        DataBindingUtil.inflate(
            LayoutInflater.from(this), layoutId, null, false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.lifecycleOwner = this
        setContentView(viewBinding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        initKeyboardScroll()
        initViews()
        initObserves()
    }

    open fun initViews() {
        /* explicitly empty */
    }

    open fun initObserves() {
        /* explicitly empty */
    }

    open fun initKeyboardScroll() {
        val deferringInsetsListener = RootViewDeferringInsetsCallback(
            persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
            deferredInsetTypes = WindowInsetsCompat.Type.ime()
        )
        ViewCompat.setWindowInsetsAnimationCallback(viewBinding.root, deferringInsetsListener)
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root, deferringInsetsListener)
    }

    protected fun flowLifecycleScope(
        state: Lifecycle.State = Lifecycle.State.STARTED,
        block: suspend CoroutineScope.() -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(state) {
                block.invoke(this)
            }
        }
    }
}