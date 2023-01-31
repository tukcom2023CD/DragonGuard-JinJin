package com.dragonguard.android.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivityMainBinding
import com.dragonguard.android.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var viewmodel = MainViewModel()
    private lateinit var versionDialog : Dialog

    //    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewmodel

        versionDialog = Dialog(this)
        versionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        versionDialog.setContentView(R.layout.version_dialog)
        //로그인 화면으로 넘어가기
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
/*        화면전환 테스트용 버튼
        binding.btnadd.setOnClickListener {
            count++
            binding.btnadd.setText(count.toString())
        }

 */
        viewmodel.onLookRanking.observe(this, Observer {
            if(viewmodel.onLookRanking.value == true){
                val intent = Intent(applicationContext, RankingsActivity::class.java)
                startActivity(intent)
            }
        })

    }

    private fun showDialog() {
        versionDialog.show()
    }

    private fun closeKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchName.windowToken, 0)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        closeKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu1, binding.toolbar.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout->{

            }
            R.id.faq->{
                val intent = Intent(applicationContext, FaqActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
            R.id.token_criterion->{
                val intent = Intent(applicationContext, CriterionActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
            R.id.version->{
                showDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
