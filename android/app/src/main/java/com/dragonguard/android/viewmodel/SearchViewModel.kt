package com.dragonguard.android.viewmodel

import androidx.lifecycle.MutableLiveData

class SearchViewModel() {
//    var model = SearchModel()
    var onOptionListener = MutableLiveData<String>()
    var onSearchListener = MutableLiveData<String>()
    var onIconClickListener = MutableLiveData<Boolean>()
    var onUserIconSelected = MutableLiveData<Boolean>()

    fun clickUserIcon() {
        onUserIconSelected.value = true
    }

    fun clickSearchOption(){
        if(onOptionListener.value == "down"){
            onOptionListener.value = "up"
        }else{
            onOptionListener.value = "down"
        }
    }

    fun clickSearch(){
        onIconClickListener.value = true
    }

}