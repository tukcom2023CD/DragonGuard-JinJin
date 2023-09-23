package com.dragonguard.android.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.activity.search.SearchFilterActivity
import com.dragonguard.android.databinding.ActivitySearchFilterBinding
import com.dragonguard.android.databinding.LanguageListBinding
import com.google.android.material.chip.Chip

class LanguagesAdapter(private val languages: MutableList<String>, context: Context, private val type: String,
                        private val activityBinding: ActivitySearchFilterBinding):
    RecyclerView.Adapter<LanguagesAdapter.ViewHolder>() {

    private val filterActivity = context as SearchFilterActivity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguagesAdapter.ViewHolder {
        val binding = LanguageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = languages.size

    inner class ViewHolder(private val binding: LanguageListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            Log.d("type", type)
            binding.languageText.text = languages[position]
            val chip = Chip(filterActivity)
            chip.chipBackgroundColor = ColorStateList.valueOf(Color.rgb(195, 202,251))
            chip.setTextAppearanceResource(R.style.textAppearance)
            chip.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            chip.text =binding.languageText.text.toString()
            binding.languageText.setOnClickListener {
                if(binding.languageText.tag == "on") {
                    binding.languageText.tag = "off"
                    binding.languageText.setBackgroundResource(R.drawable.shadow_off)
                    when(type) {
                        "language" -> {
                            for (i in 0 until activityBinding.languageFilters.childCount) {
                                val chipL: Chip = activityBinding.languageFilters.getChildAt(i) as Chip
                                if (chipL.text.toString() == binding.languageText.text.toString()) {
                                    val language = filterActivity.map[type]!!.split(",").toMutableList()
                                    language.remove(chipL.text.toString())
                                    val sb = StringBuilder()
                                    language.forEachIndexed { index, s ->
                                        if(index != language.lastIndex) {
                                            sb.append("$s,")
                                        } else {
                                            sb.append(s)
                                        }
                                    }
                                    filterActivity.map[type] = sb.toString()
                                    activityBinding.languageFilters.removeView(chipL)
                                    break // 원하는 Chip을 찾았으므로 반복문을 종료합니다.
                                }
                            }
                        }
                        "stars" -> {
                            for (i in 0 until activityBinding.starFilters.childCount) {
                                val chipL: Chip = activityBinding.starFilters.getChildAt(i) as Chip
                                Log.d("child", chipL.text.toString())
                                if (chipL.text.toString() == binding.languageText.text.toString()) {
                                    filterActivity.map[type] = ""
                                    activityBinding.starFilters.removeView(chipL)
                                    break // 원하는 Chip을 찾았으므로 반복문을 종료합니다.
                                }
                            }
                            filterActivity.map[type] = ""
                        }
                        "forks" -> {
                            for (i in 0 until activityBinding.forkFilters.childCount) {
                                val chipL: Chip = activityBinding.forkFilters.getChildAt(i) as Chip
                                if (chipL.text.toString() == binding.languageText.text.toString()) {
                                    filterActivity.map[type] = ""
                                    activityBinding.forkFilters.removeView(chipL)
                                    break // 원하는 Chip을 찾았으므로 반복문을 종료합니다.
                                }
                            }
                            filterActivity.map[type] = ""
                        }
                        "topics" -> {
                            for (i in 0 until activityBinding.topicFilters.childCount) {
                                val chipL: Chip = activityBinding.topicFilters.getChildAt(i) as Chip
                                if (chipL.text.toString() == binding.languageText.text.toString()) {
                                    filterActivity.map[type] = ""
                                    activityBinding.topicFilters.removeView(chipL)
                                    break // 원하는 Chip을 찾았으므로 반복문을 종료합니다.
                                }
                            }
                            filterActivity.map[type] = ""
                        }
                    }
                } else {
                    binding.languageText.tag = "on"
                    binding.languageText.setBackgroundResource(R.drawable.shadow_on)
                    when(type) {
                        "language" -> {
                            val before = filterActivity.map[type]
                            if(!before.isNullOrBlank()) {
                                filterActivity.map.put(type, "$before,${binding.languageText.text}")
                            } else {
                                filterActivity.map.put(type, binding.languageText.text.toString())
                            }
                            chip.setOnClickListener {
                                val language = filterActivity.map[type]!!.split(",").toMutableList()
                                language.remove(chip.text.toString())
                                val sb = StringBuilder()
                                language.forEachIndexed { index, s ->
                                    if(index != language.lastIndex) {
                                        sb.append("$s,")
                                    } else {
                                        sb.append(s)
                                    }
                                }
                                filterActivity.map[type] = sb.toString()
                                activityBinding.languageFilters.removeView(it)
                            }
                            activityBinding.languageFilters.addView(chip)
                        }
                        else -> {
                            filterActivity.map.put(type, binding.languageText.text.toString())
                            when(type) {
                                "stars" -> {
                                    chip.setOnClickListener {
                                        filterActivity.map[type] = ""
                                        activityBinding.starFilters.removeView(it)
                                    }
                                    activityBinding.starFilters.addView(chip)
                                }
                                "forks" -> {
                                    chip.setOnClickListener {
                                        filterActivity.map[type] = ""
                                        activityBinding.forkFilters.removeView(it)
                                    }
                                    activityBinding.forkFilters.addView(chip)
                                }
                                "topics" -> {
                                    chip.setOnClickListener {
                                        filterActivity.map[type] = ""
                                        activityBinding.topicFilters.removeView(it)
                                    }
                                    activityBinding.topicFilters.addView(chip)
                                }
                            }
                        }
                    }
                }
            }
        }

    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: LanguagesAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }
}