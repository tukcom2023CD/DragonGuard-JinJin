package com.dragonguard.android.viewmodel

import androidx.lifecycle.MutableLiveData

class MenuViewModel {
    var onLogoutSelected = MutableLiveData<Boolean>()
    var onFAQSelected = MutableLiveData<Boolean>()
    var onCriterionSelected = MutableLiveData<Boolean>()
    var onVersionSelected = MutableLiveData<Boolean>()

    fun clickLogout() {
        onLogoutSelected.value = true
    }

    fun clickFAQ() {
        onFAQSelected.value = true
    }

    fun clickCriterion() {
        onCriterionSelected.value = true
    }

    fun clickVersion() {
        onVersionSelected.value = true
    }
}