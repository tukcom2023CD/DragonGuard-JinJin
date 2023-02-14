package com.dragonguard.android.recycleradapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.model.RepoContributorsItem
import kotlin.math.absoluteValue

/*
 선택한 repo의 contributor들의 정보를 나열하기 위한 recycleradapter
 */
class ContributorsAdapter (private val datas : ArrayList<RepoContributorsItem>, private val context: Context, private val colors: ArrayList<Int>) : RecyclerView.Adapter<ContributorsAdapter.ViewHolder>() {
    private var ranking  = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contributors_list,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val contRanking: TextView = itemView.findViewById(R.id.contribute_ranking)
        private val githubId : TextView = itemView.findViewById(R.id.contrubutor_id)
        private val color : ImageView = itemView.findViewById(R.id.contributor_color)

        fun bind(data1: RepoContributorsItem, data2: RepoContributorsItem) {
            if(contRanking.text.isNullOrEmpty()) {
                if(data1.commits == data2.commits) {
                    contRanking.text = (ranking -1).toString()
                } else {
                    contRanking.text = ranking.toString()
                }
                ranking++
            }
            githubId.text = data1.githubId
            var red = (Math.random()*255).toInt()
            var green = (Math.random()*255).toInt()
            var blue = (Math.random()*255).toInt()
            while((colors.last().red - red).absoluteValue < 20 || (colors.last().blue - blue).absoluteValue < 20 || (colors.last().green - green).absoluteValue < 20 || colors.contains(Color.rgb(red,green,blue))) {
                red = (Math.random()*255).toInt()
                green = (Math.random()*255).toInt()
                blue = (Math.random()*255).toInt()
            }
//            if((colors.last().red - red).absoluteValue < 20 || (colors.last().blue - blue).absoluteValue < 20 || (colors.last().green - green).absoluteValue < 20) {
//                red = (Math.random()*255).toInt()
//                green = (Math.random()*255).toInt()
//                blue = (Math.random()*255).toInt()
//
//            }
            color.imageTintList = ColorStateList.valueOf(Color.rgb(red,green,blue))
            colors.add(Color.rgb(red,green,blue))
        }

        fun bind(data1: RepoContributorsItem) {
            if(contRanking.text.isNullOrEmpty()) {
                contRanking.text = ranking.toString()
                ranking++
            }
            githubId.text = data1.githubId
            val red = (Math.random()*255).toInt()
            val green = (Math.random()*255).toInt()
            val blue = (Math.random()*255).toInt()
            color.imageTintList = ColorStateList.valueOf(Color.rgb(red,green,blue))
            colors.add(Color.rgb(red,green,blue))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position != 0) {
            holder.bind(datas[position], datas[(position-1)])
        } else {
            holder.bind(datas[position])
        }

    }

}