package com.dragonguard.android.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.dragonguard.android.R
import com.dragonguard.android.recycleradapter.HorizontalItemDecorator
import com.dragonguard.android.recycleradapter.LanguagesAdapter
import com.dragonguard.android.recycleradapter.VerticalItemDecorator

class FilterDialog(private val languages: ArrayList<String>,
                   private val languagesCheckBox: ArrayList<Boolean>
): DialogFragment(){
    private lateinit var languagesAdapter: LanguagesAdapter
    private lateinit var recyclerView : RecyclerView
    var star = ""
    var fork = ""
    var topic = ""
    private lateinit var search: View
    private lateinit var option: ImageView

    override fun onCreateDialog(savedInstance: Bundle?): Dialog {
        search = layoutInflater.inflate(R.layout.activity_search, null)
        option = search.findViewById<ImageView>(R.id.option_icon)
        val v = layoutInflater.inflate(R.layout.filter_dialog, null)
        recyclerView = v.findViewById(R.id.languages_filter)
        val maindlgBuilder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(    // 메인 다이얼로그
            requireContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        maindlgBuilder.setView(v)
        val dlg = maindlgBuilder.create()

        languagesAdapter = LanguagesAdapter(languages, languagesCheckBox)
        recyclerView.adapter = languagesAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(VerticalItemDecorator(20))
        recyclerView.addItemDecoration(HorizontalItemDecorator(10))


        val cancel = v.findViewById<Button>(R.id.filter_cancel)
        val choose = v.findViewById<Button>(R.id.filter_choose)
        val starGroup = v.findViewById<RadioGroup>(R.id.group_star)
        val forkGroup = v.findViewById<RadioGroup>(R.id.group_fork)
        val topicGroup= v.findViewById<RadioGroup>(R.id.group_topics)
        cancel.setOnClickListener {
            dlg.cancel()
        }
        choose.setOnClickListener {
            when(starGroup.checkedRadioButtonId) {
                R.id.star_asc -> {
                    star = "asc"
                }
                R.id.star_desc -> {
                    star = "desc"
                }
            }
            when(forkGroup.checkedRadioButtonId) {
                R.id.fork_asc -> {
                    fork = "asc"
                }
                R.id.fork_desc -> {
                    fork= "desc"
                }
            }
            when(topicGroup.checkedRadioButtonId) {
                R.id.zero_topic -> {
                    topic = "0"
                }
                R.id.one_topic -> {
                    topic = "1"
                }
                R.id.two_topics -> {
                    topic = "2"
                }
                R.id.three_topics -> {
                    topic = "3"
                }
                R.id.fourormore_topics -> {
                    topic = "4~"
                }
            }
            option.performClick()
            Toast.makeText(requireContext(), "star : $star, fork : $fork topics : $topic", Toast.LENGTH_SHORT).show()
            dlg.cancel()
        }
        return dlg
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        option.performClick()
        Toast.makeText(requireContext(), "star : $star, fork : $fork topics : $topic", Toast.LENGTH_SHORT).show()
    }


}