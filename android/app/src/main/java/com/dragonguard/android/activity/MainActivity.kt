package com.dragonguard.android.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainActivity = this

        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }
    fun compRepo(){
        val intent = Intent(applicationContext, SearchActivity::class.java)
        startActivity(intent)
    }
}
