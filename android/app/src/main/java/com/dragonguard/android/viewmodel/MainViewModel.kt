package com.dragonguard.android.viewmodel


import androidx.lifecycle.MutableLiveData


class MainViewModel {
    var onSearchListener = MutableLiveData<String>()
    var onIconClickListener = MutableLiveData<Boolean>()
    var onLookRanking = MutableLiveData<Boolean>()

    fun clickSearchIcon() {
        onIconClickListener.value = true
    }

    fun searchRepo() {

    }

    fun lookRanking() {
        onLookRanking.value = true
    }

    fun compareRepo() {

    }

    fun schoolRanking() {

    }
}