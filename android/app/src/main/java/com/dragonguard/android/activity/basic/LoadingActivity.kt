package com.dragonguard.android.activity.basic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dragonguard.android.R

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
    }
}