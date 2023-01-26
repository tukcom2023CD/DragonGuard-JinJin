package com.dragonguard.android.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.dragonguard.android.R
import com.dragonguard.android.model.SearchModel

class SearchViewModel() {
//    var model = SearchModel()
    var onOptionListener = MutableLiveData<String>()
    var onSearchListener = MutableLiveData<String>()
    var onIconClickListener = MutableLiveData<Boolean>()

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