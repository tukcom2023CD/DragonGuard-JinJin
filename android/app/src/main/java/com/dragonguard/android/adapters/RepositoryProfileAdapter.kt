package com.dragonguard.android.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.activity.compare.CompareSearchActivity
import com.dragonguard.android.activity.search.RepoContributorsActivity
import com.dragonguard.android.activity.search.SearchActivity
import com.dragonguard.android.databinding.RepositoryListBinding
import com.dragonguard.android.model.search.RepoSearchResultModel

//검색한 레포지토리 나열하는 리사이클러뷰 어댑터 구현
class RepositoryProfileAdapter (private val datas : ArrayList<RepoSearchResultModel>, private val context: Context,
                                private val token: String, private val type: String, private val imgList: HashMap<String, Int>,
                                private val repoCount: Int) : RecyclerView.Adapter<RepositoryProfileAdapter.ViewHolder>() {
    private lateinit var binding: RepositoryListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = RepositoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //클릭리스너 구현
        fun bind(data: RepoSearchResultModel) {
            binding.repoName.text = data.name
            Log.d("name", "${data.name}")
            val img= imgList[data.language]
            if(img != null) {
                binding.langImg.setBackgroundResource(img)
            }
            data.created_at?.let{
                val parts = it.split("T")
                val textToShow = parts[0] + " " + parts[1]
                val last = textToShow.replace("Z", "")
                binding.repoCreateDate.text = last
            }
            binding.repoLanguage.text = data.language
            itemView.setOnClickListener{
//                Toast.makeText(context, "${repoName.text} 눌림", Toast.LENGTH_SHORT).show()
                if(type == "USERS") {
//                    Intent(context, UserDetailActivity::class.java).apply{
//                        putExtra("githubId", data.name)
//                        putExtra("token", token)
//                    }.run{context.startActivity(this)}
                } else {
                    Log.d("몇번", "현재 repoCount : $repoCount")
                    when(repoCount) {
                        0 -> {
                            Intent(context, RepoContributorsActivity::class.java).apply{
                                putExtra("repoName", data.name)
                                putExtra("token", token)
                            }.run{context.startActivity(this)}
                        }
                        else -> {
                            context as SearchActivity
                            val intent = Intent()
                            intent.putExtra("repoName", data.name)
                            intent.putExtra("token", token)
                            context.setResult(repoCount, intent)
                            context.finish()
                        }
                    }
                }
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