package com.dragonguard.android.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.activity.compare.CompareSearchActivity
import com.dragonguard.android.activity.profile.UserProfileActivity
import com.dragonguard.android.activity.search.RepoContributorsActivity
import com.dragonguard.android.activity.search.SearchActivity
import com.dragonguard.android.databinding.RepositoryListBinding
import com.dragonguard.android.model.search.RepoNameModel
import com.dragonguard.android.model.search.RepoSearchResultModel
import com.dragonguard.android.model.search.UserNameModelItem

//검색한 레포지토리 나열하는 리사이클러뷰 어댑터 구현
class RepositoryProfileAdapter(
    private val datas: ArrayList<*>, private val context: Context,
    private val token: String, private val type: String, private val imgList: HashMap<String, Int>,
    private val repoCount: Int
) : RecyclerView.Adapter<RepositoryProfileAdapter.ViewHolder>() {
    private lateinit var binding: RepositoryListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = RepositoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //클릭리스너 구현
        fun bind(data: Any?) {
            when (data) {
                is RepoSearchResultModel -> {
                    binding.repoName.text = data.name
                    Log.d("name", "${data.name}")
                    val img = imgList[data.language]
                    if (img != null) {
                        binding.langImg.setBackgroundResource(img)
                    }
                    data.created_at?.let {
                        val parts = it.split("T")
                        val textToShow = parts[0] + " " + parts[1]
                        val last = textToShow.replace("Z", "")
                        binding.repoCreateDate.text = last
                    }
                    binding.repoLanguage.text = data.language
                    itemView.setOnClickListener {
//                Toast.makeText(context, "${repoName.text} 눌림", Toast.LENGTH_SHORT).show()

                        Log.d("몇번", "현재 repoCount : $repoCount")
                        when (repoCount) {
                            0 -> {
                                Intent(context, RepoContributorsActivity::class.java).apply {
                                    putExtra("repoName", data.name)
                                    putExtra("token", token)
                                }.run { context.startActivity(this) }
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
                is UserNameModelItem -> {
                    binding.repoName.text = data.name
                    itemView.setOnClickListener {
                        Log.d("users", "user = $data")
                        if(data.is_service_member) {
                            Intent(context, UserProfileActivity::class.java).apply {
                                putExtra("userName", data.name)
                                putExtra("token", token)
                            }.run { context.startActivity(this) }
                        } else {
                            Toast.makeText(context, "${data.name}은(는) 회원이 아닙니다.", Toast.LENGTH_SHORT).show()
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