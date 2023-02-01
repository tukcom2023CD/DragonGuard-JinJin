package com.dragonguard.android.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivityMenuBinding
import com.dragonguard.android.viewmodel.MenuViewModel

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    var viewmodel = MenuViewModel()
    private lateinit var versionDialog : Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu)
        binding.menuActivity = viewmodel

        versionDialog = Dialog(this)
        versionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        versionDialog.setContentView(R.layout.version_dialog)

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        viewmodel.onLogoutSelected.observe(this, Observer {

        })

        viewmodel.onFAQSelected.observe(this, Observer {
            if(viewmodel.onFAQSelected.value == true) {
                val intent = Intent(applicationContext, FaqActivity::class.java)
                startActivity(intent)
            }
        })

        viewmodel.onCriterionSelected.observe(this, Observer {
            if(viewmodel.onCriterionSelected.value == true) {
                val intent = Intent(applicationContext, CriterionActivity::class.java)
                startActivity(intent)
            }
        })

        viewmodel.onVersionSelected.observe(this, Observer {
            if(viewmodel.onVersionSelected.value == true){
                showDialog()
            }
        })
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