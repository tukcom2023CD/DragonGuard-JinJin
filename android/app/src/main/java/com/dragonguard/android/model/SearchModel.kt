package com.dragonguard.android.model

import com.dragonguard.android.databinding.ActivitySearchBinding
import com.dragonguard.android.recycleradapter.RepositoryProfileAdapter
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class SearchModel() {
    lateinit var repositoryProfileAdapter : RepositoryProfileAdapter
    private var position = 0
    private var array1= ArrayList<String>()
    private val backendIp = ""
    private var count = 0
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(50, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    fun checkSearchOption(word: String): Boolean{
        return word=="Down"
    }


}