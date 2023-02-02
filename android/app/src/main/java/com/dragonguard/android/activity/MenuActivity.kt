package com.dragonguard.android.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var versionDialog : Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu)

        versionDialog = Dialog(this)
        versionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        versionDialog.setContentView(R.layout.version_dialog)

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.logout.setOnClickListener {

        }

        binding.faq.setOnClickListener {
            val intent = Intent(applicationContext, FaqActivity::class.java)
            startActivity(intent)
        }

        binding.tokenCriterion.setOnClickListener {
            val intent = Intent(applicationContext, CriterionActivity::class.java)
            startActivity(intent)
        }

        binding.version.setOnClickListener {
            showDialog()
        }

    }

    private fun showDialog() {
        versionDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}