package com.dragonguard.android.viewmodel


import androidx.lifecycle.MutableLiveData
import com.dragonguard.android.connect.ApiRepository


class MainViewModel {
    private val repository = ApiRepository()

    fun getSearchTierResult(id: Int): String{
        return repository.getTier(id)
    }
    var onSearchClickListener = MutableLiveData<Boolean>()
    var onUserIconSelected = MutableLiveData<Boolean>()

    fun clickUserIcon() {
        onUserIconSelected.value = true
    }

    fun clickSearchIcon() {
        onSearchClickListener.value = true
    }

}