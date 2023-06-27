package com.dragonguard.android.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.activity.compare.CompareSearchActivity
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.databinding.RepositoryListBinding
import com.dragonguard.android.model.search.RepoSearchResultModel

//비교할 Repository를 나열하기 위한 recyclerview의 adapter
class SearchCompareRepoAdapter (private val datas : ArrayList<RepoSearchResultModel>, private val context: Context, count: Int, private val token: String) : RecyclerView.Adapter<SearchCompareRepoAdapter.ViewHolder>() {
    private var repoCount = count
    private lateinit var binding: RepositoryListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = RepositoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //클릭리스너 구현
        fun bind(data: RepoSearchResultModel, count: Int) {
            val mContext = context as CompareSearchActivity
            binding.repoName.text = data.name
            Log.d("name", "$data.name")
            itemView.setOnClickListener{
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("repo_name", data.name)
                intent.putExtra("token", token)
                context.setResult(count, intent)
                context.finish()
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position], repoCount)
    }

}