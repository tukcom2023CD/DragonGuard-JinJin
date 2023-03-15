package com.dragonguard.android.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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
                   private val languagesCheckBox: ArrayList<Boolean>,
                   private val option: ImageView,
                   private val filterMap: MutableMap<String, String>
): DialogFragment(){
    private lateinit var languagesAdapter: LanguagesAdapter
    private lateinit var recyclerView : RecyclerView
    var star = ""
    var fork = ""
    var topic = ""
    var type = ""
    private lateinit var search: View

    override fun onCreateDialog(savedInstance: Bundle?): Dialog {
        search = layoutInflater.inflate(R.layout.activity_search, null)
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

        val filterLayout = v.findViewById<LinearLayout>(R.id.filter_layout)
        val cancel = v.findViewById<Button>(R.id.filter_cancel)
        val choose = v.findViewById<Button>(R.id.filter_choose)
        val starGroup = v.findViewById<RadioGroup>(R.id.group_star)
        val forkGroup = v.findViewById<RadioGroup>(R.id.group_fork)
        val topicGroup= v.findViewById<RadioGroup>(R.id.group_topics)
        val searchType = v.findViewById<RadioGroup>(R.id.group_type)
        val languages = v.findViewById<RecyclerView>(R.id.languages_filter)
        star = ""
        fork = ""
        topic = ""
        type = ""
        searchType.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.type_user) {
                filterLayout.visibility = View.GONE
                starGroup.clearCheck()
                forkGroup.clearCheck()
                topicGroup.clearCheck()
            } else {
                filterLayout.visibility = View.VISIBLE
            }
        }
        cancel.setOnClickListener {
            dlg.cancel()
        }
        choose.setOnClickListener {
            when(searchType.checkedRadioButtonId) {
                R.id.type_user -> {
                    type = "USERS"
                }
                R.id.type_repository -> {
                    type = "REPOSITORIES"
                    when(starGroup.checkedRadioButtonId) {
                        R.id.star_range0-> {
                            star = "0..9"
                        }
                        R.id.star_range1 -> {
                            star = "10..49"
                        }
                        R.id.star_range2 -> {
                            star = "50..99"
                        }
                        R.id.star_range3 -> {
                            star = "100..499"
                        }
                        R.id.star_range4 -> {
                            star = ">=500"
                        }
                    }
                    when(forkGroup.checkedRadioButtonId) {
                        R.id.fork_range0 -> {
                            fork = "0..9"
                        }
                        R.id.fork_range1 -> {
                            fork= "10..49"
                        }
                        R.id.fork_range2 -> {
                            fork= "50..99"
                        }
                        R.id.fork_range3 -> {
                            fork= "100..499"
                        }
                        R.id.fork_range4 -> {
                            fork= ">=500"
                        }
                    }
                    when(topicGroup.checkedRadioButtonId) {
                        R.id.topic0 -> {
                            topic = "0"
                        }
                        R.id.topic1 -> {
                            topic = "1"
                        }
                        R.id.topic2 -> {
                            topic = "2"
                        }
                        R.id.topic3 -> {
                            topic = "3"
                        }
                        R.id.topic_over4 -> {
                            topic = ">=4"
                        }
                    }
                }
            }
            dlg.cancel()
        }
        return dlg
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        filterMap.clear()
        if(star != "") {
            filterMap["stars"] = star
        }
        if(fork != "") {
            filterMap["forks"] = fork
        }
        if(topic != "") {
            filterMap["topics"] = topic
        }
        if(type != "") {
            filterMap["type"] = type
        }
        option.performClick()
//        Toast.makeText(requireContext(), "star : $star, fork : $fork topics : $topic", Toast.LENGTH_SHORT).show()
    }


}