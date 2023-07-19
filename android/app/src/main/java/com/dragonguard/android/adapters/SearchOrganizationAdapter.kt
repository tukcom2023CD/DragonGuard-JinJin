package com.dragonguard.android.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.activity.menu.AuthOrgActivity
import com.dragonguard.android.activity.menu.SearchOrganizationActivity
import com.dragonguard.android.databinding.RepositoryListBinding
import com.dragonguard.android.model.org.OrganizationNamesModel
import com.dragonguard.android.model.org.OrganizationNamesModelItem

class SearchOrganizationAdapter(private val datas : OrganizationNamesModel, private val context: Context, private val token: String) : RecyclerView.Adapter<SearchOrganizationAdapter.ViewHolder>() {
    private lateinit var binding: RepositoryListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = RepositoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //클릭리스너 구현
        fun bind(data: OrganizationNamesModelItem) {
            binding.repoName.text = data.name
            binding.repoName.setOnClickListener {
                val authOrg = context as SearchOrganizationActivity
                val intentW = Intent(context, AuthOrgActivity::class.java)
                intentW.putExtra("orgName", data.name)
                intentW.putExtra("orgId", data.id)
                intentW.putExtra("endPoint", data.email_endpoint)
                authOrg.setResult(0, intentW)
                authOrg.finish()
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
}