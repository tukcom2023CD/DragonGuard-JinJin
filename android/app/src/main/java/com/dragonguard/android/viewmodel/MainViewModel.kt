package com.dragonguard.android.viewmodel


import androidx.lifecycle.MutableLiveData


class MainViewModel {
    var onSearchListener = MutableLiveData<String>()
    var onSearchClickListener = MutableLiveData<Boolean>()
    var onLookRanking = MutableLiveData<Boolean>()
    var onUserIconSelected = MutableLiveData<Boolean>()

    fun clickUserIcon() {
        onUserIconSelected.value = true
    }

    fun clickSearchIcon() {
        onSearchClickListener.value = true
    }


    fun lookRanking() {
        onLookRanking.value = true
    }

    fun compareRepo() {

    }

    fun schoolRanking() {

    }
}