package com.dragonguard.android.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.databinding.TokenListBinding
import com.dragonguard.android.model.klip.TokenHistoryModelItem


class TokenListAdapter (private val datas : ArrayList<TokenHistoryModelItem>, private val context: Context) : RecyclerView.Adapter<TokenListAdapter.ViewHolder>() {
    private lateinit var binding: TokenListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = TokenListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //클릭리스너 구현
        fun bind(data: TokenHistoryModelItem) {
            val parts = data.created_at!!.split("T")
            val textToShow = "${parts[0]} ${parts[1]}"
            Log.d("parts", "나누기 $parts")
            Log.d("parts", "합치기 $textToShow")
            binding.createdDate.text = textToShow.split(".")[0]
            binding.tokenType.text = data.contribute_type
            binding.tokenAmount.text = data.amount.toString()
            binding.tokenHistoryFrame.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(data.transaction_hash_url)
                startActivity(context, intent, null)
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