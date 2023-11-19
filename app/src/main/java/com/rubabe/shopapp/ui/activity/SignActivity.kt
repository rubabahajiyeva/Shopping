package com.rubabe.shopapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rubabe.shopapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
    }

}