package com.dragonguard.android.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.databinding.LanguageListBinding

class LanguagesAdapter(private val languages: ArrayList<String>, private val languagesCheckBox: ArrayList<Boolean>): RecyclerView.Adapter<LanguagesAdapter.ViewHolder>() {
    private lateinit var binding: LanguageListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguagesAdapter.ViewHolder {
        binding = LanguageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = languages.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int){
            binding.languageCheck.isChecked = languagesCheckBox[position]
            binding.languageText.text = languages[position]
            binding.languageCheck.setOnClickListener {
                languagesCheckBox[position] = binding.languageCheck.isChecked
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