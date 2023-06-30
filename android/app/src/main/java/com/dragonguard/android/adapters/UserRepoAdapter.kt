package com.dragonguard.android.adapters

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.activity.search.RepoContributorsActivity
import com.dragonguard.android.databinding.RepositoryListBinding

class UserRepoAdapter (private val datas : List<String>, private val context: Context, private val token: String) : RecyclerView.Adapter<UserRepoAdapter.ViewHolder>() {
    private lateinit var binding: RepositoryListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = RepositoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //클릭리스너 구현
        fun bind(data: String) {
            binding.repoName.text = data
            Log.d("name", "$data.name")
            itemView.setOnClickListener{
//                Toast.makeText(context, "${repoName.text} 눌림", Toast.LENGTH_SHORT).show()
                Intent(context, RepoContributorsActivity::class.java).apply{
                    putExtra("repoName", data)
                    putExtra("token", token)
                    addFlags(FLAG_ACTIVITY_NEW_TASK)
                }.run{context.startActivity(this)}
            }
        }
    }
    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

}