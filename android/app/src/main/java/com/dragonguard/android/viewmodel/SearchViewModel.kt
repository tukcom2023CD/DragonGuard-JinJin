package com.dragonguard.android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dragonguard.android.connect.RepoSearchRepository
import com.dragonguard.android.model.Result

class SearchViewModel() : ViewModel() {
//    var model = SearchModel()
    private val repository = RepoSearchRepository()
    private val _result = MutableLiveData<List<Result>>()
    val result : LiveData<List<Result>>
        get() = _result

    fun getResult(name: String, count: Int) {
        val response = repository.searchApi(name, count)

    }

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