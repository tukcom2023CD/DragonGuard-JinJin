package com.dragonguard.android.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R

class LanguagesAdapter(private val languages: ArrayList<String>, private val languagesCheckBox: ArrayList<Boolean>): RecyclerView.Adapter<LanguagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguagesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.language_list,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = languages.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val checkBox = view.findViewById<CheckBox>(R.id.language_check)
        private val language = view.findViewById<TextView>(R.id.language_text)
        fun bind(position: Int){
            checkBox.isChecked = languagesCheckBox[position]
            language.text = languages[position]
            checkBox.setOnClickListener {
                languagesCheckBox[position] = checkBox.isChecked
            }
        }

    }

    override fun onBindViewHolder(holder: LanguagesAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }
}