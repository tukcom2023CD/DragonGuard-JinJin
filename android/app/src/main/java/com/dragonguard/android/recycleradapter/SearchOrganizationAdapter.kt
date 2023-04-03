package com.dragonguard.android.recycleradapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.activity.menu.AuthOrgActivity
import com.dragonguard.android.activity.menu.SearchOrganizationActivity
import com.dragonguard.android.model.org.OrganizationNamesModel
import com.dragonguard.android.model.org.OrganizationNamesModelItem

class SearchOrganizationAdapter(private val datas : OrganizationNamesModel, private val context: Context, private val token: String) : RecyclerView.Adapter<SearchOrganizationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repository_list,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val orgName: TextView = itemView.findViewById(R.id.repo_name)

        //클릭리스너 구현
        fun bind(data: OrganizationNamesModelItem) {
            orgName.text = data.name
            orgName.setOnClickListener {
                val authOrg = context as SearchOrganizationActivity
                val intentW = Intent(context, AuthOrgActivity::class.java)
                intentW.putExtra("orgName", data.name)
                intentW.putExtra("orgId", data.id)
                authOrg.setResult(0, intentW)
                authOrg.finish()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
}