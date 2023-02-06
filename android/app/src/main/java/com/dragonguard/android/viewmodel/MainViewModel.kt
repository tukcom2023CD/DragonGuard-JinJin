package com.dragonguard.android.viewmodel


import androidx.lifecycle.MutableLiveData
import com.dragonguard.android.connect.ApiRepository
import com.dragonguard.android.model.RegisterGithubIdModel
import com.dragonguard.android.model.UserInfoModel


class MainViewModel {
    private val repository = ApiRepository()

    fun getSearchTierResult(id: Int): UserInfoModel{
        return repository.getUserInfo(id)
    }

    fun postRegister(body: RegisterGithubIdModel): Int {
        return repository.postRegister(body)
    }

    fun getCommits(id: Int): String{
        return repository.getTier(id)
    }

    fun getRankings(id: Int) {

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