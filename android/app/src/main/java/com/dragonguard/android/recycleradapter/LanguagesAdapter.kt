package com.dragonguard.android.recycleradapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
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
    private lateinit var binding: LanguageListBinding
    private val filterActivity = context as SearchFilterActivity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguagesAdapter.ViewHolder {
        binding = LanguageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = languages.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int){
            binding.languageText.text = languages[position]
            binding.languageText.setOnClickListener {
                if(binding.languageText.tag == "on") {
                    binding.languageText.tag = "off"
                    binding.languageText.setBackgroundColor(Color.rgb(195, 202, 251))
                    filterActivity.map[type] = ""
                } else {
                    binding.languageText.tag = "on"
                    binding.languageText.setBackgroundColor(Color.rgb(225, 228, 253))
                    when(type) {
                        "language" -> {
                            val before = filterActivity.map[type]
                            if(!before.isNullOrBlank()) {
                                filterActivity.map.put(type, "$before,${binding.languageText.text}")
                            } else {
                                filterActivity.map.put(type, binding.languageText.text.toString())
                            }
                            val chip = Chip(filterActivity)
                            chip.chipBackgroundColor = ColorStateList.valueOf(Color.rgb(225, 228, 253))
                            chip.setTextAppearanceResource(R.style.textAppearance)
                            chip.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                            chip.text =binding.languageText.text.toString()
                            chip.setOnClickListener {
                                val language = filterActivity.map[type]!!.split(",").toMutableList()
                                language.remove(chip.text.toString())
                                val sb = StringBuilder()
                                language.forEach {
                                    sb.append("$it,")
                                }
                                sb.deleteAt(sb.lastIndex)
                                filterActivity.map[type] = sb.toString()
                                activityBinding.languageFilters.removeView(chip)
                            }
                            activityBinding.languageFilters.addView(chip)
                        }
                        else -> {
                            filterActivity.map.put(type, binding.languageText.text.toString())
                            val chip = Chip(filterActivity)
                            chip.chipBackgroundColor = ColorStateList.valueOf(Color.rgb(225, 228, 253))
                            chip.setTextAppearanceResource(R.style.textAppearance)
                            chip.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                            chip.text =binding.languageText.text.toString()
                            chip.setOnClickListener {
                                filterActivity.map[type] = ""
                                activityBinding.languageFilters.removeView(chip)
                            }
                            activityBinding.languageFilters.addView(chip)
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