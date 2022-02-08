package com.example.superrestoration_client.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    private var selectList: MutableLiveData<MutableSet<Int>> = MutableLiveData()
    init {
        selectList.value = mutableSetOf()
    }
    fun addModel(position: Int): Boolean { return selectList.value!!.add(position) }
    fun removeModel(position: Int): Boolean { return selectList.value!!.remove(position) }
    fun getSelectList(): MutableSet<Int> { return selectList.value!! }
}