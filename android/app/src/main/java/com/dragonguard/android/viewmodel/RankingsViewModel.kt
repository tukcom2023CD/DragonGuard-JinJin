package com.dragonguard.android.viewmodel

import androidx.lifecycle.MutableLiveData

class RankingsViewModel {
    var myRepoListener = MutableLiveData<Boolean>()
    var myOrganizationListener = MutableLiveData<Boolean>()
    var myUniversityInternalListener = MutableLiveData<Boolean>()
    var totalUniversitiesListener = MutableLiveData<Boolean>()
    var userRankingListener = MutableLiveData<Boolean>()
    var totalRepoListener = MutableLiveData<Boolean>()

    fun clickMyRepo() {
        myRepoListener.value = true
    }

    fun clickMyOrganization() {
        myOrganizationListener.value = true
    }

    fun clickMyUniversityInternal() {
        myUniversityInternalListener.value = true
    }

    fun clickTotalUniversities() {
        totalUniversitiesListener.value = true
    }

    fun clickUserRanking() {
        userRankingListener.value = true
    }

    fun clickTotalRepo() {
        totalRepoListener.value = true
    }
}