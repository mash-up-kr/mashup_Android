package com.mashup.ui.danggn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

class ShakeDanggnActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ShakeDanggnActivity::class.java).apply {

        }
    }
}
